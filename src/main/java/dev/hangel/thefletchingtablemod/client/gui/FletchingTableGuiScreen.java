package dev.hangel.thefletchingtablemod.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;

import dev.hangel.thefletchingtablemod.world.inventory.FletchingTableGuiMenu;

import com.mojang.blaze3d.systems.RenderSystem;

public class FletchingTableGuiScreen extends AbstractContainerScreen<FletchingTableGuiMenu> {
	private final static HashMap<String, Object> guistate = FletchingTableGuiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;

	public FletchingTableGuiScreen(FletchingTableGuiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	private static final ResourceLocation texture = new ResourceLocation("the_fletching_table_mod:textures/screens/fletching_table_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

		guiGraphics.blit(new ResourceLocation("the_fletching_table_mod:textures/screens/big_box.png"), this.leftPos + 127, this.topPos + 29, 0, 0, 26, 26, 26, 26);

		guiGraphics.blit(new ResourceLocation("the_fletching_table_mod:textures/screens/arrow_gui.png"), this.leftPos + 100, this.topPos + 35, 0, 0, 22, 15, 22, 15);

		guiGraphics.blit(new ResourceLocation("the_fletching_table_mod:textures/screens/plus.png"), this.leftPos + 53, this.topPos + 36, 0, 0, 13, 13, 13, 13);

		guiGraphics.blit(new ResourceLocation("the_fletching_table_mod:textures/screens/gui_arrow.png"), this.leftPos + 25, this.topPos + 34, 0, 0, 16, 16, 16, 16);

		guiGraphics.blit(new ResourceLocation("the_fletching_table_mod:textures/screens/gui_tipped_arrow.png"), this.leftPos + 132, this.topPos + 34, 0, 0, 16, 16, 16, 16);

		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.the_fletching_table_mod.fletching_table_gui.label_empty"), 8, 7, -1, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.the_fletching_table_mod.fletching_table_gui.label_inventory"), 8, 71, -1, false);
	}

	@Override
	public void onClose() {
		super.onClose();
	}

	@Override
	public void init() {
		super.init();
	}
}
