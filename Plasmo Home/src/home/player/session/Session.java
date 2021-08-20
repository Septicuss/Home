package home.player.session;

import oxygen.data.DataContainer;

public class Session {

	private DataContainer container;

	public Session() {
		this.container = new DataContainer();
	}

	public Session(DataContainer container) {
		this.container = container;
	}

	public DataContainer getContainer() {
		return this.container;
	}

	public void setContainer(DataContainer container) {
		this.container = container;
	}

	public String get(String key) {
		return this.container.get(key);
	}

	public void set(String key, String value) {
		this.container.set(key, value);
	}

	public boolean isSet(String key) {
		return this.container.isSet(key);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((container == null) ? 0 : container.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Session)) {
			return false;
		}
		Session other = (Session) obj;
		if (container == null) {
			if (other.container != null) {
				return false;
			}
		} else if (!container.equals(other.container)) {
			return false;
		}
		return true;
	}
	
	
	
}
