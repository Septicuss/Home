package home.worlds.generators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class HillsGenerator extends ChunkGenerator {

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {

		ChunkData chunk = createChunkData(world);

		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		generator.setScale(0.009D);

		int currentHeight;

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				currentHeight = (int) ((generator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.5D, 0.5D, true) + 1) * 15D
						+ 50D);

				chunk.setBlock(x, currentHeight, z, Material.GRASS_BLOCK);
				chunk.setBlock(x, currentHeight - 1, z, Material.DIRT);

				for (int y = currentHeight - 2; y > 0; y--) {
					chunk.setBlock(x, y, z, Material.STONE);
				}

				chunk.setBlock(x, 0, z, Material.BEDROCK);
			}
		}

		return chunk;

	}
}