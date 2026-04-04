package ivoid.minihex.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import ivoid.minihex.features.personalmodifiers.PersonalModifierState;
import ivoid.minihex.inits.MinihexComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Mixin(EntityPredicates.class)
public abstract class ModifierIntangibleEntityPredicatesMixin {
    @ModifyReturnValue(method = "canBePushedBy", at = @At("RETURN"))
    private static Predicate<Entity> ignorePlayerCollisions(Predicate<Entity> original, @Local(argsOnly = true) Entity self) {
        if (!(self instanceof PlayerEntity selfPlayer)) return original;

        return entity -> {
            if (entity instanceof PlayerEntity otherPlayer) {
                PersonalModifierState selfState = MinihexComponents.INSTANCE.getPERSONAL_MODIFIERS().get(selfPlayer).getState();
                PersonalModifierState otherState = MinihexComponents.INSTANCE.getPERSONAL_MODIFIERS().get(otherPlayer).getState();

                if (selfState.getIntangible() || otherState.getIntangible()) {
                    return false;
                }
            }

            return original.test(entity);
        };
    }
}
