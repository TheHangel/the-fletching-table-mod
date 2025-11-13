package dev.hangel.thefletchingtablemod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FletchingTableBlockScreen extends HandledScreen<FletchingTableBlockScreenHandler> {
    public static final Identifier GUI_TEXTURE =
            Identifier.of(TheFletchingTableMod.MOD_ID, "textures/gui/fletching_table_gui.png");

    public FletchingTableBlockScreen(FletchingTableBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);

        int guiX = (width - backgroundWidth) / 2;
        int guiY = (height - backgroundHeight) / 2;

        RenderSystem.setShaderColor(0.3f, 0.3f, 0.3f, 1.0f);
        Sprite sprite = MinecraftClient.getInstance()
                .getBakedModelManager()
                .getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
                .getSprite(Identifier.ofVanilla("item/potion"));

        context.drawSprite(guiX + 78, guiY + 34, 0, 16, 16, sprite);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
}
