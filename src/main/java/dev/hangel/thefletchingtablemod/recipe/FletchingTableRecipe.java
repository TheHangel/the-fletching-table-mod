package dev.hangel.thefletchingtablemod.recipe;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class FletchingTableRecipe implements Recipe<Container> {
    public static final RecipeType<FletchingTableRecipe> TYPE =
            RecipeType.register(TheFletchingTableMod.MOD_ID + ":fletching_table");

    private final ResourceLocation id;
    private final String group;
    private final Ingredient arrowInput;
    private final Ingredient potionInput;
    private final ItemStack result;

    private final int resultCountFromJson;

    public FletchingTableRecipe(ResourceLocation id, String group,
                                Ingredient in1, Ingredient in2,
                                ItemStack result, int resultCountFromJson) {
        this.id = id;
        this.group = group;
        this.arrowInput = in1;
        this.potionInput = in2;
        this.result = result;
        this.resultCountFromJson = resultCountFromJson;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(arrowInput);
        list.add(potionInput);
        return list;
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return arrowInput.test(inv.getItem(0)) && potionInput.test(inv.getItem(1));
    }

    @Override
    public ItemStack assemble(Container inv) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TheFletchingTableMod.FLETCHING_TABLE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    public String getGroup() { return group; }
    public Ingredient getArrowInput() { return arrowInput; }
    public Ingredient getPotionInput() { return potionInput; }
    public int getResultCountFromJson() { return resultCountFromJson; }
}