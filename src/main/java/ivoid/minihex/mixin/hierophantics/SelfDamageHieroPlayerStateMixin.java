package ivoid.minihex.mixin.hierophantics;

import at.petrak.hexcasting.api.casting.iota.Iota;
import ivoid.minihex.features.selfdamage.OpSelfDamage;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import robotgiggle.hierophantics.data.HieroPlayerState;

@Mixin(value = HieroPlayerState.class, remap = false)
public class SelfDamageHieroPlayerStateMixin {
    @Inject(method = "triggerMinds(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    private void cancelTrigger(ServerPlayerEntity player, String triggerType, CallbackInfo ci) {
        // Shouldn't ever happen due to hierophantics' own check on not triggering during overcast damage, but just in case
        if (triggerType.equals("damage") && OpSelfDamage.INSTANCE.getIS_DAMAGING()) {
            ci.cancel();
        }
    }

    @Inject(method = "checkTypedDamage", at = @At("HEAD"), cancellable = true)
    private void cancelTypeTrigger(ServerPlayerEntity player, String type, Iota initialIota, CallbackInfo ci) {
        if (type.equals("hexcasting.overcast") && OpSelfDamage.INSTANCE.getIS_DAMAGING()) {
            ci.cancel();
        }
    }
}
