package oxygen;

import org.bukkit.plugin.java.JavaPlugin;

import oxygen.data.DatabaseHandler;
import oxygen.objects.LoaderHandler;
import oxygen.objects.menu.MenuListener;
import oxygen.utils.Files;

public class Oxygen extends JavaPlugin {

	private static Oxygen instance;

	public void onEnable() {
		instance = this;
		load();
		loadExternal();
	}

	public void onDisable() {
		unloadExternal();
		unload();
	}

	public static void load() {

		// -- Listeners
		new DatabaseHandler(instance);
		new MenuListener(instance);

		// -- Handlers
		LoaderHandler.load();
		Files.load(instance);
	}

	/*
	 * PUT THE EXTERNAL IMPLEMENTATION (OUT OF OXYGEN) LOADING HERE
	 */
	public static void loadExternal() {

	}

	/*
	 * PUT THE EXTERNAL IMPLEMENTATION (OUT OF OXYGEN) UNLOADING HERE
	 */
	public static void unloadExternal() {

	}

	public static void unload() {
		LoaderHandler.unload();
	}

	public static Oxygen getInstance() {
		return instance;
	}
}
