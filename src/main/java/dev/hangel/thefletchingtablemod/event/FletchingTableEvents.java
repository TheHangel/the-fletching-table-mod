package dev.hangel.thefletchingtablemod.event;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = TheFletchingTableMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FletchingTableEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();

        if (level.isClientSide()) {
            return;
        }

        BlockState state = level.getBlockState(pos);

        if (!state.is(Blocks.FLETCHING_TABLE)) {
            return;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        serverPlayer.openMenu(new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return Component.translatable("block.minecraft.fletching_table");
            }

            @Override
            public FletchingTableBlockMenu createMenu(int syncId, @NotNull Inventory inv, Player p) {
                return new FletchingTableBlockMenu(syncId, inv, pos);
            }
        }, buf -> buf.writeBlockPos(pos));

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.CONSUME);
        event.setUseBlock(Event.Result.DENY);
        event.setUseItem(Event.Result.DENY);
    }
}
