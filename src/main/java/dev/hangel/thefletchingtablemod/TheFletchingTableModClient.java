package dev.hangel.thefletchingtablemod;

import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class TheFletchingTableModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(TheFletchingTableMod.FLETCHING_TABLE_SCREEN_HANDLER, FletchingTableBlockScreen::new);
    }
}
