package dev.hangel.thefletchingtablemod.mixin;

import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FletchingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FletchingTableBlock.class)
public class FletchingTableBlockMixin {
    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    private void theFletchingTableMod$openGui(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (level.isClientSide()) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new MenuProvider() {
                @Override public @NotNull Component getDisplayName() { return Component.translatable("block.minecraft.fletching_table"); }
                @Override
                public FletchingTableBlockMenu createMenu(int syncId, @NotNull Inventory inv, Player p) {
                    return new FletchingTableBlockMenu(syncId, inv, pos);
                }
            }, buf -> buf.writeBlockPos(pos));
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }
}
