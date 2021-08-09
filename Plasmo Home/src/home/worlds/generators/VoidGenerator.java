package home.worlds.generators;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

public class VoidGenerator extends ChunkGenerator {

	@Override
	public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z,
			@NotNull BiomeGrid biome) {
		return createChunkData(world);
	}

}
