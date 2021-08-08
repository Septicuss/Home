package oxygen.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import oxygen.Oxygen;

public class DatabaseHandler {

	private static Connection connection;

	public DatabaseHandler(JavaPlugin plugin) {
		initialize();
	}

	public void initialize() {
		try {
			File dataFolder = new File(Oxygen.getInstance().getDataFolder(), "data");

			if (!dataFolder.exists())
				dataFolder.mkdirs();

			String databasePath = "jdbc:sqlite:" + dataFolder.getAbsolutePath().replace("\\", "/") + "/data.db";
			connection = DriverManager.getConnection(databasePath);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		return connection;
	}

	public static boolean isConnected() {
		try {
			return ((connection == null || connection.isClosed()) ? false : true);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static void closeConnection() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
