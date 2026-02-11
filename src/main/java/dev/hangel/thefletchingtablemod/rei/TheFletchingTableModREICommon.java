package dev.hangel.thefletchingtablemod.rei;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class TheFletchingTableModREICommon implements REICommonPlugin {
    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(FletchingTableRecipe.class)
            .filterType(TheFletchingTableMod.FLETCHING_TABLE_RECIPE_TYPE)
            .fillMultiple(recipeEntry -> {
                FletchingTableRecipe recipe = recipeEntry.value();
                List<FletchingTableDisplay> displays = new ArrayList<>();

                Ingredient arrowIng = recipe.arrowInput();
                Ingredient potionIng = recipe.potionInput();

                boolean allowNormal = potionIng.getMatchingItems().anyMatch(e -> e.value().equals(Items.POTION));
                boolean allowSplash = potionIng.getMatchingItems().anyMatch(e -> e.value().equals(Items.SPLASH_POTION));
                boolean allowLingering = potionIng.getMatchingItems().anyMatch(e -> e.value().equals(Items.LINGERING_POTION));

                EntryIngredient inArrow = EntryIngredients.ofIngredient(arrowIng);

                if (!allowNormal && !allowSplash && !allowLingering) {
                    displays.add(new FletchingTableDisplay(
                        List.of(
                            inArrow,
                            EntryIngredients.ofIngredient(potionIng)
                        ),
                        List.of(EntryIngredients.of(recipe.output()))
                    ));
                }

                for (Potion potion : Registries.POTION) {
                    Identifier id = Registries.POTION.getId(potion);
                    if (id != null) {
                        String p = id.getPath();
                        if (p.equals("water") || p.equals("awkward") || p.equals("thick") || p.equals("mundane")) {
                            continue;
                        }
                    }

                    RegistryEntry<Potion> entry = Registries.POTION.getEntry(potion);

                    PotionContentsComponent comp = new PotionContentsComponent(entry);

                    if (allowNormal) {
                        ItemStack normal = new ItemStack(Items.POTION);
                        normal.set(DataComponentTypes.POTION_CONTENTS, comp);

                        if (!normal.getName().getString()
                                .equals(Text.translatable("item.minecraft.potion.effect.empty").getString())) {

                            ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                            out.set(DataComponentTypes.POTION_CONTENTS, comp);

                            displays.add(new FletchingTableDisplay(
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

                            displays.add(new FletchingTableDisplay(
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

                            displays.add(new FletchingTableDisplay(
                                List.of(inArrow, EntryIngredient.of(EntryStacks.of(lingering))),
                                List.of(EntryIngredient.of(EntryStacks.of(out)))
                            ));
                        }
                    }
                }

                return displays;
            });
    }
}
