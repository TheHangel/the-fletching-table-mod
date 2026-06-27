package dev.hangel.thefletchingtablemod;

import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.registry.ModRegistry;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockMenu;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeType;

public class TheFletchingTableModFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Identifier id = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "fletching_table");

        ModRegistry.FLETCHING_TABLE_RECIPE_SERIALIZER = Registry.register(
                BuiltInRegistries.RECIPE_SERIALIZER, id, FletchingTableRecipe.SERIALIZER);

        ModRegistry.FLETCHING_TABLE_RECIPE_TYPE = Registry.register(
                BuiltInRegistries.RECIPE_TYPE, id, new RecipeType<FletchingTableRecipe>() {
                    @Override
                    public String toString() {
                        return "fletching_table";
                    }
                });

        ModRegistry.FLETCHING_TABLE_RECIPE_BOOK_CATEGORY = Registry.register(
                BuiltInRegistries.RECIPE_BOOK_CATEGORY, id, new RecipeBookCategory());

        ModRegistry.FLETCHING_TABLE_MENU = Registry.register(
                BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "fletching_table_block_menu"),
                new MenuType<>(
                        (syncId, inventory) -> new FletchingTableBlockMenu(syncId, inventory, BlockPos.ZERO),
                        FeatureFlags.VANILLA_SET));

        Constants.LOG.info("{} initialised on Fabric", Constants.MOD_NAME);
    }
}
