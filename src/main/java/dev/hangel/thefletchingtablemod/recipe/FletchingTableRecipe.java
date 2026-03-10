package dev.hangel.thefletchingtablemod.recipe;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FletchingTableRecipe implements Recipe<@NotNull FletchingTableRecipeInput> {

    private final Ingredient arrowInput;
    private final Ingredient potionInput;
    private final ItemStack output;
    private PlacementInfo info;

    public FletchingTableRecipe(Ingredient arrowInput, Ingredient potionInput, ItemStack output) {
        this.arrowInput = arrowInput;
        this.potionInput = potionInput;
        this.output = output;
    }

    public Ingredient arrowInput() {
        return arrowInput;
    }

    public Ingredient potionInput() {
        return potionInput;
    }

    public ItemStack output() {
        return output;
    }

    @Override
    public boolean matches(FletchingTableRecipeInput input, @NotNull Level level) {
        return arrowInput.test(input.getItem(0))
                && potionInput.test(input.getItem(1));
    }

    @Override
    public @NotNull ItemStack assemble(FletchingTableRecipeInput input, HolderLookup.@NotNull Provider lookup) {
        return output.copy();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        if (this.info == null) {
            this.info = PlacementInfo.create(List.of(arrowInput, potionInput));
        }
        return this.info;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return TheFletchingTableMod.FLETCHING_TABLE_RECIPE_BOOK_CATEGORY.get();
    }

    @Override
    public @NotNull RecipeSerializer<@NotNull FletchingTableRecipe> getSerializer() {
        return TheFletchingTableMod.FLETCHING_TABLE_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<@NotNull FletchingTableRecipe> getType() {
        return TheFletchingTableMod.FLETCHING_TABLE_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<@NotNull FletchingTableRecipe> {
        public static final MapCodec<FletchingTableRecipe> CODEC =
                RecordCodecBuilder.mapCodec(inst -> inst.group(
                        Ingredient.CODEC.fieldOf("arrow").forGetter(FletchingTableRecipe::arrowInput),
                        Ingredient.CODEC.fieldOf("potion").forGetter(FletchingTableRecipe::potionInput),
                        ItemStack.CODEC.fieldOf("result").forGetter(FletchingTableRecipe::output)
                ).apply(inst, FletchingTableRecipe::new));

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
