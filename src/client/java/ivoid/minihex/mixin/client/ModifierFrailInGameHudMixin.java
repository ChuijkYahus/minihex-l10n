package ivoid.minihex.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import ivoid.minihex.features.personalmodifiers.PersonalModifierState;
import ivoid.minihex.inits.MinihexComponents;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public class ModifierFrailInGameHudMixin {
    @ModifyExpressionValue(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
    private double modifyMaxHealth(double original, @Local PlayerEntity player) {
        PersonalModifierState state = MinihexComponents.INSTANCE.getPERSONAL_MODIFIERS().get(player).getState();

        if (state.isFrail()) {
            return Math.min(state.getFrail(), original);
        } else {
            return original;
        }
    }
}
