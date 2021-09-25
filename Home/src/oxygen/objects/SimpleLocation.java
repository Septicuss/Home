package oxygen.objects;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Maps;

import oxygen.utilities.DataUtilities;

public class SimpleLocation implements ConfigurationSerializable {

	// - Simple Location
	private String worldName;

	private double x;
	private double y;
	private double z;

	private float yaw;
	private float pitch;

	// - Constructors
	public SimpleLocation(Location location) {
		this(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(),
				location.getPitch());
	}

	public SimpleLocation(String world, double x, double y, double z, float yaw, float pitch) {
		this.worldName = world;
		this.x = DataUtilities.round(x, 3);
		this.y = DataUtilities.round(y, 3);
		this.z = DataUtilities.round(z, 3);
		this.yaw = (float) DataUtilities.round(yaw, 3);
		this.pitch = (float) DataUtilities.round(pitch, 3);
	}

	// - Methods
	public Location getLocation() {
		World world = Bukkit.getWorld(worldName);
		if (world == null)
			return null;
		return new Location(world, x, y, z, yaw, pitch);
	}

	public String getWorldName() {
		return worldName;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

	// - Static
	public static SimpleLocation from(Location location) {
		return new SimpleLocation(location);
	}

	// - Serialization
	@Override
	public @NotNull Map<String, Object> serialize() {
		final Map<String, Object> slMap = Maps.newHashMap();
		slMap.put("loc", DataUtilities.serializeSimpleLocation(this));
		return slMap;
	}

	public static SimpleLocation deserialize(Map<String, Object> slMap) {
		try {
			String serializedLocation = (String) slMap.get("loc");
			return DataUtilities.deserializeSimpleLocation(serializedLocation);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// - Object
	@Override
	public int hashCode() {
		return getLocation().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SimpleLocation)) {
			return false;
		}
		SimpleLocation other = (SimpleLocation) obj;
		return other.getLocation().equals(this.getLocation());
	}

}
