package home;

import home.commands.CommandHandler;
import oxygen.Oxygen;

/**
 * Register all services used in the "home" package here.
 */
public class HomeService {

	private static HomeService instance;
	private CommandHandler commandHandler;

	public HomeService(Oxygen plugin) {

		instance = this;
		commandHandler = new CommandHandler(plugin);
		
	}

	public static HomeService get() {
		return instance;
	}

	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

}
