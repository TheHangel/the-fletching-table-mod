package dev.hangel.thefletchingtablemod.screen;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;

public class FletchingTableBlockScreen extends AbstractContainerScreen<FletchingTableBlockMenu> {
    public static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(TheFletchingTableMod.MOD_ID, "textures/gui/fletching_table_gui.png");

    public FletchingTableBlockScreen(FletchingTableBlockMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        Minecraft client = Minecraft.getInstance();
        TextureAtlas atlas  = client.getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);

        context.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        RenderSystem.setShaderColor(0.0f, 0.0f, 0.0f, 1.0f);

        TextureAtlasSprite arrowSprite = atlas.getSprite(ResourceLocation.parse("item/arrow"));

        TextureAtlasSprite potionSprite = atlas.getSprite(ResourceLocation.parse("item/potion"));

        TextureAtlasSprite splashPotionSprite = atlas.getSprite(ResourceLocation.parse("item/splash_potion"));

        TextureAtlasSprite lingeringPotionSprite = atlas.getSprite(ResourceLocation.parse("item/lingering_potion"));

        TextureAtlasSprite tippedArrowSpriteBase = atlas.getSprite(ResourceLocation.parse("item/tipped_arrow_base"));

        TextureAtlasSprite tippedArrowSpriteHead = atlas.getSprite(ResourceLocation.parse("item/tipped_arrow_head"));

        if(this.menu.getSlot(0).getItem().isEmpty()) {
            context.blit(x + 25, y + 34, 0, 16, 16, arrowSprite);
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
            context.blit(x + 78, y + 34, 0, 16, 16, current);
        }

        if(this.menu.getSlot(2).getItem().isEmpty()) {
            context.blit(x + 132, y + 34, 0, 16, 16, tippedArrowSpriteBase);
            context.blit(x + 132, y + 34, 0, 16, 16, tippedArrowSpriteHead);
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    @Override
    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }
}