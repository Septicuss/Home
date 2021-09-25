package home.objects.locations;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import home.Home;
import home.player.session.Session;
import oxygen.Oxygen;
import oxygen.objects.Cuboid;
import oxygen.player.OxygenPlayer;
import oxygen.utilities.DataUtilities;

public class LocationListener implements Listener {

	private LocationService service;

	public LocationListener(Oxygen plugin, LocationService service) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.service = service;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();

		Location location = e.getTo();
		Location locFrom = e.getFrom();
		if (!location.getBlock().equals(locFrom.getBlock()))
			return;

		OxygenPlayer oPlayer = Oxygen.get().getOxygenPlayerService().get(player.getName());

		if (!(oPlayer.get("ignoreBorders") == null))
			return;

		String homeLocationName = oPlayer.get("homeLocation");
		if (!service.locationExists(homeLocationName))
			return;
		HomeLocation homeLoc = service.getLocation(homeLocationName);

		Cuboid border = homeLoc.getBorder();

		if (location.getY() < border.getLowerY()) {
			service.teleport(player, homeLocationName);
		}

		if (!border.containsLocation(location)) {

			Vector corrector = new Vector(0, 0.1, 0);

			Vector velocity = border.vectorFromLocToCuboid(location).add(corrector);
			player.setVelocity(velocity);

		}

	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (setInteractSkipped(event) == false) {
			interactionHandler(event);
		}
	}

	private void interactionHandler(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		Session session = Home.get().getSessionHandler().getSession(player.getName());
		int selectionStatus = Integer.parseInt(session.get("selectionStatus"));

		if (selectionStatus == 1) {
			Location loc1 = e.getClickedBlock().getLocation();
			session.set("loc1", DataUtilities.serializeLocation(loc1));
			session.set("selectionStatus", "2");

			player.sendMessage("Select second border pos");
		}
		if (selectionStatus == 2) {
			Location loc2 = e.getClickedBlock().getLocation();
			session.set("loc2", DataUtilities.serializeLocation(loc2));
			session.set("selectionStatus", "3");

			player.sendMessage("Select spawn pos");
		}
		if (selectionStatus == 3) {
			Location spawnLoc = e.getClickedBlock().getLocation();
			Cuboid border = new Cuboid((Location) DataUtilities.deserializeLocation(session.get("loc1")),
					(Location) DataUtilities.deserializeLocation(session.get("loc2")));

			service.createHomeLocation(session.get("locationName"), border, spawnLoc);

			e.getItem().setAmount(0);

			player.sendMessage("Location successfully created");
			service.teleport(player, session.get("locationName"));
			
			Home.get().getSessionHandler().clearSession(player.getName());
		}
	}

	private boolean setInteractSkipped(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if (!Home.get().getSessionHandler().exists(player.getName()))
			return true;

		Session session = Home.get().getSessionHandler().getSession(player.getName());

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK))
			return true;
		if (e.getItem() == null)
			return true;
		if (!(e.getItem().getType() == Material.BLAZE_ROD))
			return true;
		if (!session.isSet("selectionStatus")) {
			player.sendMessage("You are gay");
			e.getItem().setAmount(0);
			return true;
		}
		if (!(player.isOp()))
			return true;

		return false;
	}

}
