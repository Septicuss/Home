package home.player.session;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles local sessions. Used for events and for keeping data, that is not
 * needed after restart. ( For example saving the current cutscene a player is
 * on )
 */
public class SessionHandler {

	// player name, Session
	private ConcurrentHashMap<String, Session> sessions;

	public SessionHandler() {
		this.sessions = new ConcurrentHashMap<>();
	}

	public Session getSession(String playerName) {
		Session session = sessions.getOrDefault(playerName, new Session());
		sessions.put(playerName, session);
		return session;
	}
	
	public boolean isExist(String playerName) {
		return this.sessions.containsKey(playerName);
	}

	public void updateSession(String playerName, Session updatedSession) {
		sessions.put(playerName, updatedSession);
	}

	public void clearSession(String playerName) {
		sessions.remove(playerName);
	}

}
