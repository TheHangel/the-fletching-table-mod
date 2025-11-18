package com.example.examplemod.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record FletchingTableRecipeInput(ItemStack arrowInput, ItemStack potionInput) implements RecipeInput {

    @Override
    public @NotNull ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> arrowInput;
            case 1 -> potionInput;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
