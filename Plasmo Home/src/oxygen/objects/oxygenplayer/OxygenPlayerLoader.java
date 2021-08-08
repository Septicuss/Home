package oxygen.objects.oxygenplayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;

import oxygen.data.DataContainer;
import oxygen.data.DatabaseHandler;

public class OxygenPlayerLoader {

	// Variables
	private static ConcurrentHashMap<String, OxygenPlayer> cache;
	private static Connection connection;

	// Loaders

	public static void load() {
		connection = DatabaseHandler.getConnection();
		cache = new ConcurrentHashMap<>();
		createTable();
	}

	public static void unload() {
		saveAll();
		cache.clear();
		cache = null;
	}

	// Database

	private static void createTable() {
		try {
			String statementString = "CREATE TABLE IF NOT EXISTS players (name TEXT UNIQUE, data TEXT)";

			Statement statement = connection.createStatement();
			statement.execute(statementString);
			statement.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static OxygenPlayer get(String playerName) {
		playerName = playerName.toLowerCase();

		if (cache.containsKey(playerName)) {
			return cache.get(playerName);
		}

		try {
			String statementString = "SELECT * FROM players WHERE name=?";

			PreparedStatement preparedStatement = connection.prepareStatement(statementString);
			preparedStatement.setString(1, playerName);

			ResultSet results = preparedStatement.executeQuery();
			if (results.next()) {

				String name = results.getString("name");
				String data = results.getString("data");

				DataContainer container = DataContainer.deserialize(data);

				OxygenPlayer oPlayer = new OxygenPlayer(name, container);
				load(oPlayer);

				preparedStatement.close();
				return oPlayer;
			}
			preparedStatement.close();

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		OxygenPlayer oPlayer = new OxygenPlayer(playerName);
		load(oPlayer);

		return oPlayer;
	}

	public static void save(OxygenPlayer oPlayer) {
		try {
			if (connection == null || !DatabaseHandler.isConnected()) {
				System.out.println(
						"Attempted to save OxygenPlayer '" + oPlayer.getName() + "' but database was not found.");
				return;
			}

			String name = oPlayer.getName().toLowerCase();
			String data = oPlayer.getData().serialize();

			String statementString = "INSERT INTO players(name,data) VALUES('%s','%2s') ON CONFLICT (name) DO UPDATE SET data=excluded.data";

			Statement statement = connection.createStatement();
			statement.execute(String.format(statementString, name, data));
			statement.close();
		} catch (Exception exception) {
			throw new IllegalStateException("An exception has occured while trying to save OxygenPlayer", exception);
		}
	}

	public static void saveAll() {
		if (cache == null || cache.isEmpty()) {
			return;
		}

		for (OxygenPlayer oPlayer : cache.values()) {
			save(oPlayer);
		}
	}

	public static void load(OxygenPlayer oPlayer) {
		cache.put(oPlayer.getName().toLowerCase(), oPlayer);
	}

}
