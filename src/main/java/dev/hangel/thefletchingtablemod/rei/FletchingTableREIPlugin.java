package dev.hangel.thefletchingtablemod.rei;

import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreen;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
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

import java.util.List;
import java.util.Optional;

@REIPluginClient
public class FletchingTableREIPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FletchingTableREICategory());
        registry.addWorkstations(FletchingTableREICategory.CATEGORY_ID, EntryStacks.of(Blocks.FLETCHING_TABLE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
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
                    registry.add(new FletchingTableREIDisplay(arrowDisplay.copy(), normal, out));
                }
            }

            // Splash potion
            {
                ItemStack splash = new ItemStack(Items.SPLASH_POTION);
                splash.set(DataComponents.POTION_CONTENTS, contents);

                if (!splash.getHoverName().getString().equals(Component.translatable("item.minecraft.splash_potion.effect.empty").getString())) {
                    ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                    out.set(DataComponents.POTION_CONTENTS, contents);
                    registry.add(new FletchingTableREIDisplay(arrowDisplay.copy(), splash, out));
                }
            }

            // Lingering potion
            {
                ItemStack lingering = new ItemStack(Items.LINGERING_POTION);
                lingering.set(DataComponents.POTION_CONTENTS, contents);

                if (!lingering.getHoverName().getString().equals(Component.translatable("item.minecraft.lingering_potion.effect.empty").getString())) {
                    ItemStack out = new ItemStack(Items.TIPPED_ARROW);
                    out.set(DataComponents.POTION_CONTENTS, contents);
                    registry.add(new FletchingTableREIDisplay(arrowDisplay.copy(), lingering, out));
                }
            }
        }
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(screen.getGuiLeft() + 100, screen.getGuiTop() + 34, 25, 20), FletchingTableBlockScreen.class, FletchingTableREICategory.CATEGORY_ID);
    }
}