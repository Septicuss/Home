package home;

import home.commands.CommandHandler;
import home.objects.locations.LocationHandler;
import home.player.menu.PlayerMenuListener;
import home.player.session.SessionHandler;
import home.worlds.WorldHandler;
import oxygen.Oxygen;

/**
 * Register all services used in the "home" package here.
 */
public class Home {

	private static Home instance;

	private Oxygen plugin;

	private CommandHandler commandHandler;
	private SessionHandler sessionHandler;
	private WorldHandler worldHandler;
	private LocationHandler locationHandler;

	public Home(Oxygen plugin) {
		instance = this;
		this.plugin = plugin;
	}

	public void load() {
		commandHandler = new CommandHandler(plugin);
		sessionHandler = new SessionHandler();
		worldHandler = new WorldHandler(plugin);
		locationHandler = new LocationHandler(plugin.getFileUtilities());

		new PlayerMenuListener(plugin);

		locationHandler.load();
	}

	public void unload() {
		locationHandler.save();
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

	public LocationHandler getLocationHandler() {
		return locationHandler;
	}

}
