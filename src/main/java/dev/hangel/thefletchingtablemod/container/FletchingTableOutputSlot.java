package dev.hangel.thefletchingtablemod.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FletchingTableOutputSlot extends Slot {
    private final Container input;

    public FletchingTableOutputSlot(Container outputInv, int index, int x, int y, Container inputInv) {
        super(outputInv, index, x, y);
        this.input = inputInv;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack onTake(Player player, ItemStack stack) {
        int toConsume = stack.getCount();

        ItemStack arrows = input.getItem(0);
        ItemStack potion = input.getItem(1);

        if (!arrows.isEmpty()) arrows.shrink(Math.min(toConsume, arrows.getCount()));
        if (!potion.isEmpty()) potion.shrink(1);

        input.setChanged();
        return super.onTake(player, stack);
    }
}