package dev.hangel.thefletchingtablemod;

import dev.hangel.thefletchingtablemod.screen.FletchingTableScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class TheFletchingTableModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(TheFletchingTableMod.FLETCHING_TABLE_CONTAINER, FletchingTableScreen::new);
    }
}