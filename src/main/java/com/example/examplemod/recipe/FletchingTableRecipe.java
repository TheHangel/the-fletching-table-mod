package com.example.examplemod.recipe;

import com.example.examplemod.ExampleMod;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record FletchingTableRecipe(Ingredient arrowInput, Ingredient potionInput, ItemStack output)
        implements Recipe<FletchingTableRecipeInput> {

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(arrowInput);
        list.add(potionInput);
        return list;
    }

    @Override
    public boolean matches(FletchingTableRecipeInput input, Level level) {
        return arrowInput.test(input.getItem(0))
                && potionInput.test(input.getItem(1));
    }

    @Override
    public ItemStack assemble(FletchingTableRecipeInput input, HolderLookup.Provider lookup) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        // Tu peux mettre ce que tu veux, c'est rarement utilisé pour des recettes “non crafting grid”
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registriesLookup) {
        return output;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        // En Forge ça sera un RegistryObject, donc `.get()`
        return ExampleMod.FLETCHING_TABLE_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ExampleMod.FLETCHING_TABLE_RECIPE_TYPE.get();
    }

    // -------- Serializer Forge 1.21.1 --------
    public static class Serializer implements RecipeSerializer<FletchingTableRecipe> {

        // JSON (data pack) -> objet
        public static final MapCodec<FletchingTableRecipe> CODEC =
                RecordCodecBuilder.mapCodec(inst -> inst.group(
                        // si DISALLOW_EMPTY_CODEC existe dans tes mappings, tu peux l’utiliser
                        Ingredient.CODEC.fieldOf("arrow").forGetter(FletchingTableRecipe::arrowInput),
                        Ingredient.CODEC.fieldOf("potion").forGetter(FletchingTableRecipe::potionInput),
                        ItemStack.CODEC.fieldOf("result").forGetter(FletchingTableRecipe::output)
                ).apply(inst, FletchingTableRecipe::new));

        // Network (sync recettes serveur -> client)
        public static final StreamCodec<RegistryFriendlyByteBuf, FletchingTableRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, FletchingTableRecipe::arrowInput,
                        Ingredient.CONTENTS_STREAM_CODEC, FletchingTableRecipe::potionInput,
                        ItemStack.STREAM_CODEC, FletchingTableRecipe::output,
                        FletchingTableRecipe::new
                );

        @Override
        public @NotNull MapCodec<FletchingTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, FletchingTableRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
