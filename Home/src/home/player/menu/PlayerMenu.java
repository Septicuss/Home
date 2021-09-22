package home.player.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

import oxygen.menu.Menu;
import oxygen.menu.MenuService;

public class PlayerMenu extends Menu {

	public PlayerMenu(JavaPlugin plugin, MenuService service) {
		super(plugin, service, "Меню", 4 * 9);
	}

	@Override
	public void loadIcons(Player player) throws Exception {

	}

	@Override
	public void onInventoryClick(Player player, InventoryClickEvent event) throws Exception {
		event.setCancelled(true);
	}

	@Override
	public void onInventoryClose(Player player, InventoryCloseEvent event) throws Exception {
	}

}
