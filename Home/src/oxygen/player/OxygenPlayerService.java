package oxygen.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;

import oxygen.data.DataContainer;
import oxygen.data.DatabaseService;

public class OxygenPlayerService {

	// Variables

	private ConcurrentHashMap<String, OxygenPlayer> oxygenPlayerCache;
	private DatabaseService databaseService;
	private Connection connection;

	// Constructor

	public OxygenPlayerService(DatabaseService databaseService) {
		this.oxygenPlayerCache = new ConcurrentHashMap<>();
		this.databaseService = databaseService;
		this.connection = databaseService.getConnection();

		createTable();
	}

	// Database

	private void createTable() {
		try {
			String statementString = "CREATE TABLE IF NOT EXISTS players (name TEXT UNIQUE, data TEXT)";

			Statement statement = connection.createStatement();
			statement.execute(statementString);
			statement.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public OxygenPlayer get(String playerName) {
		playerName = playerName.toLowerCase();

		if (oxygenPlayerCache.containsKey(playerName)) {
			return oxygenPlayerCache.get(playerName);
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
				save(oPlayer, true);

				preparedStatement.close();
				return oPlayer;
			}
			preparedStatement.close();

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		OxygenPlayer oPlayer = new OxygenPlayer(playerName);
		save(oPlayer, true);

		return oPlayer;
	}

	public void save(OxygenPlayer oPlayer, boolean cache) {

		if (cache) {
			oxygenPlayerCache.put(oPlayer.getName().toLowerCase(), oPlayer);
			return;
		}

		try {
			if (connection == null || !databaseService.isConnected()) {
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

	public void saveAll() {
		if (oxygenPlayerCache == null || oxygenPlayerCache.isEmpty()) {
			return;
		}

		for (OxygenPlayer oPlayer : oxygenPlayerCache.values()) {
			save(oPlayer, false);
		}
	}

}
