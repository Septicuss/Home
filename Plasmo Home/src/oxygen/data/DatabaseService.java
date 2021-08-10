package oxygen.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import org.bukkit.plugin.java.JavaPlugin;

public class DatabaseService {

	private Connection connection;

	public DatabaseService(JavaPlugin plugin) {
		connect(plugin);
	}

	public void connect(JavaPlugin plugin) {
		try {
			if (connection != null) {
				closeConnection();
			}

			File dataFolder = new File(plugin.getDataFolder(), "data");

			if (!dataFolder.exists())
				dataFolder.mkdirs();

			String databasePath = "jdbc:sqlite:" + dataFolder.getAbsolutePath().replace("\\", "/") + "/data.db";
			connection = DriverManager.getConnection(databasePath);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public boolean isConnected() {
		try {
			return ((connection == null || connection.isClosed()) ? false : true);
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}

	public void closeConnection() {
		try {
			connection.close();
			this.connection = null;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public Connection getConnection() {
		return connection;
	}

}
