package ivoid.minihex.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import ivoid.minihex.features.personalmodifiers.PersonalModifierState;
import ivoid.minihex.inits.MinihexComponents;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public abstract class ModifierNearsightedBackgroundRendererMixin {
    @Inject(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", shift = At.Shift.BEFORE))
    private static void applyNearsightedFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci, @Local Entity entity, @Local BackgroundRenderer.FogData fogData) {
        if (entity instanceof PlayerEntity player) {
            PersonalModifierState state = MinihexComponents.INSTANCE.getPERSONAL_MODIFIERS().get(player).getState();

            if (!state.isNearsighted()) return;

            float fogEnd = (float) state.getNearsighted();
            float fogStart = 0;

            if (fogData.fogEnd > fogEnd) {
                // Override it
                fogData.fogStart = fogStart;
                fogData.fogEnd = fogEnd;
                fogData.fogShape = FogShape.SPHERE;
            }
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V"))
    private static void applyFogColor(float red, float green, float blue, float alpha, Operation<Void> original, @Local(argsOnly = true) Camera camera) {
        Entity entity = camera.getFocusedEntity();

        if (entity instanceof PlayerEntity player) {
            PersonalModifierState state = MinihexComponents.INSTANCE.getPERSONAL_MODIFIERS().get(player).getState();

            if (!state.isNearsighted()) {
                original.call(red, green, blue, alpha);
                return;
            }

            BackgroundRendererAccessor.setRed(0);
            BackgroundRendererAccessor.setGreen(0);
            BackgroundRendererAccessor.setBlue(0);

            original.call(0f, 0f, 0f, alpha);
        } else {
            original.call(red, green, blue, alpha);
        }
    }
}
