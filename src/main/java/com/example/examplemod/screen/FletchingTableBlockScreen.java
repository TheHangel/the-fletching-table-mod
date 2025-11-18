package com.example.examplemod.screen;

import com.example.examplemod.ExampleMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FletchingTableBlockScreen extends AbstractContainerScreen<FletchingTableBlockMenu> {
    public static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ExampleMod.MOD_ID, "textures/gui/fletching_table_gui.png");

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

        //MinecraftClient client = MinecraftClient.getInstance();
        //SpriteAtlasTexture atlas  = client.getBakedModelManager().getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);

        context.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        /*RenderSystem.setShaderColor(0.0f, 0.0f, 0.0f, 1.0f);

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

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);*/
    }

    /*@Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }*/
}