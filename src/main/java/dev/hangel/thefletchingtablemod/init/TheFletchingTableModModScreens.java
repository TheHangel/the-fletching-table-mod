
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package dev.hangel.thefletchingtablemod.init;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import dev.hangel.thefletchingtablemod.client.gui.FletchingTableGuiScreen;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TheFletchingTableModModScreens {
	@SubscribeEvent
	public static void clientLoad(RegisterMenuScreensEvent event) {
		event.register(TheFletchingTableModModMenus.FLETCHING_TABLE_GUI.get(), FletchingTableGuiScreen::new);
	}
}
