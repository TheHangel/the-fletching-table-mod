package dev.hangel.the_fletching_table_mod.mixin;

import dev.hangel.the_fletching_table_mod.TheFletchingTableMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FletchingTableBlock.class)
public abstract class FletchingTableBlockMixin extends CraftingTableBlock {

    private static final Text SCREEN_TITLE = Text.translatable("container.fletching");

    public FletchingTableBlockMixin(Settings settings) {
        super(settings);
    }

    /*@Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new FletchingScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), SCREEN_TITLE);
    }*/

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onUseMixin(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info) {
        /*if (AdditionMain.CONFIG.fletching_table_use) {
            if (!world.isClient) {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            }
            info.setReturnValue(ActionResult.success(world.isClient));
        }*/
        //if (world.isClient) {
            player.swingHand(hand);
            player.sendMessage(Text.literal("FLET"), true);
        //}
    }
}
