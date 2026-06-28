package dev.hangel.thefletchingtablemod;

import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.registry.ModRegistry;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

@Mod(Constants.MOD_ID)
public class TheFletchingTableModNeoForge {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Constants.MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Constants.MOD_ID);

    public static final DeferredRegister<RecipeBookCategory> RECIPE_BOOK_CATEGORIES =
            DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, Constants.MOD_ID);

    public static final DeferredHolder<MenuType<?>, @NotNull MenuType<@NotNull FletchingTableBlockMenu>> FLETCHING_TABLE_MENU =
            MENUS.register("fletching_table_block_menu",
                    () -> IMenuTypeExtension.create(
                            (syncId, inv, buf) -> new FletchingTableBlockMenu(syncId, inv, BlockPos.ZERO)));

    public static final DeferredHolder<RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull FletchingTableRecipe>>
            FLETCHING_TABLE_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("fletching_table", () -> FletchingTableRecipe.SERIALIZER);

    public static final DeferredHolder<RecipeType<?>, @NotNull RecipeType<@NotNull FletchingTableRecipe>>
            FLETCHING_TABLE_RECIPE_TYPE =
            RECIPE_TYPES.register("fletching_table", RecipeType::simple);

    public static final DeferredHolder<RecipeBookCategory, @NotNull RecipeBookCategory>
            FLETCHING_TABLE_RECIPE_BOOK_CATEGORY =
            RECIPE_BOOK_CATEGORIES.register("fletching_table", RecipeBookCategory::new);

    public TheFletchingTableModNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        MENUS.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        RECIPE_BOOK_CATEGORIES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        Constants.LOG.info("{} initialised on NeoForge", Constants.MOD_NAME);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        ModRegistry.FLETCHING_TABLE_MENU = FLETCHING_TABLE_MENU.get();
        ModRegistry.FLETCHING_TABLE_RECIPE_SERIALIZER = FLETCHING_TABLE_RECIPE_SERIALIZER.get();
        ModRegistry.FLETCHING_TABLE_RECIPE_TYPE = FLETCHING_TABLE_RECIPE_TYPE.get();
        ModRegistry.FLETCHING_TABLE_RECIPE_BOOK_CATEGORY = FLETCHING_TABLE_RECIPE_BOOK_CATEGORY.get();
    }
}
