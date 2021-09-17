package home.admin;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import home.HomeService;
import home.commands.HomeCommand;
import home.locations.HomeLocation;
import home.locations.LocationService;
import oxygen.Oxygen;
import oxygen.objects.ColorPalette;
import oxygen.player.OxygenPlayer;
import oxygen.utilities.MessageUtilities;

public class AdminCommand extends HomeCommand {

	// - Static Palette
	private static final ColorPalette p = new ColorPalette("#7aba49", "#a5a8a3", "#942010");

	// - Main command
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof Player)
			((Player) sender).playSound(((Player) sender).getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 1f, 1f);

		if (args.length == 0) {
			message(sender, " ");
			message(sender, getDivider());
			message(sender, p.getFirstColor() + "§lAdmin Help");
			message(sender, p.getFirstColor() + "/admin " + p.getSecondColor() + "location ...");
			message(sender, p.getFirstColor() + "/admin " + p.getSecondColor() + "world ...");
			message(sender, getDivider());
			message(sender, " ");
			return;
		}

		if (args[0].equalsIgnoreCase("location") || args[0].equalsIgnoreCase("loc")) {
			locationCommand(sender, args);
			return;
		}

		if (args[0].equalsIgnoreCase("world") || args[0].equalsIgnoreCase("w")) {
			worldCommand(sender, args);
			return;
		}
	}

	// - Subcommands

	/**
	 * /admin world...
	 */
	private void worldCommand(CommandSender sender, String[] args) {
		if (args.length == 1) {
			message(sender, " ");
			message(sender, getDivider());
			message(sender, p.getFirstColor() + "§lWorld Help");
			message(sender, p.getFirstColor() + "/...world " + p.getSecondColor() + "teleport [name] (player)");
			message(sender, p.getFirstColor() + "/...world " + p.getSecondColor() + "list");
			message(sender, getDivider());
			message(sender, " ");
			return;
		}

		// /admin world teleport [name]
		if (args[1].equalsIgnoreCase("teleport") || args[1].equalsIgnoreCase("tp")) {
			if (args.length == 2) {
				message(sender, p.getColor(3) + "[!] Missing [name] argument!");
				return;
			}

			String playerName = args.length >= 4 ? args[3] : sender.getName();

			if (Bukkit.getPlayer(playerName) == null) {
				message(sender, p.getColor(3) + "[!] Player not found!");
				return;
			}

			Player player = Bukkit.getPlayer(playerName);
			String worldName = args[2];

			if (Bukkit.getWorld(worldName) == null) {
				message(sender, p.getColor(3) + "[!] World not found!");
				return;
			}

			World world = Bukkit.getWorld(worldName);
			Block teleportBlock = world.getHighestBlockAt(0, 0);

			if (teleportBlock == null || teleportBlock.getType().isAir()) {
				teleportBlock = world.getBlockAt(0, 63, 0);
				teleportBlock.setType(Material.GLASS);
			}

			Location teleportLocation = teleportBlock.getLocation().add(0.5, 1, 0.5);

			player.teleport(teleportLocation);
			player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);
			return;
		}

		// /admin world list
		if (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("l")) {
			message(sender, " ");
			message(sender, getDivider());
			message(sender, p.getFirstColor() + "§lList:");

			List<World> worlds = Bukkit.getWorlds();
			for (World world : worlds) {
				message(sender, p.getSecondColor() + "- " + world.getName());
			}

			return;
		}
	}

	/**
	 * /admin location ...
	 */
	private void locationCommand(CommandSender sender, String[] args) {
		if (args.length == 1) {
			message(sender, " ");
			message(sender, getDivider());
			message(sender, p.getFirstColor() + "§lLocation Help [These commands are world specific!]");
			message(sender, p.getFirstColor() + "/...location " + p.getSecondColor() + "create [name]");
			message(sender, p.getFirstColor() + "/...location " + p.getSecondColor() + "remove [name]");
			message(sender, p.getFirstColor() + "/...location " + p.getSecondColor() + "teleport [name] (player)");
			message(sender, p.getFirstColor() + "/...location " + p.getSecondColor() + "list ");
			message(sender, getDivider());
			message(sender, " ");
			return;
		}

		// /admin location create [name]
		if (args[1].equalsIgnoreCase("create")) {
			if (args.length == 2) {
				message(sender, p.getColor(3) + "[!] Missing [name] argument!");
				return;
			}
			Player player = (Player) sender;
			OxygenPlayer oxygenPlayer = new OxygenPlayer(player.getName());
			String locationName = args[2];
			HomeService.get().getSessionHandler().getSession(player.getName()).set("selectionStatus", "1");
			HomeService.get().getSessionHandler().getSession(player.getName()).set("locationName", locationName);

			ItemStack selectionItem = new ItemStack(Material.BLAZE_ROD);

			oxygenPlayer.give(selectionItem);

			String message = p.getFirstColor() + "Select first border pos";

			message(player, message);
			return;
		}

		// /admin location remove [name]
		if (args[1].equalsIgnoreCase("remove")) {
			if (args.length == 2) {
				message(sender, p.getColor(3) + "[!] Missing [name] argument!");
				return;
			}
			Player player = (Player) sender;
			String locationName = args[2];

			HashMap<String, HomeLocation> locations = HomeService.get().getLocationService().getLocations();
			if (!locations.containsKey(locationName)) {
				message(player, p.getColor(3) + "[!] Location not found!");
				return;
			}

			HomeService.get().getLocationService().removeLocation(locationName);

			String message = p.getFirstColor() + "Location successfuly removed";

			message(player, message);
			return;
		}

		// /admin location teleport [name] (player)
		if (args[1].equalsIgnoreCase("teleport") || args[1].equalsIgnoreCase("tp")) {
			if (args.length == 2) {
				message(sender, p.getColor(3) + "[!] Missing [name] argument!");
				return;
			}

			String playerName = args.length >= 4 ? args[3] : sender.getName();

			if (Bukkit.getPlayer(playerName) == null) {
				message(sender, p.getColor(3) + "[!] Player not found!");
				return;
			}

			Player player = Bukkit.getPlayer(playerName);

			String locationName = args[2];

			LocationService locService = HomeService.get().getLocationService();

			if (!locService.isExist(locationName)) {
				message(player, p.getColor(3) + "[!] Location not found!");
				return;
			}

			locService.teleport(player, locationName);
			player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);
			return;
		}

		// /admin location list
		if (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("l")) {

			message(sender, " ");
			message(sender, getDivider());
			message(sender, p.getFirstColor() + "§lList:");

			HashMap<String, HomeLocation> locations = HomeService.get().getLocationService().getLocations();

			if (locations.isEmpty()) {
				message(sender, p.getSecondColor() + "- Empty");
				return;
			}

			for (String locationName : locations.keySet()) {
				if (locationName == null)
					continue;

				message(sender, p.getSecondColor() + "- " + locationName);
			}

			return;
		}
		// /admin location ignoreBorders
		if (args[1].equalsIgnoreCase("ignoreBorders")) {

			Player player = (Player) sender;
			OxygenPlayer oPlayer = Oxygen.get().getOxygenPlayerService().get(player.getName());

			if (oPlayer.get("ignoreBorders") == null) {
				oPlayer.set("ignoreBorders", "1");
				message(sender, p.getFirstColor() + "now borders dont pushes you");
			}
			else {
				oPlayer.set("ignoreBorders", null);
				message(sender, p.getFirstColor() + "now borders pushes you");
			}


			return;
		}
	}

	// - Miscellaneous methods

	private void message(CommandSender sender, String message) {
		MessageUtilities.sendMessage(sender, message);
	}

	private String getDivider() {
		String divider = "--%s--%s--%s--";
		divider = divider.replace("%s", p.getFirstColor() + "*");
		divider = divider.replace("--", p.getSecondColor() + "---");
		return divider;
	}

}
