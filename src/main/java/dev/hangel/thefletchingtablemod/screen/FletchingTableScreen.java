package dev.hangel.thefletchingtablemod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import dev.hangel.thefletchingtablemod.container.FletchingTableContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class FletchingTableScreen extends AbstractContainerScreen<FletchingTableContainer> {

    private static final ResourceLocation GUI_TEXTURE =
            new ResourceLocation(TheFletchingTableMod.MOD_ID, "textures/gui/fletching_table_gui.png");

    public FletchingTableScreen(FletchingTableContainer screenContainer, Inventory inv, Component title) {
        super(screenContainer, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bind(GUI_TEXTURE);
        blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int x0 = this.leftPos + 25;
        int y0 = this.topPos + 34;

        int x1 = this.leftPos + 78;
        int y1 = this.topPos + 34;

        int x2 = this.leftPos + 132;
        int y2 = this.topPos + 34;

        this.minecraft.getTextureManager().bind(TextureAtlas.LOCATION_BLOCKS);

        TextureAtlasSprite arrowSprite = this.minecraft.getModelManager()
                .getAtlas(TextureAtlas.LOCATION_BLOCKS)
                .getSprite(new ResourceLocation("minecraft", "item/arrow"));

        TextureAtlasSprite potionSprite = this.minecraft.getModelManager()
                .getAtlas(TextureAtlas.LOCATION_BLOCKS)
                .getSprite(new ResourceLocation("minecraft", "item/potion"));

        TextureAtlasSprite splashPotionSprite = this.minecraft.getModelManager()
                .getAtlas(TextureAtlas.LOCATION_BLOCKS)
                .getSprite(new ResourceLocation("minecraft", "item/splash_potion"));

        TextureAtlasSprite lingeringPotionSprite = this.minecraft.getModelManager()
                .getAtlas(TextureAtlas.LOCATION_BLOCKS)
                .getSprite(new ResourceLocation("minecraft", "item/lingering_potion"));

        TextureAtlasSprite tippedArrowSpriteBase = this.minecraft.getModelManager()
                .getAtlas(TextureAtlas.LOCATION_BLOCKS)
                .getSprite(new ResourceLocation("minecraft", "item/tipped_arrow_base"));

        TextureAtlasSprite tippedArrowSpriteHead = this.minecraft.getModelManager()
                .getAtlas(TextureAtlas.LOCATION_BLOCKS)
                .getSprite(new ResourceLocation("minecraft", "item/tipped_arrow_head"));

        if (!this.menu.getSlot(0).hasItem()) {
            RenderSystem.color4f(0f, 0f, 0f, 0.35f);
            GuiComponent.blit(ms, x0, y0, this.getBlitOffset(), 16, 16, arrowSprite);
            RenderSystem.color4f(1f, 1f, 1f, 1f);
        }

        if (!this.menu.getSlot(1).hasItem()) {
            long time = this.minecraft.level != null ? this.minecraft.level.getGameTime() : 0L;
            int periodTicks = 20;
            int index = (int)((time / periodTicks) % 3);

            TextureAtlasSprite sprite;
            if      (index == 0) sprite = potionSprite;
            else if (index == 1) sprite = splashPotionSprite;
            else                 sprite = lingeringPotionSprite;

            RenderSystem.color4f(0f, 0f, 0f, 0.35f);
            GuiComponent.blit(ms, x1, y1, this.getBlitOffset(), 16, 16, sprite);
            RenderSystem.color4f(1f, 1f, 1f, 1f);
        }

        if (!this.menu.getSlot(2).hasItem()) {
            RenderSystem.color4f(0f, 0f, 0f, 0.35f);
            GuiComponent.blit(ms, x2, y2, this.getBlitOffset(), 16, 16, tippedArrowSpriteBase);
            GuiComponent.blit(ms, x2, y2, this.getBlitOffset(), 16, 16, tippedArrowSpriteHead);
            RenderSystem.color4f(1f, 1f, 1f, 1f);
        }
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
}