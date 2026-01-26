package dev.hangel.thefletchingtablemod;

import dev.hangel.thefletchingtablemod.container.FletchingTableContainer;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipeSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TheFletchingTableMod implements ModInitializer {
	public static final String MOD_ID = "the_fletching_table_mod";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static MenuType<FletchingTableContainer> FLETCHING_TABLE_CONTAINER;

	public static RecipeSerializer<FletchingTableRecipe> FLETCHING_TABLE_SERIALIZER;

	@Override
	public void onInitialize() {
		FLETCHING_TABLE_CONTAINER = ScreenHandlerRegistry.registerExtended(
				id("fletching_table"),
				(syncId, playerInv, buf) -> {
					BlockPos pos = buf.readBlockPos();
					Player player = playerInv.player;
					Level world = player.level;
					return new FletchingTableContainer(syncId, world, pos, playerInv, player);
				}
		);

		FLETCHING_TABLE_SERIALIZER = Registry.register(
				Registry.RECIPE_SERIALIZER,
				id("fletching_table"),
				new FletchingTableRecipeSerializer()
		);
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}