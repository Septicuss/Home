package home.commands;

import org.bukkit.command.CommandSender;

/**
 * @see CommandHandler
 */
public abstract class HomeCommand {

	public abstract void execute(CommandSender sender, String[] args);

}
