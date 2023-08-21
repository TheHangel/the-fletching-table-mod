
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package dev.hangel.thefletchingtablemod.init;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.gui.screens.MenuScreens;

import dev.hangel.thefletchingtablemod.client.gui.FletchingTableGuiScreen;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TheFletchingTableModModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(TheFletchingTableModModMenus.FLETCHING_TABLE_GUI.get(), FletchingTableGuiScreen::new);
		});
	}
}
