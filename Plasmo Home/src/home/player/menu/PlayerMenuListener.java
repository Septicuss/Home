package home.player.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.java.JavaPlugin;

import home.HomeService;
import home.player.session.Session;
import oxygen.Oxygen;
import oxygen.menu.MenuService;

public class PlayerMenuListener implements Listener {

	public PlayerMenuListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent event) {
		event.setCancelled(true);

		Player player = event.getPlayer();
		Session session = HomeService.get().getSessionHandler().getSession(player.getName());

		if (session.isSet("menu-locked"))
			return;

		Oxygen oxygen = Oxygen.get();
		MenuService menuService = oxygen.getMenuService();

		new PlayerMenu(oxygen, menuService).openMenu(player);
	}

}
