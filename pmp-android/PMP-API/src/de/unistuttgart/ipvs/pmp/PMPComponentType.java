package de.unistuttgart.ipvs.pmp;

public enum PMPComponentType {
    APP("APP"),
    RESOURCE_GROUP("RESOURCE_GROUP"),
    PMP("PMP"),
    NONE("NONE");
    
    private String toString;
    
    
    PMPComponentType(String toString) {
        this.toString = toString;
    }
    
    
    @Override
    public String toString() {
        return this.toString;
    }
};
