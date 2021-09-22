package oxygen.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuListener implements Listener {

	private MenuService service;

	public MenuListener(JavaPlugin plugin, MenuService service) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.service = service;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) throws Exception {
		Player player = (Player) event.getWhoClicked();
		Menu menu = service.getMenus().get(event.getView().getTopInventory());

		if (menu == null) {
			return;
		}
		menu.onInventoryClick(player, event);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) throws Exception {
		Menu menu = service.getMenus().get(event.getInventory());

		if (menu == null) {
			return;
		}

		menu.onInventoryClose((Player) event.getPlayer(), event);
	}

}
