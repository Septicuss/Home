package home.objects.locations;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Maps;

import oxygen.objects.SimpleLocation;

public class HomeLocation implements ConfigurationSerializable {

	// - Home Location
	private String id;
	private SimpleLocation simpleLocation;

	// - Area
	private String areaId;

	// - Constructors
	public HomeLocation(String id, SimpleLocation simpleLocation) {
		this.id = id;
		this.simpleLocation = simpleLocation;
	}

	public HomeLocation(String id, Location location) {
		this.id = id;
		this.simpleLocation = SimpleLocation.from(location);
	}

	public HomeLocation(String id, SimpleLocation simpleLocation, String areaId) {
		this.id = id;
		this.simpleLocation = simpleLocation;
		this.areaId = areaId;
	}

	// - Methods
	public String getId() {
		return id;
	}

	public SimpleLocation getSimpleLocation() {
		return simpleLocation;
	}

	public Location getLocation() {
		return simpleLocation.getLocation();
	}

	public boolean hasArea() {
		return (areaId != null);
	}

	// - Serialization

	@Override
	public @NotNull Map<String, Object> serialize() {
		final Map<String, Object> hlMap = Maps.newHashMap();
		hlMap.put("id", id);
		hlMap.put("sl", simpleLocation.serialize());
		if (areaId != null)
			hlMap.put("areaId", areaId);
		return hlMap;
	}

	public static HomeLocation deserialize(Map<String, Object> hlMap) {
		try {
			String id = (String) hlMap.get("id");
			SimpleLocation simpleLocation = SimpleLocation.deserialize((Map<String, Object>) hlMap.get("sl"));
			String areaId = (String) hlMap.getOrDefault("areaId", null);

			return new HomeLocation(id, simpleLocation, areaId);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// - Object
	@Override
	public int hashCode() {
		return getLocation().hashCode() + getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof HomeLocation)) {
			return false;
		}
		HomeLocation other = (HomeLocation) obj;
		return other.getId().equals(this.id) && other.getLocation().equals(this.getLocation());
	}

}
