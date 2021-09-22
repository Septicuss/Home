package oxygen.utilities;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import oxygen.data.DataContainer;

public class ItemUtilities {

	private JavaPlugin plugin;

	public ItemUtilities(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	// -- Data

	public void setDataContainer(ItemStack item, DataContainer container) {
		for (Entry<String, String> entry : container.getData().entrySet())
			if (entry.getKey() == null || entry.getValue() == null)
				continue;
			else
				setData(item, entry.getKey(), entry.getValue());
	}

	public void clearData(ItemStack item) {
		ItemMeta itemMeta = item.getItemMeta();
		PersistentDataContainer container = itemMeta.getPersistentDataContainer();

		Set<NamespacedKey> namespacedKeys = container.getKeys();

		for (NamespacedKey key : namespacedKeys)
			container.remove(key);

		item.setItemMeta(itemMeta);
	}

	public HashMap<String, String> getData(ItemStack item) {
		HashMap<String, String> data = new HashMap<>();

		ItemMeta itemMeta = item.getItemMeta();
		PersistentDataContainer container = itemMeta.getPersistentDataContainer();

		Set<NamespacedKey> namespacedKeys = container.getKeys();

		for (NamespacedKey key : namespacedKeys)
			data.put(key.getKey(), container.get(key, PersistentDataType.STRING));

		return data;
	}

	public String getData(ItemStack item, String key) {
		return getData(item).getOrDefault(key, null);
	}

	public void setData(ItemStack item, String key, String value) {
		ItemMeta itemMeta = item.getItemMeta();
		PersistentDataContainer container = itemMeta.getPersistentDataContainer();

		NamespacedKey namespacedKey = getNamespacedKey(key);
		container.set(namespacedKey, PersistentDataType.STRING, value);

		item.setItemMeta(itemMeta);
	}

	public boolean hasData(ItemStack item, String key) {
		if (isEmpty(item))
			return false;

		HashMap<String, String> data = getData(item);

		if (data == null || data.isEmpty())
			return false;

		return data.containsKey(key);
	}

	private NamespacedKey getNamespacedKey(String key) {
		return new NamespacedKey(plugin, key.toLowerCase());
	}

	// -- Methods
	public static boolean isEmpty(ItemStack item) {
		return (item == null || item.getType() == Material.AIR);
	}

}
