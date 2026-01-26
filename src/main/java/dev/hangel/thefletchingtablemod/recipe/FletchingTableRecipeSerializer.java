package dev.hangel.thefletchingtablemod.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

public class FletchingTableRecipeSerializer implements RecipeSerializer<FletchingTableRecipe> {

    @Override
    public FletchingTableRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String group = GsonHelper.getAsString(json, "group", "");

        Ingredient in1 = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "arrow"));
        Ingredient in2 = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "potion"));

        JsonObject resultObj = GsonHelper.getAsJsonObject(json, "result");
        ItemStack result = ShapedRecipe.itemFromJson(resultObj);

        int count = GsonHelper.getAsInt(resultObj, "count", 0);
        if (count > 0) result.setCount(count);

        return new FletchingTableRecipe(recipeId, group, in1, in2, result, count);
    }

    @Nullable
    @Override
    public FletchingTableRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        String group = buffer.readUtf(32767);
        Ingredient in1 = Ingredient.fromNetwork(buffer);
        Ingredient in2 = Ingredient.fromNetwork(buffer);
        ItemStack result = buffer.readItem();
        int count = buffer.readVarInt();

        return new FletchingTableRecipe(recipeId, group, in1, in2, result, count);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, FletchingTableRecipe recipe) {
        buffer.writeUtf(recipe.getGroup());
        recipe.getArrowInput().toNetwork(buffer);
        recipe.getPotionInput().toNetwork(buffer);

        buffer.writeItem(recipe.getResultItem());
        buffer.writeVarInt(recipe.getResultCountFromJson());
    }
}