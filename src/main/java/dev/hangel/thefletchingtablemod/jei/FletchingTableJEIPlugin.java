package dev.hangel.thefletchingtablemod.jei;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JeiPlugin
public class FletchingTableJEIPlugin implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(TheFletchingTableMod.MOD_ID, "jei_fletching_table");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FletchingTableCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<FletchingTableJEIRecipe> jeiRecipes = new ArrayList<>();

        ItemStack arrowDisplay = new ItemStack(Items.ARROW);

        for (Potion potion : BuiltInRegistries.POTION) {
            Identifier id = BuiltInRegistries.POTION.getKey(potion);
            if (id != null) {
                String path = id.getPath();
                if (path.equals("water") || path.equals("awkward") ||
                        path.equals("thick") || path.equals("mundane")) {
                    continue;
                }
            }

            Identifier potionId = BuiltInRegistries.POTION.getKey(potion);
            if (potionId == null) continue;

            ResourceKey<Potion> key = ResourceKey.create(Registries.POTION, potionId);
            Optional<Holder.Reference<Potion>> potionHolderOpt = BuiltInRegistries.POTION.get(key);
            if (potionHolderOpt.isEmpty()) continue;

            Holder.Reference<Potion> potionHolder = potionHolderOpt.get();

            PotionContents contents = new PotionContents(
                    Optional.of(potionHolder),
                    Optional.empty(),
                    List.of(),
                    Optional.empty()
            );

            // Normal potion
            {
                ItemStack normal = new ItemStack(Items.POTION);
                normal.set(DataComponents.POTION_CONTENTS, contents);

                if (!normal.getHoverName().getString().equals(Component.translatable("item.minecraft.potion.effect.empty").getString())) {
                    ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                    out.set(DataComponents.POTION_CONTENTS, contents);
                    jeiRecipes.add(new FletchingTableJEIRecipe(arrowDisplay.copy(), normal, out));
                }
            }

            // Splash potion
            {
                ItemStack splash = new ItemStack(Items.SPLASH_POTION);
                splash.set(DataComponents.POTION_CONTENTS, contents);

                if (!splash.getHoverName().getString().equals(Component.translatable("item.minecraft.splash_potion.effect.empty").getString())) {
                    ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                    out.set(DataComponents.POTION_CONTENTS, contents);
                    jeiRecipes.add(new FletchingTableJEIRecipe(arrowDisplay.copy(), splash, out));
                }
            }

            // Lingering potion
            {
                ItemStack lingering = new ItemStack(Items.LINGERING_POTION);
                lingering.set(DataComponents.POTION_CONTENTS, contents);

                if (!lingering.getHoverName().getString().equals(Component.translatable("item.minecraft.lingering_potion.effect.empty").getString())) {
                    ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                    out.set(DataComponents.POTION_CONTENTS, contents);
                    jeiRecipes.add(new FletchingTableJEIRecipe(arrowDisplay.copy(), lingering, out));
                }
            }
        }

        registration.addRecipes(FletchingTableCategory.FLETCHING_TABLE_RECIPE_TYPE, jeiRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(FletchingTableBlockScreen.class, 100, 34, 25, 20, FletchingTableCategory.FLETCHING_TABLE_RECIPE_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Blocks.FLETCHING_TABLE.asItem()), FletchingTableCategory.FLETCHING_TABLE_RECIPE_TYPE);
    }
}