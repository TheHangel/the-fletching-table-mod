package dev.hangel.thefletchingtablemod.mixin;

import dev.hangel.thefletchingtablemod.container.FletchingTableContainer;
import net.minecraft.block.BlockState;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
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
        World world,
        BlockPos pos,
        PlayerEntity player,
        Hand hand,
        BlockRayTraceResult hit,
        CallbackInfoReturnable<ActionResultType> cir
    ) {
        if (world.isClientSide) {
            cir.setReturnValue(ActionResultType.SUCCESS);
            return;
        }

        NetworkHooks.openGui((ServerPlayerEntity) player,
            new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent("block.minecraft.fletching_table");
                }

                @Override
                public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity p) {
                    return new FletchingTableContainer(windowId, world, pos, inv, p);
                }
            },
            (buf) -> buf.writeBlockPos(pos)
        );

        cir.setReturnValue(ActionResultType.CONSUME);
    }
}