package home.objects.locations;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import oxygen.objects.Cuboid;
import oxygen.objects.SimpleLocation;

public class HomeArea implements ConfigurationSerializable {

	// - Home Area
	private String id;
	private SimpleLocation firstPoint;
	private SimpleLocation secondPoint;

	// - Constructor
	public HomeArea(String id, SimpleLocation firstPoint, SimpleLocation secondPoint) {
		this.id = id;
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;
	}

	public HomeArea(String id, Cuboid cuboid) {
		this.id = id;
		this.firstPoint = SimpleLocation.from(cuboid.getUpperLocation());
		this.secondPoint = SimpleLocation.from(cuboid.getLowerLocation());
	}

	public HomeArea(String id, Location firstPoint, Location secondPoint) {
		this(id, SimpleLocation.from(firstPoint), SimpleLocation.from(secondPoint));
	}

	// - Methods
	public String getId() {
		return id;
	}

	public SimpleLocation getFirstPoint() {
		return firstPoint;
	}

	public SimpleLocation getSecondPoint() {
		return secondPoint;
	}

	public Cuboid getCuboid() {
		return new Cuboid(firstPoint, secondPoint);
	}

	public World getWorld() {
		return getCuboid().getWorld();
	}

	public boolean containsLocation(Location otherLocation) {
		return getCuboid().containsLocation(otherLocation);
	}

	// - Serialization

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> haMap = new HashMap<>();
		haMap.put("id", id);
		haMap.put("fp", firstPoint.serialize());
		haMap.put("sp", secondPoint.serialize());
		return haMap;
	}

	public static HomeArea deserialize(Map<String, Object> haMap) {
		try {
			String id = (String) haMap.get("id");
			SimpleLocation firstPoint = SimpleLocation.deserialize((Map<String, Object>) haMap.get("fp"));
			SimpleLocation secondPoint = SimpleLocation.deserialize((Map<String, Object>) haMap.get("sp"));
			return new HomeArea(id, firstPoint, secondPoint);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// - Equals
	@Override
	public int hashCode() {
		return Objects.hash(firstPoint, id, secondPoint);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof HomeArea))
			return false;
		HomeArea other = (HomeArea) obj;
		return Objects.equals(firstPoint, other.firstPoint) && Objects.equals(id, other.id)
				&& Objects.equals(secondPoint, other.secondPoint);
	}

}
