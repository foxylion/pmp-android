package de.unistuttgart.ipvs.pmp.apps.vhike.model;

public class CompactMessage {
    
    public int id;
    public CompactUser sender, recipient;
    public boolean isInvitation;
    public int status;
    public String message;
    
    
    public CompactMessage(int messageId, CompactUser sender, CompactUser recipient, boolean isInvitation, String message) {
        this.id = messageId;
        this.sender = sender;
        this.recipient = recipient;
        this.isInvitation = isInvitation;
        this.message = message;
    }
    
    
    public String toString() {
        return sender.name + (isInvitation ? " (Request)" : "");
    }
    
    
    public int getId() {
        return id;
    }
    
}
