package home.locations;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import oxygen.Oxygen;
import oxygen.data.DataContainer;
import oxygen.objects.Cuboid;
import oxygen.utilities.DataUtilities;
import oxygen.utilities.FileUtilities;

public class LocationService {

	private FileUtilities fileUtilities;
	private HashMap<String, HomeLocation> locations;
	private DataContainer selectionsStatus;

	public LocationService(Oxygen plugin) {

		fileUtilities = plugin.getFileUtilities();
		locations = loadLocations("locations");
		this.selectionsStatus = new DataContainer();
		new LocationListener(plugin, this);

	}

	// getters/setters

	// homeLocs
	public HashMap<String, HomeLocation> getLocations() {
		return locations;
	}

	public void setLocations(HashMap<String, HomeLocation> locs) {
		this.locations = locs;
		saveLocations(locations, "locations");
	}

	// selectionsStatus
	public int getSelectionStatus(Player player) {

		if (selectionsStatus.isSet(player.getName())) {
			int selectionStatus = Integer.parseInt(selectionsStatus.get(player.getName()));
			return selectionStatus;
		} else
			return 0;

	}

	public void setSelectionStatus(Player player, int status) {
		selectionsStatus.set(player.getName(), Integer.toString(status));
	}

	// load/save
	private HashMap<String, HomeLocation> loadLocations(String fileName) {

		FileConfiguration data = fileUtilities.getFileConfiguration(fileName);

		HashMap<String, HomeLocation> locations = new HashMap<>();
		if (!data.isSet("locations"))
			return locations;

		for (String key : data.getConfigurationSection("location").getKeys(false)) {

			String path = String.format("location.%s.", key);

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

	private void saveLocations(HashMap<String, HomeLocation> locs, String fileName) {
		FileConfiguration data = fileUtilities.getFileConfiguration(fileName);

		for (Entry<String, HomeLocation> entry : locs.entrySet()) {
			String key = entry.getKey();
			HomeLocation value = entry.getValue();

			data.set(String.format("location.%s.border.high", key),
					DataUtilities.serializeLocation(value.getBorder().getUpperLocation()));
			data.set(String.format("location.%s.border.low", key),
					DataUtilities.serializeLocation(value.getBorder().getLowerLocation()));
			data.set(String.format("location.%s.spawnpoint", key),
					DataUtilities.serializeLocation(value.getSpawnPoint()));

		}
		fileUtilities.saveFileConfiguration("locations", data);
	}

}
