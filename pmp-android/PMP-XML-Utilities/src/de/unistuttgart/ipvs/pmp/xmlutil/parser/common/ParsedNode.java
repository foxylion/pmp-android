/*
 * Copyright 2012 pmp-android development team
 * Project: PMP-XML-UTILITIES
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
