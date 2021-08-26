package home.admin;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import home.HomeService;
import home.commands.HomeCommand;
import home.worlds.WorldHandler;
import oxygen.data.DataContainer;
import oxygen.objects.ColorPalette;
import oxygen.utilities.DataUtilities;
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
			message(sender, p.getFirstColor() + "/...location " + p.getSecondColor() + "set [name]");
			message(sender, p.getFirstColor() + "/...location " + p.getSecondColor() + "remove [name]");
			message(sender, p.getFirstColor() + "/...location " + p.getSecondColor() + "teleport [name] (player)");
			message(sender, p.getFirstColor() + "/...location " + p.getSecondColor() + "list ");
			message(sender, getDivider());
			message(sender, " ");
			return;
		}

		// /admin location set/remove [name]
		if (args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("remove")) {
			if (args.length == 2) {
				message(sender, p.getColor(3) + "[!] Missing [name] argument!");
				return;
			}

			Player player = (Player) sender;

			Location location = player.getLocation();
			String worldName = location.getWorld().getName();

			String locationName = args[2];
			String path = String.format("locations.%s", locationName);

			boolean remove = args[1].equalsIgnoreCase("remove");
			String newValue = (remove ? null : DataUtilities.serializeLocation(location));

			DataContainer container = getWorldContainer(worldName);
			container.set(path, newValue);
			saveWorldContainer(worldName, container);

			String message = p.getFirstColor() + "Location \"%s\" successfully %s.";
			message = String.format(message, locationName, (remove ? "removed" : "set"));

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

			String worldName = player.getWorld().getName();
			String locationName = args[2];
			String path = String.format("locations.%s", locationName);

			DataContainer container = getWorldContainer(worldName);

			if (!container.isSet(path)) {
				message(player, p.getColor(3) + "[!] Location not found!");
				return;
			}

			String serializedLocation = container.get(path);
			Location location = DataUtilities.deserializeLocation(serializedLocation);

			player.teleport(location);
			player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);
			return;
		}

		// /admin location list
		if (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("l")) {
			Player player = (Player) sender;

			message(sender, " ");
			message(sender, getDivider());
			message(sender, p.getFirstColor() + "§lList:");

			DataContainer container = getWorldContainer(player.getWorld().getName());

			if (container.getData().isEmpty()) {
				message(sender, p.getSecondColor() + "- Empty");
				return;
			}

			for (String locationName : container.getData().keySet()) {
				if (locationName == null)
					continue;
				if (!locationName.startsWith("locations."))
					continue;

				message(sender, p.getSecondColor() + "- " + locationName.replaceFirst("locations.", ""));
			}

			return;
		}
	}

	// - Miscellaneous methods
	private DataContainer getWorldContainer(String worldName) {
		WorldHandler worldHandler = HomeService.get().getWorldHandler();
		return worldHandler.getWorldData(worldName);
	}

	private void saveWorldContainer(String worldName, DataContainer container) {
		WorldHandler worldHandler = HomeService.get().getWorldHandler();
		worldHandler.setWorldData(worldName, container);
	}

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
