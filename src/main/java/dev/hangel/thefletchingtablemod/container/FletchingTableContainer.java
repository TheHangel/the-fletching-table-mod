package dev.hangel.thefletchingtablemod.container;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import dev.hangel.thefletchingtablemod.recipe.FletchingTableRecipe;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class FletchingTableContainer extends Container {
    private final World world;
    private final BlockPos pos;
    private final PlayerEntity player;

    private final CraftingInventory inputInventory = new CraftingInventory(this, 2, 1);
    private final CraftResultInventory outputInventory = new CraftResultInventory();

    public FletchingTableContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInv, PlayerEntity player) {
        super(TheFletchingTableMod.FLETCHING_TABLE_CONTAINER.get(), windowId);
        this.world = world;
        this.pos = pos;
        this.player = player;

        this.addSlot(new FletchingTableInputSlot(this, inputInventory, 0, 25, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isArrowInput(world, stack);
            }
        });
        this.addSlot(new FletchingTableInputSlot(this, inputInventory, 1, 78, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isPotionInput(world, stack);
            }
        });

        this.addSlot(new FletchingTableOutputSlot(outputInventory, 0, 132, 34, inputInventory));

        this.addPlayerInventory(playerInv);
    }

    private void addPlayerInventory(PlayerInventory playerInv) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9,
                        8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return this.world.getBlockState(pos).getBlock() == Blocks.FLETCHING_TABLE
            && playerIn.distanceToSqr(pos.getX() + 0.5D,
            pos.getY() + 0.5D,
            pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void slotsChanged(IInventory inventoryIn) {
        super.slotsChanged(inventoryIn);
        updateResult();
    }

    private void updateResult() {
        if (this.world.isClientSide) return;

        Optional<FletchingTableRecipe> opt = this.world.getRecipeManager()
                .getRecipeFor(FletchingTableRecipe.TYPE, inputInventory, this.world);

        if (!opt.isPresent()) {
            outputInventory.setItem(0, ItemStack.EMPTY);
            return;
        }

        FletchingTableRecipe recipe = opt.get();

        ItemStack arrows = inputInventory.getItem(0);
        ItemStack potionStack = inputInventory.getItem(1);

        ItemStack out = recipe.assemble(inputInventory);

        // 1) COUNT
        // - si recipe JSON a count: on respecte
        // - sinon: count = nombre de flèches dans slot0 (comme ton 1.21.1)
        int jsonCount = recipe.getResultCountFromJson();
        int countToSet = (jsonCount > 0) ? jsonCount : arrows.getCount();

        // sécurité : pas > stack max + pas <=0
        countToSet = Math.max(0, Math.min(countToSet, out.getMaxStackSize()));

        // si pas de flèches -> pas d’output
        if (countToSet <= 0) {
            outputInventory.setItem(0, ItemStack.EMPTY);
            return;
        }

        out.setCount(countToSet);

        // 2) POTION -> TIPPED ARROW NBT
        if (!potionStack.isEmpty() && out.getItem() == Items.TIPPED_ARROW) {
            Potion potionType = PotionUtils.getPotion(potionStack);
            if (potionType != null) PotionUtils.setPotion(out, potionType);

            List<EffectInstance> custom = PotionUtils.getCustomEffects(potionStack);
            if (!custom.isEmpty()) PotionUtils.setCustomEffects(out, custom);
        }

        out.getOrCreateTag().putInt("_ft_count", countToSet);

        outputInventory.setItem(0, out);
    }

    void updateResultServer() {
        if (!this.world.isClientSide) {
            updateResult();
            // force sync visuel : ton tag NBT est OK pour 1.16.5
            super.broadcastChanges();
        }
    }

    /*@Override
    public void broadcastChanges() {
        // recalcul serveur (évite boucle)
        if (!this.world.isClientSide) {
            updateResult();
        }
        super.broadcastChanges();
    }*/

    private boolean isArrowInput(World world, ItemStack stack) {
        if (stack.isEmpty()) return false;

        RecipeManager rm = world.getRecipeManager();
        for (IRecipe<?> r : rm.getRecipes()) {
            if (r.getType() == FletchingTableRecipe.TYPE) {
                FletchingTableRecipe fr = (FletchingTableRecipe) r;
                if (fr.getArrowInput().test(stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPotionInput(World world, ItemStack stack) {
        if (stack.isEmpty()) return false;

        RecipeManager rm = world.getRecipeManager();
        for (IRecipe<?> r : rm.getRecipes()) {
            if (r.getType() == FletchingTableRecipe.TYPE) {
                FletchingTableRecipe fr = (FletchingTableRecipe) r;
                if (fr.getPotionInput().test(stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void removed(PlayerEntity player) {
        super.removed(player);
        if (player.level.isClientSide()) return;

        for (int slot = 0; slot < 2; slot++) {
            ItemStack stack = inputInventory.getItem(slot);
            if (stack.isEmpty()) continue;

            ItemStack toGive = stack.copy();
            inputInventory.setItem(slot, ItemStack.EMPTY);

            player.inventory.placeItemBackInInventory(player.level, toGive);
        }
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
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
}
