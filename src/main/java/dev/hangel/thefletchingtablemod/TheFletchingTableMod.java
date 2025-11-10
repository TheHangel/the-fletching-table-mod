package dev.hangel.thefletchingtablemod;

import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheFletchingTableMod implements ModInitializer {
	public static final String MOD_ID = "the_fletching_table_mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ScreenHandlerType<FletchingTableBlockScreenHandler> FLETCHING_TABLE_SCREEN_HANDLER =
			Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MOD_ID, "fletching_table_block_screen_handler"),
					new ExtendedScreenHandlerType<>(FletchingTableBlockScreenHandler::new, BlockPos.PACKET_CODEC)
			);

	@Override
	public void onInitialize() {
	}
}