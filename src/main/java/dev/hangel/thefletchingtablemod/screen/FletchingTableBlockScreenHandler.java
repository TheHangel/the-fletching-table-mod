package dev.hangel.thefletchingtablemod.screen;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipeInput;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class FletchingTableBlockScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    private static final int ARROW_SLOT = 0, POTION_SLOT = 1, TIPPED_ARROW_SLOT = 2;

    private final BlockPos pos;
    private final PlayerEntity opener;
    private final ScreenHandlerContext context;
    private final ServerRecipeManager serverRecipeManager;

    private boolean suppressCraft;

    public FletchingTableBlockScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, pos, ScreenHandlerContext.create(playerInventory.player.getEntityWorld(), pos));
    }

    public FletchingTableBlockScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos, ScreenHandlerContext context) {
        super(TheFletchingTableMod.FLETCHING_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = new SimpleInventory(3) {
            @Override public void markDirty() {
                super.markDirty();
                if (!suppressCraft) {
                    craftLogic();
                }
            }
        };
        this.pos = pos;
        this.opener = playerInventory.player;
        this.context = context;

        World world = playerInventory.player.getEntityWorld();
        if (world.getRecipeManager() instanceof ServerRecipeManager srm) {
            this.serverRecipeManager = srm;
        } else {
            this.serverRecipeManager = null;
        }

        this.addSlot(new Slot(this.inventory, ARROW_SLOT,  25, 34) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return isArrowInput(stack);
            }
        });

        this.addSlot(new Slot(this.inventory, POTION_SLOT,  78, 34) {
            @Override public boolean canInsert(ItemStack stack) {
                return isPotionInput(stack);
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

        this.addPlayerSlots(playerInventory, 8, 84);
    }

    private boolean isArrowInput(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (serverRecipeManager != null) {
            for (RecipeEntry<?> entry : serverRecipeManager.values()) {
                if (entry.value() instanceof FletchingTableRecipe recipe) {
                    if (recipe.arrowInput().test(stack)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return stack.isOf(Items.ARROW);
    }

    private boolean isPotionInput(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (serverRecipeManager != null) {
            for (RecipeEntry<?> entry : serverRecipeManager.values()) {
                if (entry.value() instanceof FletchingTableRecipe recipe) {
                    if (recipe.potionInput().test(stack)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION);
    }

    private void craftLogic() {
        if (serverRecipeManager == null) return;

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
        ItemStack tippedArrowsStack = recipe.get().value().output().copy();
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
        if (serverRecipeManager == null) return Optional.empty();
        World world = opener.getEntityWorld();
        return serverRecipeManager.getFirstMatch(TheFletchingTableMod.FLETCHING_TABLE_RECIPE_TYPE, new FletchingTableRecipeInput(inventory.getStack(ARROW_SLOT), inventory.getStack(POTION_SLOT)), world);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (player.getEntityWorld().isClient()) return;

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
            if (isArrowInput(original)) {
                if (!this.insertItem(original, ARROW_SLOT, ARROW_SLOT + 1, false)) return ItemStack.EMPTY;
            } else if (isPotionInput(original)) {
                if (!this.insertItem(original, POTION_SLOT, POTION_SLOT + 1, false)) return ItemStack.EMPTY;
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (original.isEmpty()) slot.setStack(ItemStack.EMPTY);
        else slot.markDirty();

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(context, opener, Blocks.FLETCHING_TABLE);
    }
}
