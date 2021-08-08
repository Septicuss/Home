package oxygen.objects.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuListener implements Listener {

	public MenuListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public static void onInventoryClick(InventoryClickEvent event) throws Exception {
		Player player = (Player) event.getWhoClicked();
		Menu menu = Menu.getList().get(event.getView().getTopInventory());

		if (menu == null) {
			return;
		}
		menu.onInventoryClick(player, event);
	}

	@EventHandler
	public static void onInventoryClose(InventoryCloseEvent event) {
		Menu menu = Menu.getList().get(event.getInventory());

		if (menu == null) {
			return;
		}

		menu.onInventoryClose((Player) event.getPlayer(), event);
	}

}
