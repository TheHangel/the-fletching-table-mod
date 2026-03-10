package dev.hangel.thefletchingtablemod.screen;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class FletchingTableBlockScreen extends AbstractContainerScreen<FletchingTableBlockMenu> {
    public static final Identifier GUI_TEXTURE =
            Identifier.fromNamespaceAndPath(TheFletchingTableMod.MOD_ID, "textures/gui/fletching_table_gui.png");

    public FletchingTableBlockScreen(FletchingTableBlockMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        Minecraft client = Minecraft.getInstance();
        TextureAtlas atlas = client.getAtlasManager().getAtlasOrThrow(AtlasIds.ITEMS);

        context.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        TextureAtlasSprite arrowSprite = atlas.getSprite(Identifier.parse("item/arrow"));
        TextureAtlasSprite potionSprite = atlas.getSprite(Identifier.parse("item/potion"));
        TextureAtlasSprite splashPotionSprite = atlas.getSprite(Identifier.parse("item/splash_potion"));
        TextureAtlasSprite lingeringPotionSprite = atlas.getSprite(Identifier.parse("item/lingering_potion"));
        TextureAtlasSprite tippedArrowSpriteBase = atlas.getSprite(Identifier.parse("item/tipped_arrow_base"));
        TextureAtlasSprite tippedArrowSpriteHead = atlas.getSprite(Identifier.parse("item/tipped_arrow_head"));

        if(this.menu.getSlot(0).getItem().isEmpty()) {
            context.blitSprite(RenderPipelines.GUI_TEXTURED, arrowSprite, x + 25, y + 34, 16, 16);
        }

        long time = client.level != null ? client.level.getGameTime() : 0;

        int periodTicks = 20;
        int index = (int) ((time / periodTicks) % 3);

        TextureAtlasSprite current;
        switch (index) {
            case 0 -> current = potionSprite;
            case 1 -> current = splashPotionSprite;
            default -> current = lingeringPotionSprite;
        }

        if(this.menu.getSlot(1).getItem().isEmpty()) {
            context.blitSprite(RenderPipelines.GUI_TEXTURED, current, x + 78, y + 34, 16, 16);
        }

        if(this.menu.getSlot(2).getItem().isEmpty()) {
            context.blitSprite(RenderPipelines.GUI_TEXTURED, tippedArrowSpriteBase, x + 132, y + 34, 16, 16);
            context.blitSprite(RenderPipelines.GUI_TEXTURED, tippedArrowSpriteHead, x + 132, y + 34, 16, 16);
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }
}
