package dev.hangel.thefletchingtablemod.jei;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.screen.FletchingTableScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;

import net.minecraft.item.crafting.Ingredient;

import java.util.*;

@JeiPlugin
public class FletchingTableJEIPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_UID =
            new ResourceLocation(TheFletchingTableMod.MOD_ID, "jei_fletching_table");

    @Override
    public ResourceLocation getPluginUid() {
        System.out.println("[FT][JEI] Plugin loaded!");
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FletchingTableCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        List<FletchingTableRecipe> baseRecipes =
                mc.level.getRecipeManager().getAllRecipesFor(FletchingTableRecipe.TYPE);

        List<FletchingTableRecipe> jeiRecipes = new ArrayList<>();

        for (FletchingTableRecipe base : baseRecipes) {
            Ingredient arrowIng  = base.getArrowInput();
            Ingredient potionIng = base.getPotionInput();

            ItemStack[] potionBases = potionIng.getItems();

            boolean allowNormal    = Arrays.stream(potionBases).anyMatch(s -> s.getItem() == Items.POTION);
            boolean allowSplash    = Arrays.stream(potionBases).anyMatch(s -> s.getItem() == Items.SPLASH_POTION);
            boolean allowLingering = Arrays.stream(potionBases).anyMatch(s -> s.getItem() == Items.LINGERING_POTION);

            if (!allowNormal && !allowSplash && !allowLingering) {
                jeiRecipes.add(base);
                continue;
            }

            String emptyPotion     = new TranslationTextComponent("item.minecraft.potion.effect.empty").getString();
            String emptySplash     = new TranslationTextComponent("item.minecraft.splash_potion.effect.empty").getString();
            String emptyLingering  = new TranslationTextComponent("item.minecraft.lingering_potion.effect.empty").getString();

            for (Potion potion : Registry.POTION) {
                ResourceLocation id = Registry.POTION.getKey(potion);
                if (id == null) continue;

                String path = id.getPath();
                if (path.equals("water") || path.equals("awkward") || path.equals("thick") || path.equals("mundane")) {
                    continue;
                }

                if (allowNormal) {
                    ItemStack in = new ItemStack(Items.POTION);
                    PotionUtils.setPotion(in, potion);

                    if (!in.getHoverName().getString().equals(emptyPotion)) {
                        ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                        PotionUtils.setPotion(out, potion);

                        Ingredient inIng = Ingredient.of(in);

                        ResourceLocation jeiId = new ResourceLocation(
                                TheFletchingTableMod.MOD_ID,
                                "jei/fletching_table/" + base.getId().getPath()
                                        + "/" + id.getNamespace() + "_" + id.getPath()
                                        + "/" + in.getItem().getRegistryName().getPath()
                        );

                        String group = base.getGroup();

                        int jsonCount = base.getResultCountFromJson(); // ou 0 si tu veux

                        jeiRecipes.add(new FletchingTableRecipe(jeiId, group, arrowIng, inIng, out, jsonCount));
                    }
                }

                if (allowSplash) {
                    ItemStack in = new ItemStack(Items.SPLASH_POTION);
                    PotionUtils.setPotion(in, potion);

                    if (!in.getHoverName().getString().equals(emptySplash)) {
                        ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                        PotionUtils.setPotion(out, potion);

                        Ingredient inIng = Ingredient.of(in);

                        ResourceLocation jeiId = new ResourceLocation(
                                TheFletchingTableMod.MOD_ID,
                                "jei/fletching_table/" + base.getId().getPath()
                                        + "/" + id.getNamespace() + "_" + id.getPath()
                                        + "/" + in.getItem().getRegistryName().getPath()
                        );

                        String group = base.getGroup();

                        int jsonCount = base.getResultCountFromJson();

                        jeiRecipes.add(new FletchingTableRecipe(jeiId, group, arrowIng, inIng, out, jsonCount));
                    }
                }

                if (allowLingering) {
                    ItemStack in = new ItemStack(Items.LINGERING_POTION);
                    PotionUtils.setPotion(in, potion);

                    if (!in.getHoverName().getString().equals(emptyLingering)) {
                        ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                        PotionUtils.setPotion(out, potion);

                        Ingredient inIng = Ingredient.of(in);

                        ResourceLocation jeiId = new ResourceLocation(
                                TheFletchingTableMod.MOD_ID,
                                "jei/fletching_table/" + base.getId().getPath()
                                        + "/" + id.getNamespace() + "_" + id.getPath()
                                        + "/" + in.getItem().getRegistryName().getPath()
                        );

                        String group = base.getGroup();

                        int jsonCount = base.getResultCountFromJson();

                        jeiRecipes.add(new FletchingTableRecipe(jeiId, group, arrowIng, inIng, out, jsonCount));
                    }
                }
            }
        }

        registration.addRecipes(jeiRecipes, FletchingTableCategory.UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Blocks.FLETCHING_TABLE), FletchingTableCategory.UID);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(
                FletchingTableScreen.class,
                100, 34,
                25, 20,
                FletchingTableCategory.UID
        );
    }
}