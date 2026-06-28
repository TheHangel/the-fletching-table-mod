package dev.hangel.thefletchingtablemod.rei;

import me.shedaniel.rei.forge.REIPluginClient;

/**
 * NeoForge entry point for the REI integration.
 *
 * <p>On Fabric, REI discovers plugins through the {@code rei_client} entrypoint in fabric.mod.json.
 * On NeoForge, REI scans for the {@link REIPluginClient} annotation instead, so this thin subclass
 * exposes the shared {@link FletchingTableREIPlugin} (which lives in the common project) to REI.
 *
 * <p>REI only ships a NeoForge build for MC &gt;=26.1.2, so it is declared as an <em>optional</em>
 * dependency in neoforge.mods.toml. When REI is absent (e.g. on 26.1.0/26.1.1) this class is never
 * loaded — REI's annotation scanner is what references it — so it causes no {@code NoClassDefFoundError}.
 */
@REIPluginClient
public class FletchingTableREIPluginNeoForge extends FletchingTableREIPlugin {
}
