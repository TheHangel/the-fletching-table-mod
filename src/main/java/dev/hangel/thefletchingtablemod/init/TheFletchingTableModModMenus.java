
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package dev.hangel.thefletchingtablemod.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.registries.Registries;

import dev.hangel.thefletchingtablemod.world.inventory.FletchingTableGuiMenu;
import dev.hangel.thefletchingtablemod.TheFletchingTableModMod;

public class TheFletchingTableModModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, TheFletchingTableModMod.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<FletchingTableGuiMenu>> FLETCHING_TABLE_GUI = REGISTRY.register("fletching_table_gui", () -> IMenuTypeExtension.create(FletchingTableGuiMenu::new));
}
