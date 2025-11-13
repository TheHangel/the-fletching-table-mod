package dev.hangel.thefletchingtablemod.rei;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreen;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TheFletchingTableModREIClient implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FletchingTableCategory());

        registry.addWorkstations(FletchingTableCategory.FLETCHING_TABLE_DISPLAY_CATEGORY, EntryStacks.of(Blocks.FLETCHING_TABLE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(
            FletchingTableRecipe.class,
            TheFletchingTableMod.FLETCHING_TABLE_RECIPE_TYPE,
            recipeEntry -> {
                FletchingTableRecipe recipe = recipeEntry.value();

                Ingredient arrowIng  = recipe.arrowInput();
                Ingredient potionIng = recipe.potionInput();

                ItemStack[] potionBases = potionIng.getMatchingStacks();
                boolean allowNormal    = Arrays.stream(potionBases).anyMatch(s -> s.isOf(Items.POTION));
                boolean allowSplash    = Arrays.stream(potionBases).anyMatch(s -> s.isOf(Items.SPLASH_POTION));
                boolean allowLingering = Arrays.stream(potionBases).anyMatch(s -> s.isOf(Items.LINGERING_POTION));

                if (!allowNormal && !allowSplash && !allowLingering) {
                    return null;
                }

                EntryIngredient inArrow = EntryIngredients.ofIngredient(arrowIng);

                for (Potion potion : Registries.POTION) {
                    Identifier id = Registries.POTION.getId(potion);
                    if (id != null) {
                        String p = id.getPath();
                        if (p.equals("water") || p.equals("awkward") || p.equals("thick") || p.equals("mundane")) {
                            continue;
                        }
                    }

                    RegistryEntry<Potion> entry = Registries.POTION.getEntry(potion);

                    PotionContentsComponent comp = new PotionContentsComponent(
                        Optional.of(entry),
                        Optional.empty(),
                        List.of()
                    );

                    if (allowNormal) {
                        ItemStack normal = new ItemStack(Items.POTION);
                        normal.set(DataComponentTypes.POTION_CONTENTS, comp);

                        if (!normal.getName().getString()
                                .equals(Text.translatable("item.minecraft.potion.effect.empty").getString())) {

                            ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                            out.set(DataComponentTypes.POTION_CONTENTS, comp);

                            registry.add(new FletchingTableDisplay(
                                List.of(inArrow, EntryIngredient.of(EntryStacks.of(normal))),
                                List.of(EntryIngredient.of(EntryStacks.of(out)))
                            ));
                        }
                    }

                    if (allowSplash) {
                        ItemStack splash = new ItemStack(Items.SPLASH_POTION);
                        splash.set(DataComponentTypes.POTION_CONTENTS, comp);

                        if (!splash.getName().getString()
                                .equals(Text.translatable("item.minecraft.splash_potion.effect.empty").getString())) {

                            ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                            out.set(DataComponentTypes.POTION_CONTENTS, comp);

                            registry.add(new FletchingTableDisplay(
                                List.of(inArrow, EntryIngredient.of(EntryStacks.of(splash))),
                                List.of(EntryIngredient.of(EntryStacks.of(out)))
                            ));
                        }
                    }

                    if (allowLingering) {
                        ItemStack lingering = new ItemStack(Items.LINGERING_POTION);
                        lingering.set(DataComponentTypes.POTION_CONTENTS, comp);

                        if (!lingering.getName().getString()
                                .equals(Text.translatable("item.minecraft.lingering_potion.effect.empty").getString())) {

                            ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                            out.set(DataComponentTypes.POTION_CONTENTS, comp);

                            registry.add(new FletchingTableDisplay(
                                List.of(inArrow, EntryIngredient.of(EntryStacks.of(lingering))),
                                List.of(EntryIngredient.of(EntryStacks.of(out)))
                            ));
                        }
                    }
                }

                return null;
            }
        );
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen ->
            new Rectangle(((screen.width - 176) / 2) + 100, ((screen.height - 166) / 2) + 30, 20, 25),
            FletchingTableBlockScreen.class,
            FletchingTableCategory.FLETCHING_TABLE_DISPLAY_CATEGORY
        );
    }
}
