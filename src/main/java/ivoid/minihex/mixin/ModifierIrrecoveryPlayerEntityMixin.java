package ivoid.minihex.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import ivoid.minihex.features.personalmodifiers.PersonalModifierState;
import ivoid.minihex.inits.MinihexComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class ModifierIrrecoveryPlayerEntityMixin {
    @Inject(method = "canFoodHeal", at = @At("HEAD"), cancellable = true)
    private void disableFoodHeal(CallbackInfoReturnable<Boolean> cir) {
        if (((LivingEntity) (Object) this) instanceof PlayerEntity player) {
            PersonalModifierState state = MinihexComponents.INSTANCE.getPERSONAL_MODIFIERS().get(player).getState();

            if (state.getIrrecovery()) {
                cir.setReturnValue(false);
            }
        }
    }

    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
    private boolean disablePeacefulHeal(boolean original) {
        if (((LivingEntity) (Object) this) instanceof PlayerEntity player) {
            PersonalModifierState state = MinihexComponents.INSTANCE.getPERSONAL_MODIFIERS().get(player).getState();

            if (state.getIrrecovery()) {
                return false;
            }
        }

        return original;
    }
}
