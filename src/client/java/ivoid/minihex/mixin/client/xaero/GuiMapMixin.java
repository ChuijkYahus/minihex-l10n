package ivoid.minihex.mixin.client.xaero;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ivoid.minihex.MinihexClient;
import ivoid.minihex.features.personalmodifiers.api.DisorientedState;
import net.minecraft.client.gui.DrawContext;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.map.gui.GuiMap;
import xaero.map.radar.tracker.PlayerTrackerMenuRenderer;

@Mixin(value = GuiMap.class, remap = false)
public class GuiMapMixin {
    @Shadow
    public boolean playersMenu;

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lxaero/map/misc/Misc;hasEffect(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/effect/StatusEffect;)Z", ordinal = 0), require = 0)
    private boolean disableMap(boolean original) {
        if (MinihexClient.INSTANCE.getClientState()
                .map(s -> s.getDisoriented() == DisorientedState.NO_MAP)
                .orElse(false)
        ) {
            return true;
        }

        return original;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resource/language/I18n;translate(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"), require = 0)
    private String changeMessage(String key) {
        if (key.equals("gui.xaero_no_world_map_message")) {
            if (MinihexClient.INSTANCE.getClientState()
                    .map(s -> s.getDisoriented() == DisorientedState.NO_MAP)
                    .orElse(false)
            ) {
                return "minihex.modifier.misc:mod/disoriented/map_disabled";
            }
        }

        return key;
    }

    @WrapOperation(method = "togglePlayerMenu", at = @At(value = "FIELD", target = "Lxaero/map/gui/GuiMap;playersMenu:Z", ordinal = 0, opcode = Opcodes.GETFIELD), require = 0)
    private boolean disablePlayerMenu(GuiMap instance, Operation<Boolean> original) {
        if (MinihexClient.INSTANCE.getClientState()
                .map(s -> s.getDisoriented() != DisorientedState.NORMAL)
                .orElse(false)
        ) {
            return true; // gets negated to false by the caller
        } else {
            return original.call(instance);
        }
    }

    @Inject(method = "render", at = @At("HEAD"), require = 0)
    private void disablePlayerMenu(DrawContext guiGraphics, int scaledMouseX, int scaledMouseY, float partialTicks, CallbackInfo ci) {
        if (MinihexClient.INSTANCE.getClientState()
                .map(s -> s.getDisoriented() != DisorientedState.NORMAL)
                .orElse(false)
        ) {
            playersMenu = false;
        }
    }

    @WrapOperation(method = "onInputPress", at = @At(value = "INVOKE", target = "Lxaero/map/radar/tracker/PlayerTrackerMenuRenderer;onShowPlayersButton(Lxaero/map/gui/GuiMap;II)V"), require = 0)
    private void displayPlayerMenuKeybind(PlayerTrackerMenuRenderer instance, GuiMap screen, int width, int height, Operation<Void> original) {
        if (MinihexClient.INSTANCE.getClientState()
                .map(s -> s.getDisoriented() == DisorientedState.NORMAL)
                .orElse(true)
        ) {
            original.call(instance, screen, width, height);
        }
    }
}
