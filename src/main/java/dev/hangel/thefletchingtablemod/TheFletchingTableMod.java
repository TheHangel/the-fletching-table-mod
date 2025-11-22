package dev.hangel.thefletchingtablemod;

import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(TheFletchingTableMod.MOD_ID)
public class TheFletchingTableMod {
    public static final String MOD_ID = "the_fletching_table_mod";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<FletchingTableBlockMenu>> FLETCHING_TABLE_MENU =
            MENUS.register("fletching_table",
                    () -> IMenuTypeExtension.create(
                            FletchingTableBlockMenu::new
                    )
            );

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, MOD_ID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FletchingTableRecipe>>
            FLETCHING_TABLE_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("fletching_table", FletchingTableRecipe.Serializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<FletchingTableRecipe>>
            FLETCHING_TABLE_RECIPE_TYPE =
            RECIPE_TYPES.register("fletching_table",
                    () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MOD_ID, "fletching_table"))
            );

    public TheFletchingTableMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        MENUS.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
