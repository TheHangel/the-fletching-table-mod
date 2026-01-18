package dev.hangel.thefletchingtablemod.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class FletchingTableOutputSlot extends Slot {
    private final IInventory input;

    public FletchingTableOutputSlot(IInventory outputInv, int index, int x, int y, IInventory inputInv) {
        super(outputInv, index, x, y);
        this.input = inputInv;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
        // stack = ce que le joueur vient de prendre (donc count = output count)
        int toConsume = stack.getCount();

        // slot0 = arrows, slot1 = potion
        ItemStack arrows = input.getItem(0);
        ItemStack potion = input.getItem(1);

        if (!arrows.isEmpty()) arrows.shrink(Math.min(toConsume, arrows.getCount()));
        if (!potion.isEmpty()) potion.shrink(1);

        input.setChanged();
        return super.onTake(player, stack);
    }
}