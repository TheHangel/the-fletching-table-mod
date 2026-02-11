package dev.hangel.thefletchingtablemod;

import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
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

	public static final RecipeSerializer<FletchingTableRecipe> FLETCHING_TABLE_RECIPE_SERIALIZER = Registry.register(
		Registries.RECIPE_SERIALIZER, Identifier.of(MOD_ID, "fletching_table"), new FletchingTableRecipe.Serializer()
	);

	public static final RecipeType<FletchingTableRecipe> FLETCHING_TABLE_RECIPE_TYPE = Registry.register(
		Registries.RECIPE_TYPE, Identifier.of(MOD_ID, "fletching_table"), new RecipeType<FletchingTableRecipe>() {
			@Override
			public String toString() {
				return "fletching_table";
			}
		}
	);

	public static final RecipeBookCategory FLETCHING_TABLE_RECIPE_BOOK_CATEGORY = Registry.register(
		Registries.RECIPE_BOOK_CATEGORY, Identifier.of(MOD_ID, "fletching_table"), new RecipeBookCategory()
	);

	@Override
	public void onInitialize() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			BlockPos pos = hitResult.getBlockPos();
			if (!world.getBlockState(pos).isOf(Blocks.FLETCHING_TABLE)) {
				return ActionResult.PASS;
			}

			if (world.isClient()) {
				return ActionResult.SUCCESS;
			}

			if (player instanceof ServerPlayerEntity serverPlayer) {
				serverPlayer.openHandledScreen(new ExtendedScreenHandlerFactory<BlockPos>() {
					@Override
					public BlockPos getScreenOpeningData(ServerPlayerEntity sp) {
						return pos;
					}

					@Override
					public Text getDisplayName() {
						return Text.translatable("block.minecraft.fletching_table");
					}

					@Override
					public FletchingTableBlockScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity p) {
						return new FletchingTableBlockScreenHandler(syncId, inv, pos);
					}
				});
				return ActionResult.CONSUME;
			}

			return ActionResult.PASS;
		});
	}
}