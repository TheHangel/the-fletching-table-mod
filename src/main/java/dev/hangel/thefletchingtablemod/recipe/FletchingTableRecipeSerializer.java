package dev.hangel.thefletchingtablemod.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class FletchingTableRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
        implements IRecipeSerializer<FletchingTableRecipe> {

    @Override
    public FletchingTableRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String group = JSONUtils.getAsString(json, "group", "");

        Ingredient in1 = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "arrow"));
        Ingredient in2 = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "potion"));

        JsonObject resultObj = JSONUtils.getAsJsonObject(json, "result");
        ItemStack result = net.minecraft.item.crafting.ShapedRecipe.itemFromJson(resultObj);

        int count = JSONUtils.getAsInt(resultObj, "count", 0);
        if (count > 0) result.setCount(count);

        return new FletchingTableRecipe(recipeId, group, in1, in2, result, count);
    }

    @Nullable
    @Override
    public FletchingTableRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        String group = buffer.readUtf(32767);
        Ingredient in1 = Ingredient.fromNetwork(buffer);
        Ingredient in2 = Ingredient.fromNetwork(buffer);
        ItemStack result = buffer.readItem();
        int count = buffer.readVarInt();

        return new FletchingTableRecipe(recipeId, group, in1, in2, result, count);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, FletchingTableRecipe recipe) {
        buffer.writeUtf(recipe.getGroup());
        recipe.getArrowInput().toNetwork(buffer);
        recipe.getPotionInput().toNetwork(buffer);

        buffer.writeItem(recipe.getResultItem());
        buffer.writeVarInt(recipe.getResultCountFromJson());
    }
}