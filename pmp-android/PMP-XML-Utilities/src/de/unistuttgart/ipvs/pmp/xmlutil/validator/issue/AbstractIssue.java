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
package de.unistuttgart.ipvs.pmp.xmlutil.validator.issue;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractIssue {
    
    /**
     * List of parameters
     */
    private List<String> parameters = new ArrayList<String>();
    
    
    /**
     * Get the parameter
     * 
     * @return the parameter ("", if no parameter exists)
     */
    public List<String> getParameters() {
        return parameters;
    }
    
    
    /**
     * Set the parameter
     * 
     * @param parameter
     *            the parameter to set
     */
    public void addParameter(String parameter) {
        this.parameters.add(parameter);
    }
    
}
