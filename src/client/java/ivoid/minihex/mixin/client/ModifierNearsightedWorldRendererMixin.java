package ivoid.minihex.mixin.client;

import ivoid.minihex.features.personalmodifiers.PersonalModifierState;
import ivoid.minihex.inits.MinihexComponents;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldRenderer.class)
public abstract class ModifierNearsightedWorldRendererMixin {
    @Inject(method = "hasBlindnessOrDarkness", at = @At("HEAD"), cancellable = true)
    private void markNearsightedAsLimitedView(Camera camera, CallbackInfoReturnable<Boolean> cir) {
        // Returning true disables skybox rendering
        Entity entity = camera.getFocusedEntity();

        if (entity instanceof PlayerEntity player) {
            PersonalModifierState state = MinihexComponents.INSTANCE.getPERSONAL_MODIFIERS().get(player).getState();

            if (!state.isNearsighted()) return;

            cir.setReturnValue(true);
        }
    }
}
