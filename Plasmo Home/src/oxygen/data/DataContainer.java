package oxygen.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

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

	public String get(String key) {
		return data.getOrDefault(key, null);
	}

	public void set(String key, String value) {
		if (value == null) {
			data.remove(key);
			return;
		}

		data.put(key, value);
	}

	public void clear() {
		getData().clear();
	}

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

	public boolean isSet(String key) {
		return data.containsKey(key);
	}

	public HashMap<String, String> getData() {
		return this.data;
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

	// -- Serialization

	public static DataContainer deserialize(String serializedContainer) {
		return (DataContainer) DataUtilities.fromBase64(serializedContainer);
	}

	public String serialize() {
		return DataUtilities.toBase64(this);
	}

}
