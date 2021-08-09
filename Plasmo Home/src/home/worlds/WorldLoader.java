package home.worlds;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.collect.Sets;

import home.worlds.objects.HomeWorld;
import home.worlds.objects.HomeWorld.Generator;
import oxygen.data.DataContainer;
import oxygen.utils.Files;
import oxygen.utils.Files.FileType;

public class WorldLoader {

	// -- Variables

	private static final Set<HomeWorld> HOMEWORLDS = Sets.newHashSet();

	// -- Class loaders

	public static void load() {
		registerWorld(new HomeWorld("void", "Void!", new DataContainer(), Generator.VOID));

		loadWorlds();
		prepareWorlds();
	}

	public static void unload() {
		saveWorlds();
	}

	// -- World saving & loading

	private static void loadWorlds() {
		final FileConfiguration worldFile = Files.getConfig(FileType.HOMEWORLDS);

		if (!worldFile.isSet("worlds"))
			return;

		for (String worldId : worldFile.getConfigurationSection("worlds").getKeys(false)) {
			HomeWorld world = new HomeWorld();
			DataContainer container = DataContainer.deserialize(worldFile.getString("worlds." + worldId + ".data"));

			world.setId(worldId);
			world.setName(worldFile.getString("worlds." + worldId + ".name"));
			world.setContainer(container);
			world.setGenerator(Generator.valueOf(container.get("generator")));
			HOMEWORLDS.add(world);
		}
	}

	private static void saveWorlds() {
		final FileConfiguration worldFile = Files.getConfig(FileType.HOMEWORLDS);

		worldFile.set("worlds", null);

		for (HomeWorld world : HOMEWORLDS)
			writeWorld(worldFile, world);

		Files.saveConfig(FileType.HOMEWORLDS, worldFile);
	}

	private static void writeWorld(FileConfiguration fileConfig, HomeWorld world) {
		String path = "worlds.%s.%s";
		fileConfig.set(String.format(path, world.getId(), "name"), world.getName());
		fileConfig.set(String.format(path, world.getId(), "data"), world.getContainer().serialize());
	}

	// -- Generation & setup

	private static void prepareWorlds() {
		for (HomeWorld world : HOMEWORLDS) {
			if (world.getGenerator() == null) {
				out("Couldn't find a generator for world " + world.getName());
				continue;
			}

			if (Bukkit.getWorld(world.getId()) != null) {
				out("World " + world.getId() + " is already generated. Skipping.");
				continue;
			}

			out("Generating a world " + world.getName());
			world.generate();
		}
	}

	// -- Registration & api

	public static void registerWorld(HomeWorld world) {
		HOMEWORLDS.add(world);
	}

	public static void unregisterWorld(HomeWorld world) {
		HOMEWORLDS.remove(world);
	}

	public static HomeWorld getHomeWorld(String worldName) {
		for (HomeWorld world : HOMEWORLDS)
			if (world.getId().equals(worldName))
				return world;
		return null;
	}

	// -- Utilities

	private static void out(String str) {
		System.out.println("[Plasmo Home] " + str);
	}
}
