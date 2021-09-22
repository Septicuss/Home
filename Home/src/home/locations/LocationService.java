package home.locations;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import oxygen.Oxygen;
import oxygen.objects.Cuboid;
import oxygen.player.OxygenPlayer;
import oxygen.utilities.DataUtilities;
import oxygen.utilities.FileUtilities;

public class LocationService {

	private FileUtilities fileUtilities;
	private HashMap<String, HomeLocation> locations;
	private String fileName;

	public LocationService(Oxygen plugin) {

		fileUtilities = plugin.getFileUtilities();
		fileName = "locations";
		locations = loadLocations();
		new LocationListener(plugin, this);

	}

	// getters/setters

	// homeLocs
	public HashMap<String, HomeLocation> getLocations() {
		return locations;
	}

	public void setLocations(HashMap<String, HomeLocation> locs) {
		this.locations = locs;
		saveLocations(locations);
	}

	public void removeLocation(String locName) {
		this.locations.remove(locName);
		removeLocFromData(locName);
	}

	// interact with player
	public void teleport(Player player, String homeLocationName) {
		if (!isExist(homeLocationName))
			return;

		HomeLocation homeLoc = locations.get(homeLocationName);
		Location location = homeLoc.getSpawnPoint();
		player.teleport(location);

		OxygenPlayer oPlayer = Oxygen.get().getOxygenPlayerService().get(player.getName());
		oPlayer.set("homeLocation", homeLocationName);
		Oxygen.get().getOxygenPlayerService().save(oPlayer, true);
	}

	// utils
	public boolean isExist(String homeLocationName) {
		return this.locations.containsKey(homeLocationName);
	}

	// load/save
	private HashMap<String, HomeLocation> loadLocations() {

		FileConfiguration data = fileUtilities.getFileConfiguration(fileName);

		HashMap<String, HomeLocation> locations = new HashMap<>();
		if (!data.isSet("locations"))
			return locations;

		for (String key : data.getConfigurationSection("locations").getKeys(false)) {

			String path = String.format("locations.%s.", key);

			String serializedUpperLocation = data.getString(path + "border.high");
			Location upperLocation = DataUtilities.deserializeLocation(serializedUpperLocation);
			String serializedLowerLocation = data.getString(path + "border.low");
			Location lowerLocation = DataUtilities.deserializeLocation(serializedLowerLocation);

			String serializedSpawnPoint = data.getString(path + "spawnpoint");
			Location spawnPoint = DataUtilities.deserializeLocation(serializedSpawnPoint);

			Cuboid border = new Cuboid(lowerLocation, upperLocation);
			HomeLocation homeloc = new HomeLocation(key, border, spawnPoint);

			locations.put(key, homeloc);
		}
		return locations;
	}

	private void saveLocations(HashMap<String, HomeLocation> locs) {
		FileConfiguration data = fileUtilities.getFileConfiguration(fileName);

		for (Entry<String, HomeLocation> entry : locs.entrySet()) {
			String key = entry.getKey();
			HomeLocation value = entry.getValue();

			data.set(String.format("locations.%s.border.high", key),
					DataUtilities.serializeLocation(value.getBorder().getUpperLocation()));
			data.set(String.format("locations.%s.border.low", key),
					DataUtilities.serializeLocation(value.getBorder().getLowerLocation()));
			data.set(String.format("locations.%s.spawnpoint", key),
					DataUtilities.serializeLocation(value.getSpawnPoint()));

		}
		fileUtilities.saveFileConfiguration("locations", data);
	}

	private void removeLocFromData(String locName) {

		FileConfiguration data = fileUtilities.getFileConfiguration(fileName);
		data.getConfigurationSection("locations").set(locName, null);
		fileUtilities.saveFileConfiguration("locations", data);

	}

}
