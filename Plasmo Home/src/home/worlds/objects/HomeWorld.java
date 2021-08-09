package home.worlds.objects;

import static org.junit.Assert.assertNotNull;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

import home.worlds.generators.HillsGenerator;
import home.worlds.generators.VoidGenerator;
import oxygen.data.DataContainer;
import oxygen.utils.Colors;

public class HomeWorld {

	// -- Enum

	public enum Generator {
		VOID(new VoidGenerator()), HILLS(new HillsGenerator());

		private ChunkGenerator generator;

		Generator(ChunkGenerator generator) {
			this.generator = generator;
		}

		public ChunkGenerator getGenerator() {
			return generator;
		}
	}

	// -- Variables

	private String id;
	private String name;
	private DataContainer container;

	// -- Constructors

	public HomeWorld(String id, String name, DataContainer container, Generator generator) {
		this.id = id;
		this.name = name;
		this.container = container;
		setGenerator(generator);
	}

	public HomeWorld() {
		this.container = new DataContainer();
	}

	// -- Local properties

	public String getId() {
		return this.id;
	}

	public String getName() {
		return Colors.color(this.name);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Generator getGenerator() {
		return Generator.valueOf(this.container.get("generator"));
	}

	public void setGenerator(Generator generator) {
		this.container.set("generator", generator.toString());
	}

	// -- World manipulation

	public World getWorld() {
		return Bukkit.getWorld(id);
	}

	public World generate() {
		Generator generator = getGenerator();
		assertNotNull(generator);

		WorldCreator creator = new WorldCreator(id);
		creator.generator(generator.getGenerator());
		return creator.createWorld();
	}

	// -- Data

	public DataContainer getContainer() {
		return this.container;
	}

	public String getData(String key) {
		return this.container.get(key);
	}

	public void setData(String key, String value) {
		this.container.set(key, value);
	}

	public void setContainer(DataContainer container) {
		this.container = container;
	}

}
