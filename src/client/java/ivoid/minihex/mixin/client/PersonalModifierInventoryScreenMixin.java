package ivoid.minihex.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import ivoid.minihex.Minihex;
import ivoid.minihex.features.personalmodifiers.PersonalModifiers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;


@Mixin(InventoryScreen.class)
public abstract class PersonalModifierInventoryScreenMixin {
    @Unique
    private static final Identifier MODIFIER_ICON_TEXTURE = new Identifier(Minihex.MOD_ID, "textures/gui/hexxy_info.png");

    @Inject(method = "render", at = @At("TAIL"))
    private void renderIconAndTooltip(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null || !PersonalModifiers.INSTANCE.shouldShowInventoryIcon(player)) return;

        // Locate the recipe book icon
        List<Drawable> drawables = ((ScreenAccessor) this).minihex$getDrawables();
        TexturedButtonWidget book = (TexturedButtonWidget) drawables.stream()
                .filter(b -> b instanceof TexturedButtonWidget button &&
                        ((TexturedButtonWidgetAccessor) button).minihex$getTexture()
                                .equals(InventoryScreen.BACKGROUND_TEXTURE))
                .findFirst().orElse(null);

        int buttonX, buttonY;
        if (book == null) {
            // Removed by some other mod; copy the vanilla calculations for button pos instead
            buttonX = ((HandledScreenAccessor) this).minihex$getX() + 104;
            buttonY = ((ScreenAccessor) this).minihex$getHeight() / 2 - 22;
        } else {
            buttonX = book.getX();
            buttonY = book.getY();
        }

        buttonX += 24;
        buttonY += 1;

        RenderSystem.enableDepthTest();
        context.drawTexture(MODIFIER_ICON_TEXTURE, buttonX, buttonY, 16, 16, 0, 0, 32, 32, 32, 32);

        if (buttonX <= mouseX && mouseX <= buttonX + 16
                && buttonY <= mouseY && mouseY <= buttonY + 16
        ) {
            context.drawTooltip(
                    ((ScreenAccessor) this).minihex$getTextRenderer(),
                    PersonalModifiers.INSTANCE.getInventoryIconTooltip(
                            player,
                            Screen.hasShiftDown() ? PersonalModifiers.ShowDetails.SHOW_DETAILS : PersonalModifiers.ShowDetails.SHOW_HINT,
                            MinecraftClient.getInstance().options.advancedItemTooltips
                    ),
                    Optional.empty(),
                    mouseX,
                    mouseY
            );
        }
    }
}
