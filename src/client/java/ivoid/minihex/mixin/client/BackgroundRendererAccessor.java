package ivoid.minihex.mixin.client;

import net.minecraft.client.render.BackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BackgroundRenderer.class)
public interface BackgroundRendererAccessor {
    @Accessor
    static void setRed(float red) {
    }

    @Accessor
    static void setGreen(float green) {
    }

    @Accessor
    static void setBlue(float blue) {
    }
}
