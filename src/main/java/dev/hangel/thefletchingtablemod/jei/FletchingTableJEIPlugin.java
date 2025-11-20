package dev.hangel.thefletchingtablemod.jei;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class FletchingTableJEIPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(TheFletchingTableMod.MOD_ID, "jei_fletching_table");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FletchingTableCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().getConnection()).getRecipeManager();

        List<FletchingTableRecipe> jeiRecipes = new java.util.ArrayList<>();

        List<RecipeHolder<FletchingTableRecipe>> holders =
                recipeManager.getAllRecipesFor(TheFletchingTableMod.FLETCHING_TABLE_RECIPE_TYPE.get());

        for (RecipeHolder<FletchingTableRecipe> holder : holders) {
            FletchingTableRecipe base = holder.value();

            Ingredient arrowIng  = base.arrowInput();
            Ingredient potionIng = base.potionInput();

            ItemStack[] potionBases = potionIng.getItems();

            boolean allowNormal    = java.util.Arrays.stream(potionBases).anyMatch(s -> s.is(Items.POTION));
            boolean allowSplash    = java.util.Arrays.stream(potionBases).anyMatch(s -> s.is(Items.SPLASH_POTION));
            boolean allowLingering = java.util.Arrays.stream(potionBases).anyMatch(s -> s.is(Items.LINGERING_POTION));

            if (!allowNormal && !allowSplash && !allowLingering) {
                jeiRecipes.add(base);
                continue;
            }

            for (Potion potion : BuiltInRegistries.POTION) {
                ResourceLocation id = BuiltInRegistries.POTION.getKey(potion);
                if (id != null) {
                    String path = id.getPath();
                    if (path.equals("water") || path.equals("awkward") ||
                            path.equals("thick") || path.equals("mundane")) {
                        continue;
                    }
                }

                ResourceLocation potionId = BuiltInRegistries.POTION.getKey(potion);
                if (potionId == null) continue;

                ResourceKey<Potion> key = ResourceKey.create(Registries.POTION, potionId);

                Holder.Reference<Potion> potionHolder = BuiltInRegistries.POTION.getHolder(key).orElse(null);

                if (potionHolder == null) continue;

                PotionContents contents = new PotionContents(
                    java.util.Optional.of(potionHolder),
                    java.util.Optional.empty(),
                    java.util.List.of()
                );

                if (allowNormal) {
                    ItemStack normal = new ItemStack(Items.POTION);
                    normal.set(DataComponents.POTION_CONTENTS, contents);

                    if (!normal.getHoverName().getString().equals(Component.translatable("item.minecraft.potion.effect.empty").getString())) {

                        ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                        out.set(DataComponents.POTION_CONTENTS, contents);

                        FletchingTableRecipe jeiRecipe = new FletchingTableRecipe(
                            arrowIng,
                            Ingredient.of(normal),
                            out
                        );
                        jeiRecipes.add(jeiRecipe);
                    }
                }

                if (allowSplash) {
                    ItemStack splash = new ItemStack(Items.SPLASH_POTION);
                    splash.set(DataComponents.POTION_CONTENTS, contents);

                    if (!splash.getHoverName().getString().equals(Component.translatable("item.minecraft.splash_potion.effect.empty").getString())) {

                        ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                        out.set(DataComponents.POTION_CONTENTS, contents);

                        FletchingTableRecipe jeiRecipe = new FletchingTableRecipe(
                            arrowIng,
                            Ingredient.of(splash),
                            out
                        );
                        jeiRecipes.add(jeiRecipe);
                    }
                }

                if (allowLingering) {
                    ItemStack lingering = new ItemStack(Items.LINGERING_POTION);
                    lingering.set(DataComponents.POTION_CONTENTS, contents);

                    if (!lingering.getHoverName().getString().equals(Component.translatable("item.minecraft.lingering_potion.effect.empty").getString())) {

                        ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                        out.set(DataComponents.POTION_CONTENTS, contents);

                        FletchingTableRecipe jeiRecipe = new FletchingTableRecipe(
                            arrowIng,
                            Ingredient.of(lingering),
                            out
                        );
                        jeiRecipes.add(jeiRecipe);
                    }
                }
            }
        }

        registration.addRecipes(FletchingTableCategory.FLETCHING_TABLE_RECIPE_TYPE, jeiRecipes);
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(FletchingTableBlockScreen.class, 100, 34, 25, 20, FletchingTableCategory.FLETCHING_TABLE_RECIPE_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Blocks.FLETCHING_TABLE.asItem()), FletchingTableCategory.FLETCHING_TABLE_RECIPE_TYPE);
    }
}
