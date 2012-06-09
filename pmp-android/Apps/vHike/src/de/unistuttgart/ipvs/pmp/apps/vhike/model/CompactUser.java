package de.unistuttgart.ipvs.pmp.apps.vhike.model;

public class CompactUser {
    
    public int id;
    public String name;
    public float rating;
    
    
    public CompactUser(int id, String name, int rating) {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative");
        }
        this.id = id;
        this.name = name;
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
        this.rating = rating;
    }
}
