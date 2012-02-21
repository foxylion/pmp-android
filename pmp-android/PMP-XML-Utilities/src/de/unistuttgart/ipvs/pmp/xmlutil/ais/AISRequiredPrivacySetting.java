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
package de.unistuttgart.ipvs.pmp.xmlutil.ais;

import de.unistuttgart.ipvs.pmp.xmlutil.common.IdentifierIS;

/**
 * This is the required privacy setting of a {@link IAISRequiredResourceGroup}, which is assigned to a specific
 * {@link IAISServiceFeature}
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
     * The value of the {@link AISRequiredPrivacySetting}
     */
    private String value = "";
    
    
    /**
     * Constructor to set the attributes
     * 
     * @param identifier
     *            identifier of the {@link AISRequiredPrivacySetting}
     * @param value
     *            value of the {@link AISRequiredPrivacySetting}
     */
    public AISRequiredPrivacySetting(String identifier, String value) {
        setIdentifier(identifier);
        setValue(value);
    }
    
    
    @Override
    public String getValue() {
        return this.value;
    }
    
    
    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
