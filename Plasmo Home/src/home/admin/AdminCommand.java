package home.admin;

import org.bukkit.command.CommandSender;

import home.commands.HomeCommand;
import oxygen.objects.ColorPalette;
import oxygen.utilities.MessageUtilities;

public class AdminCommand extends HomeCommand {

	// - Static Palette
	private static final ColorPalette p = new ColorPalette("#7aba49", "#a5a8a3", "#942010");

	// - Main command
	@Override
	public void execute(CommandSender sender, String[] args) {

		if (args.length == 0) {
			sendMessage(sender, " ");
			sendMessage(sender, getDivider());
			sendMessage(sender, p.getFirstColor() + "§lAdmin Help");
			sendMessage(sender, p.getFirstColor() + "/admin " + p.getSecondColor() + "set ...");
			sendMessage(sender, getDivider());
			sendMessage(sender, " ");
			return;
		}

		if (args[0].equalsIgnoreCase("set")) {
			setCommand(sender, args);
			return;
		}

	}

	// - Subcommands
	private void setCommand(CommandSender sender, String[] args) {

		if (args.length == 1) {
			sendMessage(sender, " ");
			sendMessage(sender, getDivider());
			sendMessage(sender, p.getFirstColor() + "§lSet Help");
			sendMessage(sender, p.getFirstColor() + "/...set " + p.getSecondColor() + "location [name]");
			sendMessage(sender, getDivider());
			sendMessage(sender, " ");
			return;
		}

		if (args[1].equalsIgnoreCase("location")) {
			if (args.length == 2) {
				sendMessage(sender, p.getColor(3) + "[!] Missing [name] argument!");
				return;
			}

			String name = args[2];
			sendMessage(sender, name);
			return;
		}

	}

	// - Miscellaneous methods
	private void sendMessage(CommandSender sender, String message) {
		MessageUtilities.sendMessage(sender, message);
	}

	private String getDivider() {
		String divider = "--%s--%s--%s--";
		divider = divider.replace("%s", p.getFirstColor() + "*");
		divider = divider.replace("--", p.getSecondColor() + "---");
		return divider;
	}

}
