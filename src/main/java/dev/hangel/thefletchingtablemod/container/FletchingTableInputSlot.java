package dev.hangel.thefletchingtablemod.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class FletchingTableInputSlot extends Slot {
    private final FletchingTableContainer container;

    public FletchingTableInputSlot(FletchingTableContainer container, IInventory inv, int index, int x, int y) {
        super(inv, index, x, y);
        this.container = container;
    }

    @Override
    public void set(ItemStack stack) {
        super.set(stack);
        container.updateResultServer();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        container.updateResultServer();
    }
}
