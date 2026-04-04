package ivoid.minihex.mixin.client.xaero;

import ivoid.minihex.MinihexClient;
import ivoid.minihex.features.personalmodifiers.api.DisorientedState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.player.tracker.PlayerTrackerMinimapElement;
import xaero.hud.minimap.player.tracker.PlayerTrackerMinimapElementRenderer;

@Mixin(value = PlayerTrackerMinimapElementRenderer.class, remap = false)
public abstract class PlayerTrackerIconRendererMixin {
    @Inject(method = "renderElement(Lxaero/hud/minimap/player/tracker/PlayerTrackerMinimapElement;ZZDFDDLxaero/hud/minimap/element/render/MinimapElementRenderInfo;Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;)Z", at = @At("HEAD"), cancellable = true, require = 0)
    private void hideRadarElements(PlayerTrackerMinimapElement<?> e, boolean highlighted, boolean outOfBounds, double optionalDepth, float optionalScale, double partialX, double partialY, MinimapElementRenderInfo renderInfo, DrawContext guiGraphics, VertexConsumerProvider.Immediate vanillaBufferSource, CallbackInfoReturnable<Boolean> cir) {
        if (MinihexClient.INSTANCE.getClientState()
                .map(s -> s.getDisoriented() != DisorientedState.NORMAL)
                .orElse(false)
        ) {
            cir.setReturnValue(false);
        }
    }
}
