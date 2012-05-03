package de.unistuttgart.ipvs.pmp.resourcegroups.energy.event;

/**
 * This abstract event contains only an identifier (id) and a timestamp
 * 
 * @author Marcus Vetter
 * 
 */
public abstract class AbstractEvent {

	protected int id;
	protected long timestamp;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
