/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
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
package de.unistuttgart.ipvs.pmp.xmlutil.ais;

import de.unistuttgart.ipvs.pmp.xmlutil.common.IdentifierIS;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class AISRequiredPrivacySetting extends IdentifierIS implements IAISRequiredPrivacySetting {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -2494745855919623707L;
    
    /**
     * The value of the Privacy Setting
     */
    private String value = "";
    
    
    /**
     * Constructor to set the attributes
     * 
     * @param identifier
     *            identifier of the Privacy Setting
     * @param value
     *            value of the Privacy Setting
     */
    public AISRequiredPrivacySetting(String identifier, String value) {
        setIdentifier(identifier);
        setValue(value);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredPrivacySetting#getValue()
     */
    @Override
    public String getValue() {
        return this.value;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredPrivacySetting#setValue(java.lang.String)
     */
    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
