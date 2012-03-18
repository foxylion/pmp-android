package de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.resource;

/**
 * This object will be parsed by the {@link JSonRequestReader} to the {@link JSonRequestProvider}. It holds the key and
 * a
 * value of an parameter
 * 
 * @author Alexander Wassiljew
 * 
 */
public class ParamObject {
    
    private boolean isPost;
    /**
     * Key of the {@link ParamObject}
     */
    private String key;
    /**
     * Value of the {@link ParamObject}
     */
    private String value;
    
    
    public ParamObject(String key, String value, boolean isPost) {
        this.key = key;
        this.value = value;
        this.isPost = isPost;
    }
    
    
    public boolean isPost() {
        return this.isPost;
    }
    
    
    /**
     * Returns the key
     * 
     * @return key
     */
    public String getKey() {
        return this.key;
    }
    
    
    /**
     * Return the value
     * 
     * @return value
     */
    public String getValue() {
        return this.value;
    }
}