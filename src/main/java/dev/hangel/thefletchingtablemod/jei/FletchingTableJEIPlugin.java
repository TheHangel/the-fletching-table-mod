package dev.hangel.thefletchingtablemod.jei;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreen;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreenHandler;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class FletchingTableJEIPlugin implements IModPlugin {
    @Override
    public @NotNull Identifier getPluginUid() {
        return Identifier.of(TheFletchingTableMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
            new FletchingTableJEICategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        List<FletchingTableJEIRecipe> recipes = new ArrayList<>();

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
                    recipes.add(new FletchingTableJEIRecipe(new ItemStack(Items.ARROW), normal, out));
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
                    recipes.add(new FletchingTableJEIRecipe(new ItemStack(Items.ARROW), splash, out));
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
                    recipes.add(new FletchingTableJEIRecipe(new ItemStack(Items.ARROW), lingering, out));
                }
            }
        }

        registration.addRecipes(FletchingTableJEICategory.RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(FletchingTableJEICategory.RECIPE_TYPE, Blocks.FLETCHING_TABLE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(FletchingTableBlockScreen.class, 100, 30, 20, 25, FletchingTableJEICategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(
            FletchingTableBlockScreenHandler.class,
            TheFletchingTableMod.FLETCHING_TABLE_SCREEN_HANDLER,
            FletchingTableJEICategory.RECIPE_TYPE,
            0, 2,
            3, 36
        );
    }
}
