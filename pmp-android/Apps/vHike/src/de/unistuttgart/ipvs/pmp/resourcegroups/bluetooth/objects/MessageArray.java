package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects;

import java.io.Serializable;
import java.util.List;

public class MessageArray implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1670918836332795158L;
    List<String> messages;
    
    
    public MessageArray(List<String> messages) {
        this.messages = messages;
    }
    
    
    public List<String> getMessages() {
        return this.messages;
    }
}
