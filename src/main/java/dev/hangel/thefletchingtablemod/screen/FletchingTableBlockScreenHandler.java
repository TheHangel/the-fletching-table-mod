package dev.hangel.thefletchingtablemod.screen;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipeInput;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class FletchingTableBlockScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    private static final int ARROW_SLOT = 0, POTION_SLOT = 1, TIPPED_ARROW_SLOT = 2;

    private final BlockPos pos;
    private final PlayerEntity opener;

    private boolean suppressCraft;

    public FletchingTableBlockScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        super(TheFletchingTableMod.FLETCHING_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = new SimpleInventory(3) {
            @Override public void markDirty() {
                super.markDirty();
                if (!suppressCraft) {
                    craftLogic();
                }
            }
            @Override public boolean isValid(int slot, ItemStack stack) {
                return switch (slot) {
                    case ARROW_SLOT  -> stack.isOf(Items.ARROW);
                    case POTION_SLOT -> stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION);
                    default -> false;
                };
            }
        };
        this.pos = pos;
        this.opener = playerInventory.player;

        this.addSlot(new Slot(this.inventory, ARROW_SLOT,  25, 34) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.ARROW);
            }
        });

        this.addSlot(new Slot(this.inventory, POTION_SLOT,  78, 34) {
            @Override public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.POTION)
                    || stack.isOf(Items.SPLASH_POTION)
                    || stack.isOf(Items.LINGERING_POTION);
            }
        });

        this.addSlot(new Slot(this.inventory, TIPPED_ARROW_SLOT, 132, 34) {
            @Override public boolean canInsert(ItemStack stack) { return false; }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack taken) {
                super.onTakeItem(player, taken);

                int toConsume = taken.getCount();

                ItemStack arrows = inventory.getStack(ARROW_SLOT);
                ItemStack potion = inventory.getStack(POTION_SLOT);

                arrows.decrement(Math.min(toConsume, arrows.getCount()));
                if (!potion.isEmpty()) potion.decrement(1);
            }
        });

        this.addPlayerInventory(playerInventory);
        this.addPlayerHotbar(playerInventory);
    }

    private void craftLogic() {
        if(this.hasRecipe()) {
            this.showTippedArrows();
        }
        else {
            this.removeTippedArrows();
        }
    }

    private void showTippedArrows() {
        Optional<RecipeEntry<FletchingTableRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) return;

        int count = this.inventory.getStack(ARROW_SLOT).getCount();
        ItemStack tippedArrowsStack = recipe.get().value().output();
        tippedArrowsStack.setCount(count);

        ItemStack potionStack = this.inventory.getStack(POTION_SLOT);

        PotionContentsComponent potionContents = potionStack.get(DataComponentTypes.POTION_CONTENTS);

        if (potionContents != null) {
            tippedArrowsStack.set(DataComponentTypes.POTION_CONTENTS, potionContents);
        }

        suppressCraft = true;
        this.inventory.setStack(TIPPED_ARROW_SLOT, tippedArrowsStack);
        suppressCraft = false;
    }

    private void removeTippedArrows() {
        suppressCraft = true;
        this.inventory.setStack(TIPPED_ARROW_SLOT, ItemStack.EMPTY);
        suppressCraft = false;
    }

    private boolean hasRecipe() {
        Optional<RecipeEntry<FletchingTableRecipe>> recipe = getCurrentRecipe();
        return recipe.isPresent();
    }

    private Optional<RecipeEntry<FletchingTableRecipe>> getCurrentRecipe() {
        return this.opener.getWorld().getRecipeManager().getFirstMatch(TheFletchingTableMod.FLETCHING_TABLE_RECIPE_TYPE, new FletchingTableRecipeInput(inventory.getStack(ARROW_SLOT), inventory.getStack(POTION_SLOT)), this.opener.getWorld());
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (player.getWorld().isClient()) return;

        for (int slot = 0; slot < 2; slot++) {
            ItemStack stack = inventory.getStack(slot);
            if (stack.isEmpty()) continue;

            ItemStack toGive = stack.copy();
            inventory.setStack(slot, ItemStack.EMPTY);

            player.getInventory().offerOrDrop(toGive);
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (!slot.hasStack()) return ItemStack.EMPTY;

        ItemStack original = slot.getStack();
        newStack = original.copy();

        int containerSlots = 3;
        int playerStart = containerSlots;
        int hotbarEnd   = playerStart + 27 + 9;

        if (index < containerSlots) {
            if (!this.insertItem(original, playerStart, hotbarEnd, true)) return ItemStack.EMPTY;

            if (index == 2) {
                slot.onTakeItem(player, newStack);
            }
        } else {
            if (!this.insertItem(original, 0, containerSlots, false)) return ItemStack.EMPTY;
        }

        if (original.isEmpty()) slot.setStack(ItemStack.EMPTY);
        else slot.markDirty();

        return newStack;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
