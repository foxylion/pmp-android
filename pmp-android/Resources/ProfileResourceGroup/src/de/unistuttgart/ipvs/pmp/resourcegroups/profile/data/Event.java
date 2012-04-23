package de.unistuttgart.ipvs.pmp.resourcegroups.profile.data;

public class Event {
	
	private int id;
	private long timestamp;
	private int type;
	private int special;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getSpecial() {
		return special;
	}
	
	public void setSpecial(int special) {
		this.special = special;
	}

}
