package oxygen.objects;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import oxygen.Oxygen;
import oxygen.data.DataContainer;

public class ItemBuilder {

	private Material material;
	private String name;
	private List<String> lore;
	private int model;
	private DataContainer data;
	private int amount;

	public ItemBuilder() {
		this.material = null;
		this.name = null;
		this.lore = Lists.newArrayList();
		this.model = -1;
		this.data = new DataContainer();
		this.amount = 1;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public void addLore(String line) {
		this.lore.add(line);
	}

	public void setModel(int model) {
		this.model = model;
	}

	public void setData(String key, String value) {
		this.data.set(key, value);
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public ItemStack build() {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();

		if (name != null)
			meta.setDisplayName(name);

		if (lore != null && !lore.isEmpty())
			meta.setLore(lore);

		if (model > 0)
			meta.setCustomModelData(model);

		item.setItemMeta(meta);

		if (!data.getData().isEmpty())
			Oxygen.get().getItemUtilities().setDataContainer(item, data);

		item.setAmount(amount);
		return item;
	}

}
