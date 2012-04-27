package de.unistuttgart.ipvs.pmp.resourcegroups.profile.data;

public class Profile {
	
    public int ring;
    public int apps;
    public int contacts;
	
    public int getRing() {
		return ring;
	}
	public void setRing(int ring) {
		this.ring = ring;
	}
	public int getApps() {
		return apps;
	}
	public void setApps(int apps) {
		this.apps = apps;
	}
	public int getContacts() {
		return contacts;
	}
	public void setContacts(int contacts) {
		this.contacts = contacts;
	}
}
