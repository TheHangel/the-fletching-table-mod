package dev.hangel.thefletchingtablemod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import dev.hangel.thefletchingtablemod.container.FletchingTableContainer;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FletchingTableScreen extends ContainerScreen<FletchingTableContainer> {

    private static final ResourceLocation GUI_TEXTURE =
            new ResourceLocation(TheFletchingTableMod.MOD_ID, "textures/gui/fletching_table_gui.png");

    public FletchingTableScreen(FletchingTableContainer screenContainer, PlayerInventory inv, ITextComponent title) {
        super(screenContainer, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bind(GUI_TEXTURE);
        blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int x0 = this.leftPos + 25;
        int y0 = this.topPos + 34;

        int x1 = this.leftPos + 78;
        int y1 = this.topPos + 34;

        int x2 = this.leftPos + 132;
        int y2 = this.topPos + 34;

        this.minecraft.getTextureManager().bind(AtlasTexture.LOCATION_BLOCKS);

        TextureAtlasSprite arrowSprite = this.minecraft.getModelManager()
                .getAtlas(AtlasTexture.LOCATION_BLOCKS)
                .getSprite(new ResourceLocation("minecraft", "item/arrow"));

        TextureAtlasSprite potionSprite = this.minecraft.getModelManager()
                .getAtlas(AtlasTexture.LOCATION_BLOCKS)
                .getSprite(new ResourceLocation("minecraft", "item/potion"));

        TextureAtlasSprite splashPotionSprite = this.minecraft.getModelManager()
                .getAtlas(AtlasTexture.LOCATION_BLOCKS)
                .getSprite(new ResourceLocation("minecraft", "item/splash_potion"));

        TextureAtlasSprite lingeringPotionSprite = this.minecraft.getModelManager()
                .getAtlas(AtlasTexture.LOCATION_BLOCKS)
                .getSprite(new ResourceLocation("minecraft", "item/lingering_potion"));

        TextureAtlasSprite tippedArrowSpriteBase = this.minecraft.getModelManager()
                .getAtlas(AtlasTexture.LOCATION_BLOCKS)
                .getSprite(new ResourceLocation("minecraft", "item/tipped_arrow_base"));

        TextureAtlasSprite tippedArrowSpriteHead = this.minecraft.getModelManager()
                .getAtlas(AtlasTexture.LOCATION_BLOCKS)
                .getSprite(new ResourceLocation("minecraft", "item/tipped_arrow_head"));

        if (!this.menu.getSlot(0).hasItem()) {
            RenderSystem.color4f(0f, 0f, 0f, 0.35f);
            AbstractGui.blit(ms, x0, y0, this.getBlitOffset(), 16, 16, arrowSprite);
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
            AbstractGui.blit(ms, x1, y1, this.getBlitOffset(), 16, 16, sprite);
            RenderSystem.color4f(1f, 1f, 1f, 1f);
        }

        if (!this.menu.getSlot(2).hasItem()) {
            RenderSystem.color4f(0f, 0f, 0f, 0.35f);
            AbstractGui.blit(ms, x2, y2, this.getBlitOffset(), 16, 16, tippedArrowSpriteBase);
            AbstractGui.blit(ms, x2, y2, this.getBlitOffset(), 16, 16, tippedArrowSpriteHead);
            RenderSystem.color4f(1f, 1f, 1f, 1f);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
}