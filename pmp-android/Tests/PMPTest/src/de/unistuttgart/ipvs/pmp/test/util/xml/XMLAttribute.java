package de.unistuttgart.ipvs.pmp.test.util.xml;

public class XMLAttribute {
    
    private String name;
    
    private String value;
    
    
    public XMLAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    
    public String getName() {
        return this.name;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public String getValue() {
        return this.value;
    }
    
    
    public void setValue(String value) {
        this.value = value;
    }
    
}
