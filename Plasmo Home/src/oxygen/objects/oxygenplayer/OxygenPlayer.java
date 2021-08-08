package oxygen.objects.oxygenplayer;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import oxygen.data.DataContainer;
import oxygen.utils.Items;

public class OxygenPlayer {

	// Variables

	private String name;
	private DataContainer data;

	// Main Getter

	/**
	 * 
	 * Loads the given player from the OxygenPlayerLoader. Depending on whether the
	 * player is already loaded, the player is loaded from the cache. Otherwise from
	 * the database.
	 * 
	 * @param name The players name
	 * @return
	 */
	public static OxygenPlayer getPlayer(String name) {
		OxygenPlayer oPlayer = OxygenPlayerLoader.get(name);
		return oPlayer;
	}

	// Constructors

	public OxygenPlayer(String name, DataContainer data) {
		this.name = name;
		this.data = data;
	}

	public OxygenPlayer(String name) {
		this.name = name;
		this.data = new DataContainer();
	}

	// OxygenPlayer

	public String getName() {
		return this.name;
	}

	public DataContainer getData() {
		return this.data;
	}

	public boolean exists() {
		return this != null;
	}

	/**
	 * Load this OxygenPlayer instance into the local cache to be saved later.
	 */
	public void load() {
		OxygenPlayerLoader.load(this);
	}

	// DataContainer

	public String get(String key) {
		return this.data.get(key);
	}

	public boolean isSet(String key) {
		return this.data.isSet(key);
	}

	public void set(String key, String value) {
		this.data.set(key, value);
		load();
	}

	// Utilities

	public OfflinePlayer getOfflinePlayer() {
		return (this.name == null ? null : Bukkit.getOfflinePlayer(name));
	}

	public Player getPlayer() {
		return (this.name == null ? null : Bukkit.getPlayer(name));
	}

	public void give(ItemStack... items) {
		Player player = getPlayer();

		if (player == null) {
			return;
		}

		for (ItemStack item : items) {
			if (Items.isEmpty(item)) {
				continue;
			}

			HashMap<Integer, ItemStack> map = player.getInventory().addItem(item);
			for (ItemStack unfit : map.values())
				player.getWorld().dropItemNaturally(player.getLocation().add(0, -1, 0), unfit);
		}
	}

	// Object

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof OxygenPlayer)) {
			return false;
		}
		OxygenPlayer other = (OxygenPlayer) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
