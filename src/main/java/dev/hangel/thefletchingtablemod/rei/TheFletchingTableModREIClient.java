package dev.hangel.thefletchingtablemod.rei;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreen;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Blocks;

public class TheFletchingTableModREIClient implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FletchingTableCategory());

        registry.addWorkstations(FletchingTableCategory.FLETCHING_TABLE_DISPLAY_CATEGORY, EntryStacks.of(Blocks.FLETCHING_TABLE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(FletchingTableRecipe.class, TheFletchingTableMod.FLETCHING_TABLE_RECIPE_TYPE, FletchingTableDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen ->
            new Rectangle(((screen.width - 176) / 2) + 100, ((screen.height - 166) / 2) + 30, 20, 25),
            FletchingTableBlockScreen.class,
            FletchingTableCategory.FLETCHING_TABLE_DISPLAY_CATEGORY
        );
    }
}
