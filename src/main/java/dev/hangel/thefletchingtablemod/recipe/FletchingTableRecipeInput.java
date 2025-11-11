package dev.hangel.thefletchingtablemod.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record FletchingTableRecipeInput(ItemStack arrowInput, ItemStack potionInput) implements RecipeInput {
    @Override
    public ItemStack getStackInSlot(int slot) {
        return switch (slot) {
            case 0 -> arrowInput;
            case 1 -> potionInput;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int getSize() {
        return 2;
    }
}
