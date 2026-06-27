package dev.hangel.thefletchingtablemod.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.hangel.thefletchingtablemod.registry.ModRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
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

    public static final MapCodec<FletchingTableRecipe> CODEC =
            RecordCodecBuilder.mapCodec(inst -> inst.group(
                    Ingredient.CODEC.fieldOf("arrow").forGetter(FletchingTableRecipe::arrowInput),
                    Ingredient.CODEC.fieldOf("potion").forGetter(FletchingTableRecipe::potionInput),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(FletchingTableRecipe::output)
            ).apply(inst, FletchingTableRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FletchingTableRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, FletchingTableRecipe::arrowInput,
                    Ingredient.CONTENTS_STREAM_CODEC, FletchingTableRecipe::potionInput,
                    ItemStackTemplate.STREAM_CODEC, FletchingTableRecipe::output,
                    FletchingTableRecipe::new
            );

    public static final RecipeSerializer<FletchingTableRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private final Ingredient arrowInput;
    private final Ingredient potionInput;
    private final ItemStackTemplate output;
    private PlacementInfo info;

    public FletchingTableRecipe(Ingredient arrowInput, Ingredient potionInput, ItemStackTemplate output) {
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

    public ItemStackTemplate output() {
        return output;
    }

    @Override
    public boolean matches(FletchingTableRecipeInput input, @NotNull Level level) {
        return arrowInput.test(input.getItem(0))
                && potionInput.test(input.getItem(1));
    }

    @Override
    public @NotNull ItemStack assemble(FletchingTableRecipeInput input) {
        return output.create();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public @NotNull String group() {
        return "";
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
        return ModRegistry.FLETCHING_TABLE_RECIPE_BOOK_CATEGORY;
    }

    @Override
    public @NotNull RecipeSerializer<FletchingTableRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<@NotNull FletchingTableRecipe> getType() {
        return ModRegistry.FLETCHING_TABLE_RECIPE_TYPE;
    }
}
