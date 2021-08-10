package home.commands;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.google.common.collect.Maps;

import home.admin.AdminCommand;
import oxygen.Oxygen;

/**
 * Class used for registering HomeCommands. To register your own command, create
 * a class that extends {@link HomeCommand}, create an instance in
 * {@link CommandHandler} constructor and add it to the command map.
 * 
 * (Remember to also add your command to plugin.yml)
 */
public class CommandHandler implements CommandExecutor {

	private Map<String, HomeCommand> commandMap;

	public CommandHandler(Oxygen plugin) {

		this.commandMap = Maps.newHashMap();

		// - Initialize HomeCommand objects
		HomeCommand adminCommand = new AdminCommand();

		// - Add HomeCommand object to commandMap
		commandMap.put("admin", adminCommand);

		for (Entry<String, HomeCommand> entry : commandMap.entrySet()) {
			plugin.getCommand(entry.getKey()).setExecutor(this);
		}

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		String commandName = command.getName();

		if (commandMap.containsKey(commandName))
			commandMap.get(commandName).execute(sender, args);

		return true;
	}

}
