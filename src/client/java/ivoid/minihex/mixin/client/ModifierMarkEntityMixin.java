package ivoid.minihex.mixin.client;

import at.petrak.hexcasting.client.ClientTickCounter;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import ivoid.minihex.MinihexClient;
import ivoid.minihex.features.personalmodifiers.PersonalModifierState;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.UUID;

@Mixin(Entity.class)
public abstract class ModifierMarkEntityMixin {
    @Shadow
    public abstract UUID getUuid();

    @ModifyReturnValue(method = "getTeamColorValue", at = @At("RETURN"))
    private int applyMarkColor(int original) {
        Optional<PersonalModifierState> state = MinihexClient.INSTANCE.getClientState();

        if (state.isPresent() && state.get().getMark().containsKey(getUuid())) {
            return state.get().getMark().get(getUuid()).getColor(ClientTickCounter.getTotal());
        }

        return original;
    }
}
