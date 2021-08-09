package home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class HomeCommands implements CommandExecutor {
	
	public HomeCommands(JavaPlugin plugin) {
		plugin.getCommand("home").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!sender.isOp()) {
			return true;
		}

		homeCommand(sender, args);
		return true;
	}
	
	private void homeCommand(CommandSender sender, String[] args) {
		
	}

}
