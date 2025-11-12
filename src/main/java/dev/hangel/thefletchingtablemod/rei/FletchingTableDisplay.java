package dev.hangel.thefletchingtablemod.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import java.util.List;

public class FletchingTableDisplay extends BasicDisplay {
    public FletchingTableDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return FletchingTableCategory.FLETCHING_TABLE_DISPLAY_CATEGORY;
    }
}
