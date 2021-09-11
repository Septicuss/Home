package home.locations;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import oxygen.Oxygen;
import oxygen.data.DataContainer;
import oxygen.utilities.FileUtilities;

public class LocationService {

	private FileUtilities fileUtilities;
	private FileConfiguration data;
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
		saveLocations(locations);
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

		this.data = fileUtilities.getFileConfiguration(fileName);

		if (!data.isSet("locations"))
			return new HashMap<String, HomeLocation>();

		@SuppressWarnings("unchecked")
		HashMap<String, HomeLocation> locs = (HashMap<String, HomeLocation>) data.get("locations");

		return locs;
	}

	private void saveLocations(HashMap<String, HomeLocation> locs) {
		data.set("locations", locs);
		fileUtilities.saveFileConfiguration("locations", data);
	}

}
