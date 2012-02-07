package de.unistuttgart.ipvs.pmp.xmlutil.parser.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class ParsedNode {
    
    /**
     * Value of the node
     */
    private String value = "";
    
    /**
     * Attributes
     * key = attribute name
     * value = attribute value
     */
    private Map<String, String> attributes = new HashMap<String, String>();
    
    
    /**
     * Get the value
     * 
     * @return the value
     */
    public String getValue() {
        return this.value;
    }
    
    
    /**
     * Set the value
     * 
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    
    /**
     * Get the attributes map
     * 
     * @return the attributes
     */
    public Map<String, String> getAttributes() {
        return this.attributes;
    }
    
    
    /**
     * Get the value of an attribute given as parameter
     * 
     * @param attributeName
     *            name of the attribute
     * @return value of the attribute
     */
    public String getAttribute(String attributeName) {
        if (this.attributes.get(attributeName) == null) {
            return "";
        }
        return this.attributes.get(attributeName);
    }
    
    
    /**
     * Put an attribute to the map
     * 
     * @param attrName
     *            name of the attribute (key)
     * @param attrValue
     *            value of the attribute (value)
     */
    public void putAttribute(String attrName, String attrValue) {
        this.attributes.put(attrName, attrValue);
    }
    
}
