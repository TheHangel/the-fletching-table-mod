package dev.hangel.thefletchingtablemod;

import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockMenu;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreen;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(TheFletchingTableMod.MOD_ID)
public class TheFletchingTableMod
{
    public static final String MOD_ID = "the_fletching_table_mod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, TheFletchingTableMod.MOD_ID);

    public static final RegistryObject<MenuType<FletchingTableBlockMenu>> FLETCHING_TABLE_MENU =
            MENUS.register("fletching_table",
                    () -> IForgeMenuType.create(FletchingTableBlockMenu::new)
            );

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MOD_ID);

    public static final RegistryObject<RecipeSerializer<FletchingTableRecipe>> FLETCHING_TABLE_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("fletching_table", FletchingTableRecipe.Serializer::new);

    public static final RegistryObject<RecipeType<FletchingTableRecipe>> FLETCHING_TABLE_RECIPE_TYPE =
            RECIPE_TYPES.register("fletching_table",
                    () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MOD_ID, "fletching_table"))
            );

    public TheFletchingTableMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        MENUS.register(modEventBus);

        RECIPE_SERIALIZERS.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            event.enqueueWork(() -> MenuScreens.register(
                FLETCHING_TABLE_MENU.get(),
                FletchingTableBlockScreen::new
            ));
        }
    }
}
