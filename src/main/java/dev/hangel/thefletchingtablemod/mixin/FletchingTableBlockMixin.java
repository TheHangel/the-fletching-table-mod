package dev.hangel.thefletchingtablemod.mixin;

import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FletchingTableBlock.class)
public abstract class FletchingTableBlockMixin {
	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
	private void theFletchingTableMod$openGui(BlockState state, World world, BlockPos pos,
											  PlayerEntity player, BlockHitResult hit,
											  CallbackInfoReturnable<ActionResult> cir) {
		if (world.isClient()) {
			cir.setReturnValue(ActionResult.SUCCESS);
			return;
		}

		if (player instanceof ServerPlayerEntity serverPlayer) {
			serverPlayer.openHandledScreen(new ExtendedScreenHandlerFactory<BlockPos>() {
				@Override public BlockPos getScreenOpeningData(ServerPlayerEntity sp) { return pos; }
				@Override public Text getDisplayName() { return Text.translatable("block.minecraft.fletching_table"); }
				@Override
				public FletchingTableBlockScreenHandler createMenu(int syncId,
                                                                   net.minecraft.entity.player.PlayerInventory inv,
                                                                   PlayerEntity p) {
					return new FletchingTableBlockScreenHandler(syncId, inv, pos);
				}
			});
			cir.setReturnValue(ActionResult.CONSUME);
		}
	}
}
