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
                Ingredient arrowIng = recipe.arrowInput();

                for (Potion potion : Registries.POTION) {
                    Identifier id = Registries.POTION.getId(potion);
                    if (id != null) {
                        String p = id.getPath();
                        if (p.equals("water") || p.equals("awkward") || p.equals("thick") || p.equals("mundane")) {
                            continue;
                        }
                    }

                    PotionContentsComponent comp = new PotionContentsComponent(
                        Optional.of(new RegistryEntry.Direct<>(potion)),
                        Optional.empty(),
                        List.of()
                    );

                    EntryIngredient inArrow = EntryIngredients.ofIngredient(arrowIng);

                    ItemStack potionStack = new ItemStack(Items.POTION);
                    potionStack.set(DataComponentTypes.POTION_CONTENTS, comp);
                    EntryIngredient inPotion = EntryIngredient.of(EntryStacks.of(potionStack));

                    ItemStack outStack = new ItemStack(Items.TIPPED_ARROW);
                    outStack.set(DataComponentTypes.POTION_CONTENTS, comp);
                    EntryIngredient out = EntryIngredient.of(EntryStacks.of(outStack));

                    if (
                        potionStack.getName().getString().equals(Text.translatable("item.minecraft.potion.effect.empty").getString())
                        ||
                        outStack.getName().getString().equals(Text.translatable("item.minecraft.tipped_arrow.effect.empty").getString())
                    ) {
                        continue;
                    }

                    registry.add(new FletchingTableDisplay(
                        List.of(inArrow, inPotion),
                        List.of(out)
                    ));

                    ItemStack splash = new ItemStack(Items.SPLASH_POTION);
                    splash.set(DataComponentTypes.POTION_CONTENTS, comp);

                    if (splash.getName().getString().equals(Text.translatable("item.minecraft.splash_potion.effect.empty").getString())) {
                        continue;
                    }

                    registry.add(new FletchingTableDisplay(
                        List.of(inArrow, EntryIngredient.of(EntryStacks.of(splash))),
                        List.of(out)
                    ));

                    ItemStack lingering = new ItemStack(Items.LINGERING_POTION);
                    lingering.set(DataComponentTypes.POTION_CONTENTS, comp);

                    if (lingering.getName().getString().equals(Text.translatable("item.minecraft.lingering_potion.effect.empty").getString())) {
                        continue;
                    }

                    registry.add(new FletchingTableDisplay(
                        List.of(inArrow, EntryIngredient.of(EntryStacks.of(lingering))),
                        List.of(out)
                    ));
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
