package de.unistuttgart.ipvs.pmp.apps.vhike.model;

public class CompactUser {
    
    public int id;
    public String name;
    
    
    public CompactUser(int id, String name) {
        if (id < 0)
            throw new IllegalArgumentException("ID cannot be negative");
        this.id = id;
        this.name = name;
    }
    
}
