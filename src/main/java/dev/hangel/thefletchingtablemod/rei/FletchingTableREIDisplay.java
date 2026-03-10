package dev.hangel.thefletchingtablemod.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FletchingTableREIDisplay extends BasicDisplay {
    public FletchingTableREIDisplay(ItemStack arrowInput, ItemStack potionInput, ItemStack output) {
        super(
                List.of(
                        EntryIngredient.of(EntryStacks.of(arrowInput)),
                        EntryIngredient.of(EntryStacks.of(potionInput))
                ),
                List.of(
                        EntryIngredient.of(EntryStacks.of(output))
                )
        );
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return FletchingTableREICategory.CATEGORY_ID;
    }

    @Override
    public @Nullable DisplaySerializer<? extends FletchingTableREIDisplay> getSerializer() {
        return null;
    }
}