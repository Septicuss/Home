package oxygen.objects.menu;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import oxygen.Oxygen;

public abstract class Menu {

	private static HashMap<Inventory, Menu> MENU_LIST = new HashMap<Inventory, Menu>();

	private Inventory inventory;
	private List<Button> buttons;
	private boolean updating;

	public Menu(String name, int slots) {
		this.inventory = Bukkit.createInventory(null, slots, name);
		this.buttons = new ArrayList<>();
		this.updating = false;

		MENU_LIST.put(inventory, this);
	}

	public static void closeMenus() {

		List<HumanEntity> viewers = Lists.newArrayList();

		for (Inventory inventory : MENU_LIST.keySet()) {
			viewers.addAll(inventory.getViewers());
		}

		viewers.forEach(viewer -> viewer.closeInventory());
	}

	public void openMenu(Player player) {

		new BukkitRunnable() {

			public void run() {

				try {

					loadIcons(player);

					if (!isUpdating() || getInventory().getViewers().size() == 0) {
						this.cancel();
						return;
					}

				} catch (Exception e) {
					e.printStackTrace();
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Oxygen.getInstance(), 0L, 1L);

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

	public void addButton(Button button) {
		buttons.add(button);
	}

	public Button getButton(int slot) {
		List<Button> foundButtons = buttons.stream().filter(button -> button.containsSlot(slot)).collect(toList());
		return (foundButtons.isEmpty() ? null : foundButtons.get(0));
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public static HashMap<Inventory, Menu> getList() {
		return MENU_LIST;
	}

	public abstract void loadIcons(Player player) throws Exception;

	public abstract void onInventoryClick(Player player, InventoryClickEvent event) throws Exception;

	public abstract void onInventoryClose(Player player, InventoryCloseEvent event);

}
