package ivoid.minihex.mixin.client;

import ivoid.minihex.MinihexClient;
import ivoid.minihex.features.personalmodifiers.PersonalModifierState;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EntityRenderer.class)
public abstract class ModifierDisregardEntityMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void disregardEntity(Entity entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        Optional<PersonalModifierState> optState = MinihexClient.INSTANCE.getClientState();

        if (optState.isEmpty()) return;

        PersonalModifierState state = optState.get();

        if (state.getDisregardedEntities().contains(entity.getUuid())) {
            cir.setReturnValue(false);
            return;
        }

        if (!state.getDisregardedEntityTypes().isEmpty() && entity.getType().getRegistryEntry().getKey()
                .map(k -> state.getDisregardedEntityTypes().contains(k.getValue()))
                .orElse(false)
        ) {
            cir.setReturnValue(false);
        }
    }
}
