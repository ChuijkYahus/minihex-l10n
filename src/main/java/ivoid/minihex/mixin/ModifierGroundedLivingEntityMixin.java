package ivoid.minihex.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import ivoid.minihex.features.personalmodifiers.PersonalModifierState;
import ivoid.minihex.inits.MinihexComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class ModifierGroundedLivingEntityMixin {
    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isOnGround()Z"))
    private boolean preventJumping(boolean original) {
        LivingEntity thisObject = (LivingEntity) (Object) this;

        if (thisObject instanceof PlayerEntity player) {
            PersonalModifierState state = MinihexComponents.INSTANCE.getPERSONAL_MODIFIERS().get(player).getState();

            if (state.getGrounded()) {
                return false;
            }
        }

        return original;
    }
}
