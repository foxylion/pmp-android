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
package de.unistuttgart.ipvs.pmp.xmlutil.common;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueLocation;

public abstract class IdentifierIS extends IssueLocation implements IIdentifierIS {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 7193039662231041710L;
    
    /**
     * The identifier
     */
    private String identifier = "";
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IIdentifierIS#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IIdentifierIS#setIdentifier(java.lang.String)
     */
    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
}
