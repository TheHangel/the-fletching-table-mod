package dev.hangel.thefletchingtablemod;

import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = TheFletchingTableMod.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = TheFletchingTableMod.MOD_ID, value = Dist.CLIENT)
public class TheFletchingTableModClient {
    public TheFletchingTableModClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(
            TheFletchingTableMod.FLETCHING_TABLE_MENU.get(),
            FletchingTableBlockScreen::new
        );
    }
}
