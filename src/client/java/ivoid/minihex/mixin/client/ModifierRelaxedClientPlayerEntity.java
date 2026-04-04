package ivoid.minihex.mixin.client;

import ivoid.minihex.features.personalmodifiers.PersonalModifierState;
import ivoid.minihex.inits.MinihexComponents;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ModifierRelaxedClientPlayerEntity {
    @Inject(method = "canSprint", at = @At("HEAD"), cancellable = true)
    private void disableSprint(CallbackInfoReturnable<Boolean> cir) {
        PersonalModifierState state = MinihexComponents.INSTANCE.getPERSONAL_MODIFIERS().get(this).getState();

        if (state.getRelaxed()) cir.setReturnValue(false);
    }
}
