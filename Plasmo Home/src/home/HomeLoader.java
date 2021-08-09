package home;

import home.commands.HomeCommands;
import home.worlds.WorldLoader;
import oxygen.Oxygen;

public class HomeLoader {

	public static void load() {

		final Oxygen instance = Oxygen.getInstance();

		// -- Commands & Listeners
		new HomeCommands(instance);

		// -- Handlers & loaders
		WorldLoader.load();
		
	}

	public static void unload() {

	}

}
