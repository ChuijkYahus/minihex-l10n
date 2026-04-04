package ivoid.minihex.mixin;

import ivoid.minihex.features.personalmodifiers.PersonalModifierState;
import ivoid.minihex.inits.MinihexComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ModifierIntangibleEntityMixin {
    @Inject(method = "collidesWith", at = @At("HEAD"), cancellable = true)
    private void ignorePlayerCollisions(Entity other, CallbackInfoReturnable<Boolean> cir) {
        Entity thisObject = (Entity) (Object) this;

        if (thisObject instanceof PlayerEntity player && other instanceof PlayerEntity otherPlayer) {
            PersonalModifierState state = MinihexComponents.INSTANCE.getPERSONAL_MODIFIERS().get(player).getState();
            PersonalModifierState otherState = MinihexComponents.INSTANCE.getPERSONAL_MODIFIERS().get(otherPlayer).getState();

            if (state.getIntangible() || otherState.getIntangible()) {
                cir.setReturnValue(false);
            }
        }
    }
}
