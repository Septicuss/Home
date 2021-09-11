package home.locations;

import org.bukkit.Location;

import oxygen.objects.Cuboid;

public class HomeLocation {

	private Cuboid border;
	private String name;
	private Location spawnPoint;

	public HomeLocation(String name, Cuboid border, Location spawnPoint) {
		this.name = name;
		this.border = border;
		this.spawnPoint = spawnPoint;
	}

	public Cuboid getBorder() {
		return this.border;
	}

	public String getName() {
		return this.name;
	}

	public Location getSpawnPoint() {
		return this.spawnPoint;
	}

}
