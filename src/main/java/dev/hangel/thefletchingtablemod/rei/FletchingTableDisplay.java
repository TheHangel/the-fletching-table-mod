package dev.hangel.thefletchingtablemod.rei;

import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeEntry;

import java.util.List;

public class FletchingTableDisplay extends BasicDisplay {
    public FletchingTableDisplay(RecipeEntry<FletchingTableRecipe> recipe) {
        super(List.of(), List.of());

        FletchingTableRecipe r = recipe.value();

        EntryIngredient inArrow = EntryIngredients.ofIngredient(r.getIngredients().get(0));

        ItemStack out = r.output().copy();
        PotionContentsComponent comp = out.get(DataComponentTypes.POTION_CONTENTS);

        ItemStack inPotion = new ItemStack(Items.POTION);
        if (comp != null) inPotion.set(DataComponentTypes.POTION_CONTENTS, comp);

        EntryIngredient inPotionEntry = EntryIngredient.of(EntryStacks.of(inPotion));

        EntryIngredient outEntry = EntryIngredient.of(EntryStacks.of(out));

        super.inputs  = List.of(inArrow, inPotionEntry);
        super.outputs = List.of(outEntry);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return FletchingTableCategory.FLETCHING_TABLE_DISPLAY_CATEGORY;
    }
}
