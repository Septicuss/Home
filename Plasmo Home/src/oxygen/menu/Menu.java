package oxygen.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Menu {

	// -- Variables

	private static final long DELAY = 0L;
	private static final long UPDATE_PERIOD = 1L;

	private JavaPlugin plugin;
	private Inventory inventory;
	private boolean updating;

	// -- Constructor

	public Menu(JavaPlugin plugin, MenuService service, String name, int slots) {
		this.plugin = plugin;
		this.inventory = Bukkit.createInventory(null, slots, name);
		this.updating = false;

		service.getMenus().put(inventory, this);
	}

	// -- Abstract methods

	public abstract void loadIcons(Player player) throws Exception;

	public abstract void onInventoryClick(Player player, InventoryClickEvent event) throws Exception;

	public abstract void onInventoryClose(Player player, InventoryCloseEvent event) throws Exception;

	// -- Menu

	public void openMenu(Player player) {
		BukkitRunnable updaterTask = new BukkitRunnable() {
			@Override
			public void run() {
				try {
					loadIcons(player);

					if (!isUpdating() || getInventory().getViewers().size() == 0) {
						this.cancel();
					}

				} catch (Exception exception) {
					exception.printStackTrace();
					this.cancel();
				}
			}
		};

		updaterTask.runTaskTimer(plugin, DELAY, UPDATE_PERIOD);
		player.openInventory(inventory);
	}

	public boolean isUpdating() {
		return updating;
	}

	public void setUpdating(boolean updating) {
		this.updating = updating;
	}

	public void addIcon(ItemStack item) {
		this.inventory.addItem(item);
	}

	public void setIcon(int slot, ItemStack item) {
		this.inventory.setItem(slot, item);
	}

	public Inventory getInventory() {
		return this.inventory;
	}

}
