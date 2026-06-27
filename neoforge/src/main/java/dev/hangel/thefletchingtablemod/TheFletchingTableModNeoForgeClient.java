package dev.hangel.thefletchingtablemod;

import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class TheFletchingTableModNeoForgeClient {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(
                TheFletchingTableModNeoForge.FLETCHING_TABLE_MENU.get(),
                FletchingTableBlockScreen::new);
    }
}
