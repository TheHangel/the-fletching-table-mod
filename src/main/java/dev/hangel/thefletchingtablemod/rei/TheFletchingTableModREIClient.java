package dev.hangel.thefletchingtablemod.rei;

import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreen;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
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

public class TheFletchingTableModREIClient implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FletchingTableCategory());

        registry.addWorkstations(FletchingTableCategory.FLETCHING_TABLE_DISPLAY_CATEGORY, EntryStacks.of(Blocks.FLETCHING_TABLE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        EntryIngredient inArrow = EntryIngredients.ofIngredient(Ingredient.ofItems(Items.ARROW));

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

            // Normal potion -> tipped arrow
            {
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

            // Splash potion -> tipped arrow
            {
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

            // Lingering potion -> tipped arrow
            {
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
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen ->
            new Rectangle(((screen.width - 176) / 2) + 100, ((screen.height - 166) / 2) + 30, 20, 25),
            FletchingTableBlockScreen.class,
            FletchingTableCategory.FLETCHING_TABLE_DISPLAY_CATEGORY
        );
    }

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        registry.register(new FletchingTableTransferHandler());
    }
}
