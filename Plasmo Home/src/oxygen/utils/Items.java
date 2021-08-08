package oxygen.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.google.common.collect.Lists;

import net.md_5.bungee.api.ChatColor;
import oxygen.Oxygen;

public class Items {

	public static void addToLore(ItemStack item, String... string) {
		ItemMeta itemMeta = item.getItemMeta();

		List<String> lore = new ArrayList<>();
		if (itemMeta.hasLore())
			lore = itemMeta.getLore();

		lore.addAll(Lists.newArrayList(string));
		itemMeta.setLore(getColoredLore(lore));
		item.setItemMeta(itemMeta);
	}

	public static void clearData(ItemStack item) {
		HashMap<String, String> data = getData(item);

		for (String key : data.keySet()) {
			setData(item, key, null);
		}
	}

	public static void clearLore(ItemStack item) {
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setLore(null);
		item.setItemMeta(itemMeta);
	}

	public static ItemStack create(Material material) {
		return create(material, null);
	}

	public static ItemStack create(Material material, String name, List<String> lore) {

		ItemStack item = new ItemStack(material);
		ItemMeta itemMeta = item.getItemMeta();

		if (name != null)
			itemMeta.setDisplayName(Colors.color(name));

		if (lore != null) {
			itemMeta.setLore(getColoredLore(lore));
		}

		itemMeta.addItemFlags(ItemFlag.values());
		item.setItemMeta(itemMeta);
		return item;
	}

	public static ItemStack create(Material material, String name, String... lore) {

		ItemStack item = new ItemStack(material);
		ItemMeta itemMeta = item.getItemMeta();

		if (name != null)
			itemMeta.setDisplayName(Colors.color(name));

		if (lore != null && !lore[0].equals("")) {
			List<String> newLore = Lists.newArrayList(lore);
			itemMeta.setLore(getColoredLore(newLore));
		}

		itemMeta.addItemFlags(ItemFlag.values());
		item.setItemMeta(itemMeta);

		return item;
	}

	public static ItemStack createSkull(String name, String owner, String... lore) {

		ItemStack item = create(Material.PLAYER_HEAD, name, lore);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
		item.setItemMeta(meta);

		return item;

	}

	public static List<String> getColoredLore(List<String> oldLore) {
		List<String> newLore = Lists.newArrayList();

		for (String line : oldLore) {
			newLore.add(Colors.color(line));
		}

		return newLore;
	}

	public static String getDisplayName(ItemStack item) {
		if (isEmpty(item)) {
			return null;
		}

		if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
			return WordUtils.capitalize(item.getType().toString().toLowerCase().replace("_", " "));
		}

		return item.getItemMeta().getDisplayName();
	}

	public static HashMap<String, String> getData(ItemStack item) {
		HashMap<String, String> data = new HashMap<>();

		ItemMeta itemMeta = item.getItemMeta();
		PersistentDataContainer container = itemMeta.getPersistentDataContainer();

		Set<NamespacedKey> namespacedKeys = container.getKeys();

		for (NamespacedKey key : namespacedKeys)
			data.put(key.getKey(), container.get(key, PersistentDataType.STRING));

		return data;
	}

	public static String getData(ItemStack item, String key) {
		ItemMeta itemMeta = item.getItemMeta();
		NamespacedKey namespacedKey = getNamespacedKey(key);
		return itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
	}

	public static NamespacedKey getNamespacedKey(String key) {
		return new NamespacedKey(Oxygen.getInstance(), key);
	}

	public static boolean hasData(ItemStack item) {
		if (item == null || !item.hasItemMeta() || item.getItemMeta().getPersistentDataContainer().isEmpty())
			return false;

		ItemMeta itemMeta = item.getItemMeta();
		return !itemMeta.getPersistentDataContainer().getKeys().isEmpty();
	}

	public static boolean hasData(ItemStack item, String key) {
		if (item == null || !item.hasItemMeta() || item.getItemMeta().getPersistentDataContainer().isEmpty())
			return false;

		ItemMeta itemMeta = item.getItemMeta();
		NamespacedKey namespacedKey = getNamespacedKey(key);
		return itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING);
	}

	public static ItemStack injectLoreAtIndex(ItemStack item, int index, String... lore) {
		ItemMeta itemMeta = item.getItemMeta();
		List<String> newLore = itemMeta.getLore();
		newLore.addAll(index, getColoredLore(Lists.newArrayList(lore)));
		itemMeta.setLore(newLore);
		item.setItemMeta(itemMeta);

		return item;
	}

	public static boolean isEmpty(ItemStack item) {
		if (item == null || item.getType().isAir())
			return true;

		return false;
	}

	public static void removeLore(ItemStack item, int amount) {
		ItemMeta itemMeta = item.getItemMeta();
		List<String> lore = itemMeta.getLore();

		try {
			for (int i = 0; i < amount; i++)
				lore.remove(lore.size() - 1);
		} catch (Exception e) {
			e.getMessage();
		}

		itemMeta.setLore(getColoredLore(lore));
		item.setItemMeta(itemMeta);
	}

	public static ItemStack setAmount(ItemStack item, int amount) {
		item.setAmount(amount);
		return item;
	}

	public static void setData(ItemStack item, String key, String data) {
		ItemMeta itemMeta = item.getItemMeta();
		NamespacedKey namespacedKey = getNamespacedKey(key);
		if (data == null)
			itemMeta.getPersistentDataContainer().remove(namespacedKey);
		else
			itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, data);
		item.setItemMeta(itemMeta);
	}

	public static ItemStack setDisplayName(ItemStack item, String name) {
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(Colors.color(name));
		item.setItemMeta(itemMeta);

		return item;
	}

	public static List<String> splitLore(String lore) {
		final int defaultLineLimit = 30;
		return splitLore(lore, defaultLineLimit);
	}

	public static List<String> splitLore(String lore, int lineLimit) {
		final List<String> splitLore = Lists.newArrayList();

		final String space = " ";

		String[] splitWords = lore.split(space);
		String lastWord = splitWords[splitWords.length - 1];

		String currentLine = "";
		String nextLineColor = "";

		String fullLine = "";

		for (String word : splitWords) {

			String strippedLine = ChatColor.stripColor(currentLine);
			String strippedWord = ChatColor.stripColor(word);

			boolean overLimit = (strippedLine + space + strippedWord).length() >= lineLimit;

			if (overLimit) {

				splitLore.add(nextLineColor + currentLine.trim());
				fullLine += currentLine;

				if (word.equals(lastWord)) {
					splitLore.add(nextLineColor + word);
				}

				nextLineColor = org.bukkit.ChatColor.getLastColors(fullLine);
				currentLine = word;

				continue;
			}

			currentLine += space + word;

			if (word.equals(lastWord)) {
				splitLore.add(nextLineColor + currentLine.trim());
			}

		}

		return splitLore;

	}

}
