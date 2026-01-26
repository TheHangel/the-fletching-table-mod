package dev.hangel.thefletchingtablemod.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FletchingTableInputSlot extends Slot {
    private final FletchingTableContainer container;

    public FletchingTableInputSlot(FletchingTableContainer container, Container inv, int index, int x, int y) {
        super(inv, index, x, y);
        this.container = container;
    }

    @Override
    public void set(ItemStack stack) {
        super.set(stack);
        container.updateResultServer(); // recalcul même si juste count/merge
    }

    @Override
    public void setChanged() {
        super.setChanged();
        container.updateResultServer();
    }
}