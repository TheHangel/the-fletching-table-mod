package dev.hangel.thefletchingtablemod.recipe;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class FletchingTableRecipe implements IRecipe<IInventory> {
    public static final IRecipeType<FletchingTableRecipe> TYPE =
            IRecipeType.register(TheFletchingTableMod.MOD_ID + ":fletching_table");

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
    public boolean matches(IInventory inv, World worldIn) {
        return arrowInput.test(inv.getItem(0)) && potionInput.test(inv.getItem(1));
    }

    @Override
    public ItemStack assemble(IInventory inv) {
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
    public IRecipeSerializer<?> getSerializer() {
        return TheFletchingTableMod.FLETCHING_TABLE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return TYPE;
    }

    public String getGroup() { return group; }
    public Ingredient getArrowInput() { return arrowInput; }
    public Ingredient getPotionInput() { return potionInput; }
    public int getResultCountFromJson() { return resultCountFromJson; }
}