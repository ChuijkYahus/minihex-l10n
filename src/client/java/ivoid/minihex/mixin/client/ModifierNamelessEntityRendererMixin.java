package ivoid.minihex.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ivoid.minihex.MinihexClient;
import ivoid.minihex.features.personalmodifiers.api.NamelessState;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public abstract class ModifierNamelessEntityRendererMixin {
    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;hasLabel(Lnet/minecraft/entity/Entity;)Z"))
    private boolean hideNametag(boolean original) {
        if (MinihexClient.INSTANCE.getClientState()
                .map(s -> s.getNameless() == NamelessState.NAMELESS)
                .orElse(false)) {
            return false;
        } else {
            return original;
        }
    }

    @WrapOperation(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I"))
    private int limitNametag(TextRenderer instance, Text text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextRenderer.TextLayerType layerType, int backgroundColor, int light, Operation<Integer> original) {
        if (MinihexClient.INSTANCE.getClientState()
                .map(s -> s.getNameless() != NamelessState.NAMED)
                .orElse(false)
        ) {
            // Do not render through blocks
            layerType = TextRenderer.TextLayerType.NORMAL;
        }

        return original.call(instance, text, x, y, color, shadow, matrix, vertexConsumers, layerType, backgroundColor, light);
    }
}
