package dev.hangel.thefletchingtablemod;

import dev.hangel.thefletchingtablemod.registry.ModRegistry;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class TheFletchingTableModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModRegistry.FLETCHING_TABLE_MENU, FletchingTableBlockScreen::new);
    }
}
