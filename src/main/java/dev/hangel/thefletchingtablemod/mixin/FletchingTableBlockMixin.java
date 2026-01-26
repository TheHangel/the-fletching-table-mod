package dev.hangel.thefletchingtablemod.mixin;

import dev.hangel.thefletchingtablemod.container.FletchingTableContainer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FletchingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FletchingTableBlock.class)
public abstract class FletchingTableBlockMixin {

    @Inject(
        method = "use",
        at = @At("HEAD"),
        cancellable = true
    )
    private void FletchingTableBlock$use(
        BlockState state,
        Level world,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hit,
        CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (world.isClientSide) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        if (!(player instanceof ServerPlayer)) {
            cir.setReturnValue(InteractionResult.CONSUME);
            return;
        }

        player.openMenu(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                buf.writeBlockPos(pos);
            }

            @Override
            public Component getDisplayName() {
                return new TranslatableComponent("block.minecraft.fletching_table");
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, Inventory inv, Player p) {
                return new FletchingTableContainer(syncId, world, pos, inv, p);
            }
        });

        cir.setReturnValue(InteractionResult.CONSUME);
    }
}