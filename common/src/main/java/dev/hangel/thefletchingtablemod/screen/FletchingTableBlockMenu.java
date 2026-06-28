package dev.hangel.thefletchingtablemod.screen;

import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipeInput;
import dev.hangel.thefletchingtablemod.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FletchingTableBlockMenu extends AbstractContainerMenu {
    private final SimpleContainer inventory;

    private static final int ARROW_SLOT = 0, POTION_SLOT = 1, TIPPED_ARROW_SLOT = 2;

    private final BlockPos pos;
    private final Player opener;

    private boolean suppressCraft;

    public FletchingTableBlockMenu(int syncId, Inventory inv, BlockPos pos) {
        super(ModRegistry.FLETCHING_TABLE_MENU, syncId);
        this.pos = pos;
        this.opener = inv.player;

        this.inventory = new SimpleContainer(3) {
            @Override
            public void setChanged() {
                super.setChanged();
                if (!suppressCraft && !opener.level().isClientSide()) {
                    craftLogic();
                }
            }
        };

        this.addSlot(new Slot(this.inventory, ARROW_SLOT, 25, 34) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isArrowInput(stack);
            }
        });

        this.addSlot(new Slot(this.inventory, POTION_SLOT, 78, 34) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isPotionInput(stack);
            }
        });

        this.addSlot(new Slot(this.inventory, TIPPED_ARROW_SLOT, 132, 34) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                super.onTake(player, stack);

                int toConsume  = stack.getCount();

                ItemStack arrows = inventory.getItem(ARROW_SLOT);
                ItemStack potion = inventory.getItem(POTION_SLOT);

                arrows.shrink(Math.min(toConsume, arrows.getCount()));
                if(!potion.isEmpty()) potion.shrink(1);
            }
        });

        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
    }

    private RecipeManager getRecipeManager() {
        if (opener.level() instanceof ServerLevel serverLevel) {
            return serverLevel.recipeAccess();
        }
        return null;
    }

    private boolean isArrowInput(ItemStack stack) {
        if (stack.isEmpty()) return false;
        RecipeManager rm = getRecipeManager();
        if (rm == null) {
            return stack.is(Items.ARROW);
        }

        for (RecipeHolder<?> entry : rm.getRecipes()) {
            if (entry.value() instanceof FletchingTableRecipe recipe && recipe.arrowInput().test(stack)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPotionInput(ItemStack stack) {
        if (stack.isEmpty()) return false;
        RecipeManager rm = getRecipeManager();
        if (rm == null) {
            return stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION);
        }

        for (RecipeHolder<?> entry : rm.getRecipes()) {
            if (entry.value() instanceof FletchingTableRecipe recipe && recipe.potionInput().test(stack)) {
                return true;
            }
        }
        return false;
    }

    private void craftLogic() {
        if(this.hasRecipe()) {
            this.showTippedArrows();
        }
        else {
            this.removeTippedArrows();
        }
        this.broadcastChanges();
    }

    private void showTippedArrows() {
        Optional<RecipeHolder<@NotNull FletchingTableRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) return;

        int count = this.inventory.getItem(ARROW_SLOT).getCount();
        ItemStack tippedArrowsStack = recipe.get().value().output().create();
        tippedArrowsStack.setCount(count);

        ItemStack potionStack = this.inventory.getItem(POTION_SLOT);

        PotionContents potionContents = potionStack.get(DataComponents.POTION_CONTENTS);

        if (potionContents != null) {
            tippedArrowsStack.set(DataComponents.POTION_CONTENTS, potionContents);
        }

        suppressCraft = true;
        this.inventory.setItem(TIPPED_ARROW_SLOT, tippedArrowsStack);
        suppressCraft = false;
    }

    private void removeTippedArrows() {
        suppressCraft = true;
        this.inventory.setItem(TIPPED_ARROW_SLOT, ItemStack.EMPTY);
        suppressCraft = false;
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<@NotNull FletchingTableRecipe>> recipe = getCurrentRecipe();
        return recipe.isPresent();
    }

    private Optional<RecipeHolder<@NotNull FletchingTableRecipe>> getCurrentRecipe() {
        RecipeManager rm = getRecipeManager();
        if (rm == null) return Optional.empty();
        return rm.getRecipeFor(ModRegistry.FLETCHING_TABLE_RECIPE_TYPE, new FletchingTableRecipeInput(inventory.getItem(ARROW_SLOT), inventory.getItem(POTION_SLOT)), this.opener.level());
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        if (player.level().isClientSide()) return;

        for (int slot = 0; slot < 2; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.isEmpty()) continue;

            ItemStack toGive = stack.copy();
            inventory.setItem(slot, ItemStack.EMPTY);

            player.getInventory().placeItemBackInInventory(toGive);
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack original = slot.getItem();
        newStack = original.copy();

        int containerSlots = 3;
        int playerStart = containerSlots;
        int hotbarEnd   = playerStart + 27 + 9;

        if (index < containerSlots) {
            if (!this.moveItemStackTo(original, playerStart, hotbarEnd, true)) return ItemStack.EMPTY;

            if (index == 2) {
                slot.onTake(player, newStack);
            }
        } else {
            if (!this.moveItemStackTo(original, 0, containerSlots, false)) return ItemStack.EMPTY;
        }

        if (original.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return newStack;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (opener.level().isClientSide()) return true;
        return opener.blockPosition().closerThan(this.pos, 8.0);
    }
}
