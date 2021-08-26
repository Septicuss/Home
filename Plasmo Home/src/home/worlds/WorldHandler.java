package home.worlds;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;

import home.player.session.Session;
import oxygen.Oxygen;
import oxygen.data.DataContainer;
import oxygen.utilities.BlockUtilities;

public class WorldHandler {

	// [ world name, object ]
	private ConcurrentHashMap<String, Session> worldSessions;

	private BlockUtilities blockUtilities;

	public WorldHandler(Oxygen oxygen) {
		this.worldSessions = new ConcurrentHashMap<>();
		this.blockUtilities = oxygen.getBlockUtilities();
	}

	// - Sessions

	public Session getWorldSession(String worldName) {
		return this.worldSessions.getOrDefault(worldName, new Session());
	}

	public void updateWorldSession(String worldName, Session session) {
		this.worldSessions.put(worldName, session);
	}

	public void clearSession(String worldName) {
		this.worldSessions.remove(worldName);
	}

	// - World Data

	public DataContainer getWorldData(String worldName) {
		Block worldBlock = getWorldBlock(worldName);

		if (worldBlock == null)
			return null;

		PersistentDataContainer pdc = blockUtilities.getBlockPersistentDataContainer(worldBlock);
		return new DataContainer(pdc);
	}

	public void setWorldData(String worldName, DataContainer container) {
		Block worldBlock = getWorldBlock(worldName);

		if (worldBlock == null)
			return;

		PersistentDataContainer pdc = blockUtilities.getBlockPersistentDataContainer(worldBlock);
		container.toPersistentDataContainer(pdc);

		blockUtilities.save(worldBlock, pdc);
	}

	private Block getWorldBlock(String worldName) {
		World world = Bukkit.getWorld(worldName);

		if (world == null)
			return null;

		Block worldBlock = world.getBlockAt(0, 0, 0);

		if (worldBlock.getType() == Material.AIR)
			worldBlock.setType(Material.BEDROCK);

		return worldBlock;
	}

}
