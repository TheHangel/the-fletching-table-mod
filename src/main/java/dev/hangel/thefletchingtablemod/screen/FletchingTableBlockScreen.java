package dev.hangel.thefletchingtablemod.screen;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Atlases;
import net.minecraft.util.Identifier;

public class FletchingTableBlockScreen extends HandledScreen<FletchingTableBlockScreenHandler> {
    public static final Identifier GUI_TEXTURE =
            Identifier.of(TheFletchingTableMod.MOD_ID, "textures/gui/fletching_table_gui.png");

    public FletchingTableBlockScreen(FletchingTableBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        MinecraftClient client = MinecraftClient.getInstance();
        SpriteAtlasTexture atlas  = client.getAtlasManager().getAtlasTexture(Atlases.ITEMS);

        context.drawTexture(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 256);

        Sprite arrowSprite = atlas.getSprite(Identifier.ofVanilla("item/arrow"));

        Sprite potionSprite = atlas.getSprite(Identifier.ofVanilla("item/potion"));

        Sprite splashPotionSprite = atlas.getSprite(Identifier.ofVanilla("item/splash_potion"));

        Sprite lingeringPotionSprite = atlas.getSprite(Identifier.ofVanilla("item/lingering_potion"));

        Sprite tippedArrowSpriteBase = atlas.getSprite(Identifier.ofVanilla("item/tipped_arrow_base"));

        Sprite tippedArrowSpriteHead = atlas.getSprite(Identifier.ofVanilla("item/tipped_arrow_head"));

        int black = 0xFF000000;

        if(this.handler.getSlot(0).getStack().isEmpty()) {
            context.drawSpriteStretched(RenderPipelines.GUI_TEXTURED, arrowSprite, x + 25, y + 34, 16, 16, black);
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
            context.drawSpriteStretched(RenderPipelines.GUI_TEXTURED, current, x + 78, y + 34, 16, 16, black);
        }

        if(this.handler.getSlot(2).getStack().isEmpty()) {
            context.drawSpriteStretched(RenderPipelines.GUI_TEXTURED, tippedArrowSpriteBase, x + 132, y + 34, 16, 16, black);
            context.drawSpriteStretched(RenderPipelines.GUI_TEXTURED, tippedArrowSpriteHead, x + 132, y + 34, 16, 16, black);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
