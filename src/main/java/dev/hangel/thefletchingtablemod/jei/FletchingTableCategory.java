package dev.hangel.thefletchingtablemod.jei;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class FletchingTableCategory implements IRecipeCategory<FletchingTableJEIRecipe> {
    public static final Identifier UUID = Identifier.fromNamespaceAndPath(TheFletchingTableMod.MOD_ID, "fletching_table");
    public static final Identifier GUI_TEXTURE =
            Identifier.fromNamespaceAndPath(TheFletchingTableMod.MOD_ID, "textures/gui/fletching_table_gui.png");

    public static final IRecipeType<FletchingTableJEIRecipe> FLETCHING_TABLE_RECIPE_TYPE =
            IRecipeType.create(UUID, FletchingTableJEIRecipe.class);

    private final IDrawable icon;

    public FletchingTableCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.FLETCHING_TABLE));
    }

    @Override
    public IRecipeType<FletchingTableJEIRecipe> getRecipeType() {
        return FLETCHING_TABLE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.minecraft.fletching_table");
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FletchingTableJEIRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 25, 34).add(recipe.arrowInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 78, 34).add(recipe.potionInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 132, 34).add(recipe.output());
    }
}