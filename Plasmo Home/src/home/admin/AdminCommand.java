package home.admin;

import org.bukkit.command.CommandSender;

import home.commands.HomeCommand;
import oxygen.utilities.MessageUtilities;

public class AdminCommand extends HomeCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			MessageUtilities.sendMessage(sender, "§c§lHelp");
			return;
		}

		MessageUtilities.sendMessage(sender, "§c§llol");

	}

}
