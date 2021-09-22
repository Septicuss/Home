package oxygen.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import oxygen.Oxygen;
import oxygen.utilities.DataUtilities;

public class DataContainer implements Serializable {

	private static final long serialVersionUID = 1L;
	private HashMap<String, String> data;

	public DataContainer() {
		this.data = new HashMap<>();
	}

	public DataContainer(HashMap<String, String> data) {
		this.data = data;
	}

	public DataContainer(PersistentDataContainer pdc) {
		this.data = new HashMap<>();

		for (NamespacedKey namespacedKey : pdc.getKeys()) {
			String key = namespacedKey.getKey();
			String value = pdc.get(namespacedKey, PersistentDataType.STRING);

			if (key == null || value == null)
				continue;

			data.put(key, value);
		}
	}

	// -- Values

	public DataContainer getByValue(String value) {
		DataContainer otherContainer = new DataContainer();
		for (Entry<String, String> entry : data.entrySet()) {
			if (entry == null || entry.getKey() == null || entry.getValue() == null)
				continue;

			if (entry.getValue().toLowerCase().contains(value.toLowerCase()))
				otherContainer.set(entry.getKey(), entry.getValue());
		}
		return otherContainer;
	}

	public void set(String key, String value) {
		if (value == null) {
			data.remove(key);
			return;
		}

		data.put(key, value);
	}

	public void toPersistentDataContainer(PersistentDataContainer pdc) {
		JavaPlugin plugin = Oxygen.get();

		for (NamespacedKey key : pdc.getKeys()) {
			pdc.remove(key);
		}

		for (Entry<String, String> entry : data.entrySet()) {
			NamespacedKey namespacedKey = getNamespacedKey(plugin, entry.getKey());
			String value = entry.getValue();

			pdc.set(namespacedKey, PersistentDataType.STRING, value);
			System.out.println("To persistent data container \"" + entry.getKey() + "\"");
		}
	}

	private NamespacedKey getNamespacedKey(JavaPlugin plugin, String key) {
		return new NamespacedKey(plugin, key);
	}

	public HashMap<String, String> getData() {
		return this.data;
	}

	public String get(String key) {
		return data.getOrDefault(key, null);
	}

	public void clear() {
		getData().clear();
	}

	public boolean isSet(String key) {
		return data.containsKey(key);
	}

	// -- Object

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataContainer other = (DataContainer) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	// -- Debug
	public List<String> getDebug() {
		List<String> debugMessage = new ArrayList<>();

		debugMessage.add(" ");
		debugMessage.add("-- DataContainer debug --");

		for (Entry<String, String> entry : this.data.entrySet()) {
			debugMessage.add("- " + entry.getKey() + " : " + entry.getValue());
		}

		debugMessage.add("-- End --");

		return debugMessage;
	}

	public void printDebug() {

		System.out.println(" ");
		System.out.println("-- DataContainer debug --");

		for (Entry<String, String> entry : this.data.entrySet()) {
			System.out.println("- " + entry.getKey() + " : " + entry.getValue());
		}

		System.out.println("-- End --");

	}

	// -- Serialization

	public static DataContainer deserialize(String serializedContainer) {
		return (DataContainer) DataUtilities.fromBase64(serializedContainer);
	}

	public String serialize() {
		return DataUtilities.toBase64(this);
	}

}
