package dev.hangel.thefletchingtablemod.registry;

import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Holds the registered game objects shared by the common code. The actual registration happens in the
 * loader specific entry points (Fabric / NeoForge) which assign these fields. Common code only reads them.
 */
public final class ModRegistry {

    public static MenuType<FletchingTableBlockMenu> FLETCHING_TABLE_MENU;
    public static RecipeSerializer<FletchingTableRecipe> FLETCHING_TABLE_RECIPE_SERIALIZER;
    public static RecipeType<FletchingTableRecipe> FLETCHING_TABLE_RECIPE_TYPE;
    public static RecipeBookCategory FLETCHING_TABLE_RECIPE_BOOK_CATEGORY;

    private ModRegistry() {
    }
}
