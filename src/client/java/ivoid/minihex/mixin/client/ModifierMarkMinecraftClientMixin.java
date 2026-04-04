package ivoid.minihex.mixin.client;

import ivoid.minihex.MinihexClient;
import ivoid.minihex.features.personalmodifiers.PersonalModifierState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(MinecraftClient.class)
public abstract class ModifierMarkMinecraftClientMixin {
    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    public void markEntities(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        Optional<PersonalModifierState> state = MinihexClient.INSTANCE.getClientState();

        if (state.isPresent() && state.get().getMark().containsKey(entity.getUuid())) {
            cir.setReturnValue(true);
        }
    }
}
