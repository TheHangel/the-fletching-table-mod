package dev.hangel.thefletchingtablemod.rei;

import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockScreenHandler;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

import java.util.List;

public class FletchingTableTransferHandler implements TransferHandler {

    private static final int ARROW_SLOT  = 0;
    private static final int POTION_SLOT = 1;
    private static final int CONTAINER_SLOT_COUNT = 3;

    @Override
    public ApplicabilityResult checkApplicable(Context context) {
        if (!(context.getMenu() instanceof FletchingTableBlockScreenHandler)) {
            return ApplicabilityResult.createNotApplicable();
        }
        if (!(context.getDisplay() instanceof FletchingTableDisplay)) {
            return ApplicabilityResult.createNotApplicable();
        }
        return ApplicabilityResult.createApplicable();
    }

    @Override
    public Result handle(Context context) {
        if (!(context.getMenu() instanceof FletchingTableBlockScreenHandler handler)) {
            return Result.createNotApplicable();
        }
        if (!(context.getDisplay() instanceof FletchingTableDisplay display)) {
            return Result.createNotApplicable();
        }

        MinecraftClient mc = context.getMinecraft();
        PlayerEntity player = mc.player;
        if (player == null || mc.interactionManager == null) {
            return Result.createFailed(Text.of("err"));
        }

        List<EntryIngredient> inputs = display.getInputEntries();
        if (inputs.size() < 2) {
            return Result.createNotApplicable();
        }

        ItemStack arrowTemplate  = getFirstItemStack(inputs.get(0));
        ItemStack potionTemplate = getFirstItemStack(inputs.get(1));
        if (arrowTemplate.isEmpty() || potionTemplate.isEmpty()) {
            return Result.createNotApplicable();
        }

        boolean canTransfer = canPerformTransfer(handler, arrowTemplate, potionTemplate);

        if (!context.isActuallyCrafting()) {
            if (!canTransfer) {
                return Result.createFailed(Text.of("Not enough items"));
            }
            return Result.createSuccessful();
        }

        if (!canTransfer) {
            return Result.createFailed(Text.of("Not enough items"));
        }

        clearSlotToPlayerInventory(handler, player, ARROW_SLOT);
        clearSlotToPlayerInventory(handler, player, POTION_SLOT);

        boolean okArrow  = moveOneStackLike(handler, player, arrowTemplate, ARROW_SLOT);
        boolean okPotion = moveOneStackLike(handler, player, potionTemplate, POTION_SLOT);

        if (!okArrow || !okPotion) {
            return Result.createFailed(Text.of("err"));
        }

        return Result.createSuccessful().blocksFurtherHandling(true);
    }

    private boolean canPerformTransfer(ScreenHandler handler, ItemStack arrowTemplate, ItemStack potionTemplate) {
        boolean hasArrow = false;
        boolean hasPotion = false;

        List<Slot> slots = handler.slots;
        for (int i = CONTAINER_SLOT_COUNT; i < slots.size(); i++) {
            ItemStack stack = slots.get(i).getStack();
            if (stack.isEmpty()) continue;

            if (!hasArrow && ItemStack.areItemsAndComponentsEqual(stack, arrowTemplate)) {
                hasArrow = true;
            }
            if (!hasPotion && ItemStack.areItemsAndComponentsEqual(stack, potionTemplate)) {
                hasPotion = true;
            }

            if (hasArrow && hasPotion) {
                return true;
            }
        }

        return false;
    }

    private ItemStack getFirstItemStack(EntryIngredient ingredient) {
        if (ingredient.isEmpty()) return ItemStack.EMPTY;
        EntryStack<?> entry = ingredient.get(0);
        if (entry.getValue() instanceof ItemStack stack) {
            return stack.copy();
        }
        return ItemStack.EMPTY;
    }

    private void clearSlotToPlayerInventory(ScreenHandler handler, PlayerEntity player, int slotIndex) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.interactionManager == null) return;

        Slot slot = handler.slots.get(slotIndex);
        if (!slot.hasStack()) return;

        int syncId = handler.syncId;

        mc.interactionManager.clickSlot(syncId, slotIndex, 0, SlotActionType.PICKUP, player);

        int firstPlayerSlot = CONTAINER_SLOT_COUNT;
        int lastSlot = handler.slots.size();

        for (int i = firstPlayerSlot; i < lastSlot; i++) {
            if (!handler.slots.get(i).hasStack()) {
                mc.interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, player);
                return;
            }
        }

        mc.interactionManager.clickSlot(syncId, slotIndex, 0, SlotActionType.PICKUP, player);
    }

    private boolean moveOneStackLike(ScreenHandler handler, PlayerEntity player, ItemStack template, int targetSlotIndex) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.interactionManager == null) return false;

        int syncId = handler.syncId;
        List<Slot> slots = handler.slots;
        int firstPlayerSlot = CONTAINER_SLOT_COUNT;

        int sourceSlot = -1;
        for (int i = firstPlayerSlot; i < slots.size(); i++) {
            ItemStack stack = slots.get(i).getStack();
            if (stack.isEmpty()) continue;
            if (ItemStack.areItemsAndComponentsEqual(stack, template)) {
                sourceSlot = i;
                break;
            }
        }

        if (sourceSlot == -1) {
            return false;
        }

        mc.interactionManager.clickSlot(syncId, sourceSlot, 0, SlotActionType.PICKUP, player);
        mc.interactionManager.clickSlot(syncId, targetSlotIndex, 0, SlotActionType.PICKUP, player);
        mc.interactionManager.clickSlot(syncId, sourceSlot, 0, SlotActionType.PICKUP, player);

        return true;
    }
}
