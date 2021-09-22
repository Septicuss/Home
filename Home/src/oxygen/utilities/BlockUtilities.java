package oxygen.utilities;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockUtilities {

	private JavaPlugin plugin;

	public BlockUtilities(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	// Setting local data

	public String getData(Block block, String key) {
		if (isEmpty(block)) {
			clearData(block);
			return null;
		}

		final PersistentDataContainer blockContainer = getBlockPersistentDataContainer(block);
		final NamespacedKey namespacedKey = getNamespacedKey(block);

		if (blockContainer.isEmpty() || !blockContainer.has(namespacedKey, PersistentDataType.STRING)) {
			return null;
		}

		return blockContainer.get(namespacedKey, PersistentDataType.STRING);
	}

	public void setData(Block block, String key, String value) {
		final PersistentDataContainer blockContainer = getBlockPersistentDataContainer(block);
		final NamespacedKey namespacedKey = getNamespacedKey(block);

		if (value == null) {
			blockContainer.remove(namespacedKey);
		} else {
			blockContainer.set(namespacedKey, PersistentDataType.STRING, value);
		}

		save(block, blockContainer);
	}

	public void save(Block block, PersistentDataContainer blockContainer) {
		final PersistentDataContainer chunkContainer = block.getChunk().getPersistentDataContainer();
		final NamespacedKey key = getNamespacedKey(block);

		if (blockContainer.isEmpty()) {
			chunkContainer.remove(key);
		} else {
			chunkContainer.set(key, PersistentDataType.TAG_CONTAINER, blockContainer);
		}
	}

	public PersistentDataContainer getBlockPersistentDataContainer(Block block) {
		final PersistentDataContainer chunkContainer = block.getChunk().getPersistentDataContainer();
		final PersistentDataContainer blockContainer;

		final NamespacedKey key = getNamespacedKey(block);

		if (chunkContainer.has(key, PersistentDataType.TAG_CONTAINER)) {
			blockContainer = chunkContainer.get(key, PersistentDataType.TAG_CONTAINER);
			assert blockContainer != null;
			return blockContainer;
		}

		blockContainer = chunkContainer.getAdapterContext().newPersistentDataContainer();
		chunkContainer.set(key, PersistentDataType.TAG_CONTAINER, blockContainer);

		return blockContainer;
	}

	public void clearData(Block block) {
		final PersistentDataContainer blockContainer = getBlockPersistentDataContainer(block);
		blockContainer.getKeys().forEach(key -> blockContainer.remove(key));
		save(block, blockContainer);
	}

	private NamespacedKey getNamespacedKey(Block block) {
		final int x = block.getX() & 0x000F;
		final int y = block.getY() & 0x00FF;
		final int z = block.getZ() & 0x000F;
		return new NamespacedKey(plugin, String.format("x%dy%dz%d", x, y, z));
	}

	public boolean isEmpty(Block block) {
		if (block == null || block.getType().isAir()) {
			return true;
		}
		return false;
	}

	public boolean hasData(Block block) {
		return !getBlockPersistentDataContainer(block).isEmpty();
	}

	public boolean hasData(Block block, String key) {
		return getData(block, key) != null;
	}

}
