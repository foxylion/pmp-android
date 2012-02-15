package de.unistuttgart.ipvs.pmp.xmlutil.common.informationset;

public abstract class BasicIdentifierIS extends BasicIS {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -7004162555072891861L;
    
    /**
     * The identifier
     */
    private String identifier = "";
    
    
    /**
     * Get the identifier
     * 
     * @return identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    /**
     * Set the identifier
     * 
     * @param identifier
     *            identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
}
