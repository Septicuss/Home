package home.locations;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import oxygen.Oxygen;
import oxygen.objects.Cuboid;

public class LocationListener implements Listener {

	private LocationService service;
	private HashMap<Player, HashMap<String, Location>> data;

	public LocationListener(Oxygen plugin, LocationService service) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.service = service;
		data = new HashMap<Player, HashMap<String, Location>>();
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {

		Player player = e.getPlayer();
		int selectionStatus = service.getSelectionStatus(player);
		

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;
		
		if (e.getItem() == null)
			return;
		if (!(e.getItem().getType() == Material.BLAZE_ROD))
			return;
		
		if (selectionStatus == 0) {
			player.sendMessage("You are gay");
			e.getItem().setAmount(0);
			return;
		}
		System.out.print(4);
		if (!(player.isOp()))
			return;

		if (selectionStatus == 1) {
			Location loc1 = e.getClickedBlock().getLocation();
			HashMap<String, Location> locs = new HashMap<String, Location>();
			locs.put("loc1", loc1);
			data.put(player, locs);
			
			service.setSelectionStatus(player, 2);
			player.sendMessage("Select second border pos");
		}
		if (selectionStatus == 2) {
			Location loc2 = e.getClickedBlock().getLocation();
			HashMap<String, Location> locs = data.get(player);
			locs.put("loc2", loc2);
			
			service.setSelectionStatus(player, 3);
			player.sendMessage("Select spawn pos");
		}
		if (selectionStatus == 3) {
			Location spawnLoc = e.getClickedBlock().getLocation();
			HashMap<String, Location> locs = data.get(player);

			Cuboid border = new Cuboid(locs.get("loc1"), locs.get("loc2"));
			HomeLocation location = new HomeLocation("test", border, spawnLoc);
			HashMap<String, HomeLocation> locations = service.getLocations();
			locations.put("test", location);
			service.setLocations(locations);

			service.setSelectionStatus(player, 0);
			e.getItem().setAmount(0);
			data.remove(player);
			
			player.sendMessage("Location successfully created");
		}

	}

}
