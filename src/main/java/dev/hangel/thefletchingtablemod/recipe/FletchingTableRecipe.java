package dev.hangel.thefletchingtablemod.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public record FletchingTableRecipe(Ingredient arrowInput, Ingredient potionInput, ItemStack output) implements Recipe<FletchingTableRecipeInput> {

    @Override
    public boolean matches(FletchingTableRecipeInput input, World world) {
        return arrowInput.test(input.getStackInSlot(0)) && potionInput.test(input.getStackInSlot(1));
    }

    @Override
    public ItemStack craft(FletchingTableRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<FletchingTableRecipeInput>> getSerializer() {
        return TheFletchingTableMod.FLETCHING_TABLE_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<FletchingTableRecipeInput>> getType() {
        return TheFletchingTableMod.FLETCHING_TABLE_RECIPE_TYPE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.NONE;
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return TheFletchingTableMod.FLETCHING_TABLE_RECIPE_BOOK_CATEGORY;
    }

    public static class Serializer implements RecipeSerializer<FletchingTableRecipe> {
        public static final MapCodec<FletchingTableRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("arrow").forGetter(FletchingTableRecipe::arrowInput),
            Ingredient.CODEC.fieldOf("potion").forGetter(FletchingTableRecipe::potionInput),
            ItemStack.CODEC.fieldOf("result").forGetter(FletchingTableRecipe::output)
        ).apply(inst, FletchingTableRecipe::new));

        public static final PacketCodec<RegistryByteBuf, FletchingTableRecipe> STREAM_CODEC =
            PacketCodec.tuple(
                Ingredient.PACKET_CODEC, FletchingTableRecipe::arrowInput,
                Ingredient.PACKET_CODEC, FletchingTableRecipe::potionInput,
                ItemStack.PACKET_CODEC, FletchingTableRecipe::output,
                FletchingTableRecipe::new
            );

        @Override
        public MapCodec<FletchingTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, FletchingTableRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}
