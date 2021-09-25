package home.objects.locations;

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

/**
 * A service responsible for managing HomeLocations, teleportation and data
 * handling.
 */
public class LocationServiceOld {

	private static final String fileName = "locations";
	private FileUtilities fileUtilities;
	private HashMap<String, HomeLocation> locations;

	public LocationServiceOld(Oxygen plugin) {

		fileUtilities = plugin.getFileUtilities();
		locations = loadLocations();
		new LocationListenerOld(plugin, this);

	}

	// HOME LOCATIONS
	
	public void createHomeLocation(String name, Cuboid border, Location spawnPoint){
		HomeLocation homeLocation = new HomeLocation(name, border, spawnPoint);
		this.locations.put(homeLocation.getName(), homeLocation);
		saveLocations(locations);
	}

	public void removeLocation(String homeLocationName) {
		this.locations.remove(homeLocationName);
		deleteLocation(homeLocationName);
	}

	public HomeLocation getLocation(String homeLocationName) {
		return locations.get(homeLocationName);
	}

	public boolean locationExists(String homeLocationName) {
		return this.locations.containsKey(homeLocationName);
	}
	
	public HashMap<String, HomeLocation> getLocations() {
		@SuppressWarnings("unchecked")
		HashMap<String, HomeLocation> clone = (HashMap<String, HomeLocation>) locations.clone();
		return clone;
	}

	// interact with player

	public void teleport(Player player, String homeLocationName) {
		if (!locationExists(homeLocationName))
			return;

		HomeLocation homeLoc = locations.get(homeLocationName);
		Location location = homeLoc.getSpawnPoint();
		player.teleport(location);

		OxygenPlayer oPlayer = Oxygen.get().getOxygenPlayerService().get(player.getName());
		oPlayer.set("homeLocation", homeLocationName);
		Oxygen.get().getOxygenPlayerService().save(oPlayer, true);
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

	private void deleteLocation(String locName) {

		FileConfiguration data = fileUtilities.getFileConfiguration(fileName);
		data.getConfigurationSection("locations").set(locName, null);
		fileUtilities.saveFileConfiguration("locations", data);

	}

}
