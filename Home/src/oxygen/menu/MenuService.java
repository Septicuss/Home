package oxygen.menu;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class MenuService {

	private HashMap<Inventory, Menu> menus;

	public MenuService(JavaPlugin plugin) {
		this.menus = Maps.newHashMap();

		registerListener(plugin, this);
	}

	public HashMap<Inventory, Menu> getMenus() {
		return menus;
	}

	public void registerListener(JavaPlugin plugin, MenuService service) {
		new MenuListener(plugin, service);
	}

	public void closeMenus() {
		Set<HumanEntity> humanEntities = Sets.newHashSet();

		for (Inventory inventory : menus.keySet())
			humanEntities.addAll(inventory.getViewers());

		for (HumanEntity humanEntity : humanEntities)
			humanEntity.closeInventory();
	}

}
