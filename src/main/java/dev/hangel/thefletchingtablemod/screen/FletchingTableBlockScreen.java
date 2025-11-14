package dev.hangel.thefletchingtablemod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
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

        MinecraftClient client = MinecraftClient.getInstance();
        SpriteAtlasTexture atlas  = client.getBakedModelManager().getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);

        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        RenderSystem.setShaderColor(0.0f, 0.0f, 0.0f, 1.0f);

        Sprite arrowSprite = atlas.getSprite(Identifier.ofVanilla("item/arrow"));

        Sprite potionSprite = atlas.getSprite(Identifier.ofVanilla("item/potion"));

        Sprite splashPotionSprite = atlas.getSprite(Identifier.ofVanilla("item/splash_potion"));

        Sprite lingeringPotionSprite = atlas.getSprite(Identifier.ofVanilla("item/lingering_potion"));

        Sprite tippedArrowSpriteBase = atlas.getSprite(Identifier.ofVanilla("item/tipped_arrow_base"));

        Sprite tippedArrowSpriteHead = atlas.getSprite(Identifier.ofVanilla("item/tipped_arrow_head"));

        if(this.handler.getSlot(0).getStack().isEmpty()) {
            context.drawSprite(x + 25, y + 34, 0, 16, 16, arrowSprite);
        }

        long time = client.world != null ? client.world.getTime() : 0;

        int periodTicks = 20;
        int index = (int) ((time / periodTicks) % 3);

        Sprite current;
        switch (index) {
            case 0 -> current = potionSprite;
            case 1 -> current = splashPotionSprite;
            default -> current = lingeringPotionSprite;
        }

        if(this.handler.getSlot(1).getStack().isEmpty()) {
            context.drawSprite(x + 78, y + 34, 0, 16, 16, current);
        }

        if(this.handler.getSlot(2).getStack().isEmpty()) {
            context.drawSprite(x + 132, y + 34, 0, 16, 16, tippedArrowSpriteBase);
            context.drawSprite(x + 132, y + 34, 0, 16, 16, tippedArrowSpriteHead);
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
