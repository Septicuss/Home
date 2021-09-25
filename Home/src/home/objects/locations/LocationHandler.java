package home.objects.locations;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import oxygen.objects.SimpleLocation;
import oxygen.utilities.FileUtilities;

public class LocationHandler {

	// - Variables
	private static final String FILE_NAME = "locations";

	private FileUtilities fileUtilities;

	private ConcurrentHashMap<String, HomeLocation> homeLocations;
	private ConcurrentHashMap<String, HomeArea> homeAreas;

	// - Constructor
	public LocationHandler(FileUtilities fileUtilities) {
		this.fileUtilities = fileUtilities;
		this.homeLocations = new ConcurrentHashMap<>();
		this.homeAreas = new ConcurrentHashMap<>();

		ConfigurationSerialization.registerClass(SimpleLocation.class);
		ConfigurationSerialization.registerClass(HomeLocation.class);
		ConfigurationSerialization.registerClass(HomeArea.class);

	}

	// - File Methods
	public void load() {
		final FileConfiguration locations = fileUtilities.getFileConfiguration(FILE_NAME);
		String configurationPath = null;

		configurationPath = "homelocations";

		if (locations.isSet(configurationPath)) {
			for (String id : locations.getConfigurationSection(configurationPath).getKeys(false)) {
				HomeLocation homeLocation = (HomeLocation) locations.get(configurationPath + "." + id);
				addHomeLocation(homeLocation);
			}
		}

		configurationPath = "homeareas";

		if (locations.isSet(configurationPath)) {
			for (String id : locations.getConfigurationSection(configurationPath).getKeys(false)) {
				HomeArea homeArea = (HomeArea) locations.get(configurationPath + "." + id);
				addHomeArea(homeArea);
			}
		}
	}

	public void save() {
		final FileConfiguration locations = fileUtilities.getFileConfiguration(FILE_NAME);
		clearData(locations);

		String configurationPath = null;

		configurationPath = "homelocations.%s";

		for (HomeLocation homeLocation : homeLocations.values()) {
			String finalConfigurationPath = String.format(configurationPath, homeLocation.getId());
			locations.set(finalConfigurationPath, homeLocation);
		}

		configurationPath = "homeareas.%s";

		for (HomeArea homeArea : homeAreas.values()) {
			String finalConfigurationPath = String.format(configurationPath, homeArea.getId());
			locations.set(finalConfigurationPath, homeArea);
		}

		fileUtilities.saveFileConfiguration(FILE_NAME, locations);
	}

	private void clearData(FileConfiguration config) {
		for (String key : config.getConfigurationSection("").getKeys(false))
			config.set(key, null);
	}

	// - Methods

	public HomeLocation getHomeLocation(final String id) {
		return this.homeLocations.getOrDefault(id, null);
	}

	public boolean hasHomeLocation(final String id) {
		return this.homeLocations.containsKey(id);
	}

	public void addHomeLocation(final HomeLocation homeLocation) {
		this.homeLocations.put(homeLocation.getId(), homeLocation);
	}

	public void removeHomeLocation(final String id) {
		this.homeLocations.remove(id);
	}

	public void removeHomeLocation(final HomeLocation homeLocation) {
		removeHomeLocation(homeLocation.getId());
	}

	// -

	public HomeArea getHomeArea(final String id) {
		return this.homeAreas.getOrDefault(id, null);
	}

	public boolean hasHomeArea(final String id) {
		return this.homeAreas.containsKey(id);
	}

	public void addHomeArea(final HomeArea homeArea) {
		this.homeAreas.put(homeArea.getId(), homeArea);
	}

	public void removeHomeArea(final String id) {
		this.homeAreas.remove(id);
	}

	public void removeHomeArea(final HomeArea homeArea) {
		removeHomeArea(homeArea.getId());
	}

}
