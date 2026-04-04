package ivoid.minihex.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import ivoid.minihex.features.personalmodifiers.PersonalModifierState;
import ivoid.minihex.inits.MinihexComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class ModifierFrailLivingEntityMixin {
    @ModifyReturnValue(method = "getMaxHealth", at = @At("RETURN"))
    private float overrideMaxHealth(float original) {
        if (((LivingEntity) (Object) this) instanceof PlayerEntity player) {
            PersonalModifierState state = MinihexComponents.INSTANCE.getPERSONAL_MODIFIERS().get(player).getState();

            if (state.isFrail()) {
                return Math.min((float) state.getFrail(), original);
            }
        }

        return original;
    }
}
