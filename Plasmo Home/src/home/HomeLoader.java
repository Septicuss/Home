package home;

import oxygen.Oxygen;

public class HomeLoader {
	
	

	public static void load() {
		
		new HomeCommands(Oxygen.getInstance());

	}

	public static void unload() {

	}

}
