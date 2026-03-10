package dev.hangel.thefletchingtablemod.rei;

import dev.hangel.thefletchingtablemod.screen.FletchingTableBlockMenu;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FletchingTableTransferHandler implements TransferHandler {
    private static final int ARROW_SLOT = 0;
    private static final int POTION_SLOT = 1;
    private static final int PLAYER_INV_START = 3;
    private static final int PLAYER_INV_END = 39;

    @Override
    public ApplicabilityResult checkApplicable(Context context) {
        Display display = context.getDisplay();
        if (!(context.getMenu() instanceof FletchingTableBlockMenu)
                || !FletchingTableREICategory.CATEGORY_ID.equals(display.getCategoryIdentifier())) {
            return ApplicabilityResult.createNotApplicable();
        }
        return ApplicabilityResult.createApplicable();
    }

    @Override
    public Result handle(Context context) {
        if (!(context.getMenu() instanceof FletchingTableBlockMenu menu)) {
            return Result.createNotApplicable();
        }

        Display display = context.getDisplay();
        List<EntryIngredient> inputs = display.getInputEntries();
        if (inputs.size() < 2) return Result.createNotApplicable();

        ItemStack wantedArrow = getFirstItemStack(inputs.get(0));
        ItemStack wantedPotion = getFirstItemStack(inputs.get(1));

        if (wantedArrow.isEmpty() || wantedPotion.isEmpty()) {
            return Result.createFailed(Component.literal("Invalid recipe"));
        }

        Minecraft mc = context.getMinecraft();
        if (mc.player == null) return Result.createFailed(Component.literal("No player"));

        Inventory playerInv = mc.player.getInventory();

        int arrowSourceSlot = findMatchingSlot(menu, playerInv, wantedArrow, PLAYER_INV_START, PLAYER_INV_END);
        int potionSourceSlot = findMatchingSlot(menu, playerInv, wantedPotion, PLAYER_INV_START, PLAYER_INV_END);

        if (arrowSourceSlot == -1 && menu.getSlot(ARROW_SLOT).getItem().isEmpty()) {
            return Result.createFailed(Component.translatable("error.rei.not.enough.materials"));
        }
        if (potionSourceSlot == -1 && menu.getSlot(POTION_SLOT).getItem().isEmpty()) {
            return Result.createFailed(Component.translatable("error.rei.not.enough.materials"));
        }

        if (!context.isActuallyCrafting()) {
            return Result.createSuccessful();
        }

        MultiPlayerGameMode gameMode = mc.gameMode;
        if (gameMode == null) return Result.createFailed(Component.literal("No game mode"));

        int containerId = menu.containerId;

        // Clear existing items from input slots back to inventory
        if (menu.getSlot(ARROW_SLOT).hasItem()) {
            gameMode.handleInventoryMouseClick(containerId, ARROW_SLOT, 0, ClickType.QUICK_MOVE, mc.player);
        }
        if (menu.getSlot(POTION_SLOT).hasItem()) {
            gameMode.handleInventoryMouseClick(containerId, POTION_SLOT, 0, ClickType.QUICK_MOVE, mc.player);
        }

        // Re-find slots after clearing (indices may have shifted)
        arrowSourceSlot = findMatchingSlot(menu, playerInv, wantedArrow, PLAYER_INV_START, PLAYER_INV_END);
        potionSourceSlot = findMatchingSlot(menu, playerInv, wantedPotion, PLAYER_INV_START, PLAYER_INV_END);

        // Move arrow to slot 0
        if (arrowSourceSlot != -1) {
            gameMode.handleInventoryMouseClick(containerId, arrowSourceSlot, 0, ClickType.PICKUP, mc.player);
            gameMode.handleInventoryMouseClick(containerId, ARROW_SLOT, 0, ClickType.PICKUP, mc.player);
            // If there's leftover on cursor, put it back
            if (!menu.getCarried().isEmpty()) {
                gameMode.handleInventoryMouseClick(containerId, arrowSourceSlot, 0, ClickType.PICKUP, mc.player);
            }
        }

        // Move potion to slot 1
        if (potionSourceSlot != -1) {
            gameMode.handleInventoryMouseClick(containerId, potionSourceSlot, 0, ClickType.PICKUP, mc.player);
            gameMode.handleInventoryMouseClick(containerId, POTION_SLOT, 0, ClickType.PICKUP, mc.player);
            // If there's leftover on cursor, put it back
            if (!menu.getCarried().isEmpty()) {
                gameMode.handleInventoryMouseClick(containerId, potionSourceSlot, 0, ClickType.PICKUP, mc.player);
            }
        }

        return Result.createSuccessful();
    }

    private static ItemStack getFirstItemStack(EntryIngredient ingredient) {
        for (EntryStack<?> entry : ingredient) {
            if (entry.getType().equals(VanillaEntryTypes.ITEM)) {
                ItemStack stack = entry.castValue();
                if (!stack.isEmpty()) return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private static int findMatchingSlot(FletchingTableBlockMenu menu, Inventory playerInv, ItemStack wanted, int from, int to) {
        for (int i = from; i < to; i++) {
            ItemStack slotStack = menu.getSlot(i).getItem();
            if (ItemStack.isSameItemSameComponents(slotStack, wanted)) {
                return i;
            }
        }
        return -1;
    }
}