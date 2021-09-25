package home.objects.portals;

import org.bukkit.Location;

public class Portal {

	private Location position;
	private String homeLocationName;
	private boolean withDoor;

	public Portal(Location position, String homeLocationName, boolean withDoor) {
		this.position = position;
		this.homeLocationName = homeLocationName;
		this.withDoor = withDoor;
	}

	public Location getPosition() {
		return position;
	}

	public String getHomeLocationName() {
		return homeLocationName;
	}

	public void setHomeLocationName(String homeLocationName) {
		this.homeLocationName = homeLocationName;
	}

	public boolean isWithDoor() {
		return withDoor;
	}

}
