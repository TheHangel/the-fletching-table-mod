
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package dev.hangel.thefletchingtablemod.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

import dev.hangel.thefletchingtablemod.world.inventory.FletchingTableGuiMenu;
import dev.hangel.thefletchingtablemod.TheFletchingTableModMod;

public class TheFletchingTableModModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TheFletchingTableModMod.MODID);
	public static final RegistryObject<MenuType<FletchingTableGuiMenu>> FLETCHING_TABLE_GUI = REGISTRY.register("fletching_table_gui", () -> IForgeMenuType.create(FletchingTableGuiMenu::new));
}
