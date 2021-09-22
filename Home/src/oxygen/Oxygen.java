package oxygen;

import org.bukkit.plugin.java.JavaPlugin;

import home.HomeService;
import oxygen.data.DatabaseService;
import oxygen.menu.MenuService;
import oxygen.player.OxygenPlayerService;
import oxygen.utilities.BlockUtilities;
import oxygen.utilities.FileUtilities;
import oxygen.utilities.ItemUtilities;

public class Oxygen extends JavaPlugin {

	// # Services #
	private DatabaseService databaseService;
	private MenuService menuService;
	private OxygenPlayerService oxygenPlayerService;

	// # Utilities #
	private BlockUtilities blockUtilities;
	private FileUtilities fileUtilities;
	private ItemUtilities itemUtilities;

	private static Oxygen oxygen;

	@Override
	public void onEnable() {
		oxygen = this;
		JavaPlugin instance = oxygen;

		// - Services
		databaseService = new DatabaseService(instance);
		menuService = new MenuService(instance);
		oxygenPlayerService = new OxygenPlayerService(databaseService);

		// - Utilities
		blockUtilities = new BlockUtilities(instance);
		fileUtilities = new FileUtilities(instance);
		itemUtilities = new ItemUtilities(instance);

		// - External
		new HomeService(oxygen);
	}

	@Override
	public void onDisable() {
		oxygenPlayerService.saveAll();
		menuService.closeMenus();
		databaseService.closeConnection();
	}

	public static Oxygen get() {
		return oxygen;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public OxygenPlayerService getOxygenPlayerService() {
		return oxygenPlayerService;
	}

	public BlockUtilities getBlockUtilities() {
		return blockUtilities;
	}

	public FileUtilities getFileUtilities() {
		return fileUtilities;
	}

	public ItemUtilities getItemUtilities() {
		return itemUtilities;
	}
}
