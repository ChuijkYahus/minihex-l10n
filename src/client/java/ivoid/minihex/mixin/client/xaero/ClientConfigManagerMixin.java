package ivoid.minihex.mixin.client.xaero;

import ivoid.minihex.MinihexClient;
import ivoid.minihex.features.personalmodifiers.api.DisorientedState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.profile.ConfigProfile;
import xaero.map.common.config.option.WorldMapProfiledConfigOptions;

@Mixin(value = ClientConfigManager.class, remap = false)
public abstract class ClientConfigManagerMixin {
    @Inject(method = "getEffective(Lxaero/lib/common/config/profile/ConfigProfile;Lxaero/lib/common/config/option/ConfigOption;)Ljava/lang/Object;", at = @At("HEAD"), cancellable = true, require = 0)
    private void disableRadar(ConfigProfile config, ConfigOption<?> option, CallbackInfoReturnable<Object> cir) {
        if (option == WorldMapProfiledConfigOptions.DISPLAY_TRACKED_PLAYERS
                || option == WorldMapProfiledConfigOptions.MINIMAP_RADAR
                || option == MinimapProfiledConfigOptions.DISPLAY_RADAR
                || option == MinimapProfiledConfigOptions.WAYPOINTS_IN_WORLD
        ) {
            if (MinihexClient.INSTANCE.getClientState().map(s -> s.getDisoriented() != DisorientedState.NORMAL).orElse(false)) {
                cir.setReturnValue(false);
            }
        } else if (option == MinimapProfiledConfigOptions.DISPLAY_MINIMAP) {
            if (MinihexClient.INSTANCE.getClientState().map(s -> s.getDisoriented() == DisorientedState.NO_MAP).orElse(false)) {
                cir.setReturnValue(false);
            }
        }
    }
}
