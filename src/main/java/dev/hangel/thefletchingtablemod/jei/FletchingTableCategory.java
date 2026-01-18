package dev.hangel.thefletchingtablemod.jei;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.Blocks;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Arrays;

public class FletchingTableCategory implements IRecipeCategory<FletchingTableRecipe> {

    public static final ResourceLocation UID =
            new ResourceLocation(TheFletchingTableMod.MOD_ID, "fletching_table");

    public static final ResourceLocation GUI_TEXTURE =
            new ResourceLocation(TheFletchingTableMod.MOD_ID, "textures/gui/fletching_table_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public FletchingTableCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(GUI_TEXTURE, 0, 0, 176, 80);
        this.icon = helper.createDrawableIngredient(new ItemStack(Blocks.FLETCHING_TABLE));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends FletchingTableRecipe> getRecipeClass() {
        return FletchingTableRecipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent("block.minecraft.fletching_table").getString();
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(FletchingTableRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Arrays.asList(
                recipe.getArrowInput(),
                recipe.getPotionInput()
        ));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FletchingTableRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();

        stacks.init(0, true, 25, 34);
        stacks.init(1, true, 78, 34);
        stacks.init(2, false, 132, 34);

        stacks.set(ingredients);
    }
}