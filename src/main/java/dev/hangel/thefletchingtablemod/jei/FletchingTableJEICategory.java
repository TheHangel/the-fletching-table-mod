package dev.hangel.thefletchingtablemod.jei;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class FletchingTableJEICategory implements IRecipeCategory<FletchingTableJEIRecipe> {
    public static final IRecipeType<FletchingTableJEIRecipe> RECIPE_TYPE =
        IRecipeType.create(Identifier.of(TheFletchingTableMod.MOD_ID, "fletching_table"), FletchingTableJEIRecipe.class);

    private static final Identifier TEXTURE =
        Identifier.of(TheFletchingTableMod.MOD_ID, "textures/gui/fletching_table_gui.png");

    private final IDrawableStatic background;
    private final IDrawable icon;
    private final Text title;

    public FletchingTableJEICategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(TEXTURE, 0, 0, 175, 82)
            .setTextureSize(256, 256)
            .build();
        this.icon = guiHelper.createDrawableIngredient(
            VanillaTypes.ITEM_STACK,
            new ItemStack(Blocks.FLETCHING_TABLE)
        );
        this.title = Text.translatable("block.minecraft.fletching_table");
    }

    @Override
    public @NotNull IRecipeType<FletchingTableJEIRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Text getTitle() {
        return title;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return 175;
    }

    @Override
    public int getHeight() {
        return 82;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FletchingTableJEIRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addInputSlot(25, 34).add(recipe.arrowInput());
        builder.addInputSlot(78, 34).add(recipe.potionInput());
        builder.addOutputSlot(132, 34).add(recipe.output());
    }

    @Override
    public void draw(@NotNull FletchingTableJEIRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull DrawContext guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
    }
}
