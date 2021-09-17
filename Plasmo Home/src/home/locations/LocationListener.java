package home.locations;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import home.HomeService;
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

		String homeLocationName = oPlayer.get("homeLocation");
		HomeLocation homeLoc = service.getLocations().get(homeLocationName);

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
	public void onInteract(PlayerInteractEvent e) {

		Player player = e.getPlayer();

		if (!HomeService.get().getSessionHandler().isExist(player.getName()))
			return;
		Session session = HomeService.get().getSessionHandler().getSession(player.getName());
		int selectionStatus = Integer.parseInt(session.get("selectionStatus"));

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;
		if (e.getItem() == null)
			return;

		if (!(e.getItem().getType() == Material.BLAZE_ROD))
			return;

		if (!session.isSet("selectionStatus")) {
			player.sendMessage("You are gay");
			e.getItem().setAmount(0);
			return;
		}
		if (!(player.isOp()))
			return;

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

			HomeLocation location = new HomeLocation(session.get("locationName"), border, spawnLoc);

			HashMap<String, HomeLocation> locations = service.getLocations();
			locations.put(session.get("locationName"), location);
			service.setLocations(locations);

			e.getItem().setAmount(0);
			HomeService.get().getSessionHandler().clearSession(player.getName());

			player.sendMessage("Location successfully created");
		}

	}

}
