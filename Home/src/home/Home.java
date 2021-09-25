package home;

import home.commands.CommandHandler;
import home.objects.locations.LocationService;
import home.player.menu.PlayerMenuListener;
import home.player.session.SessionHandler;
import home.worlds.WorldHandler;
import oxygen.Oxygen;

/**
 * Register all services used in the "home" package here.
 */
public class Home {

	private static Home instance;
	private CommandHandler commandHandler;
	private SessionHandler sessionHandler;
	private WorldHandler worldHandler;
	private LocationService locationService;

	public Home(Oxygen plugin) {

		instance = this;
		commandHandler = new CommandHandler(plugin);
		sessionHandler = new SessionHandler();
		worldHandler = new WorldHandler(plugin);
		locationService = new LocationService(plugin);

		new PlayerMenuListener(plugin);
	}

	public static Home get() {
		return instance;
	}

	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

	public SessionHandler getSessionHandler() {
		return sessionHandler;
	}

	public WorldHandler getWorldHandler() {
		return worldHandler;
	}

	public LocationService getLocationService() {
		return locationService;
	}

}
