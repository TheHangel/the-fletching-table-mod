package dev.hangel.thefletchingtablemod;

import dev.hangel.thefletchingtablemod.container.FletchingTableContainer;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipeSerializer;
import dev.hangel.thefletchingtablemod.screen.FletchingTableScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TheFletchingTableMod.MOD_ID)
public class TheFletchingTableMod {

    public static final String MOD_ID = "the_fletching_table_mod";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, TheFletchingTableMod.MOD_ID);

    public static final RegistryObject<ContainerType<FletchingTableContainer>> FLETCHING_TABLE_CONTAINER =
            CONTAINER_TYPES.register("fletching_table", () ->
                    IForgeContainerType.create((windowId, playerInv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        PlayerEntity player = playerInv.player;
                        World world = player.level;
                        return new FletchingTableContainer(windowId, world, pos, playerInv, player);
                    }));

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TheFletchingTableMod.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<FletchingTableRecipe>> FLETCHING_TABLE_SERIALIZER =
            RECIPE_SERIALIZERS.register("fletching_table", FletchingTableRecipeSerializer::new);

    public TheFletchingTableMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        CONTAINER_TYPES.register(modBus);
        RECIPE_SERIALIZERS.register(modBus);

        modBus.addListener(this::onClientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ScreenManager.register(FLETCHING_TABLE_CONTAINER.get(), FletchingTableScreen::new);
        });
    }
}