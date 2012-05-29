package de.unistuttgart.ipvs.pmp.apps.vhike.model;

public class CompactMessage {
    
    public int id;
    public CompactUser sender, recipient;
    public boolean isOffer;
    public int status;
    public String message;
    
    
    public CompactMessage(int messageId, CompactUser sender, CompactUser recipient, boolean isOffer, String message) {
        this.id = messageId;
        this.sender = sender;
        this.recipient = recipient;
        this.isOffer = isOffer;
        this.message = message;
        
    }
    
    
    public String toString() {
        return sender.name + (isOffer ? " (Request)" : "");
    }
    
    
    public int getId() {
        return id;
    }
    
}
