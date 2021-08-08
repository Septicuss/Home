package oxygen.objects;

import oxygen.data.DatabaseHandler;
import oxygen.objects.menu.Menu;
import oxygen.objects.oxygenplayer.OxygenPlayerLoader;

public class LoaderHandler {

	public static void load() {
		OxygenPlayerLoader.load();
	}

	public static void unload() {
		Menu.closeMenus();
		OxygenPlayerLoader.unload();
		DatabaseHandler.closeConnection();
	}

}
