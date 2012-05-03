package de.unistuttgart.ipvs.pmp.resourcegroups.energy.event;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class ScreenEvent extends AbstractEvent {

	/**
	 * Attribute of the screen event
	 */
	private boolean changedTo;

	/**
	 * Constructor
	 */
	public ScreenEvent(int id, long timestamp, boolean changedTo) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.changedTo = changedTo;
	}

	/**
	 * @return the changedTo
	 */
	public boolean isChangedTo() {
		return changedTo;
	}

	/**
	 * @param changedTo
	 *            the changedTo to set
	 */
	public void setChangedTo(boolean changedTo) {
		this.changedTo = changedTo;
	}

}
