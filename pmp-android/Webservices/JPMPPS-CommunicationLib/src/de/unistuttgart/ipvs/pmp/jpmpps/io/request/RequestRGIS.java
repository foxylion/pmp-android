/*
 * Copyright 2012 pmp-android development team
 * Project: JPMPPS-CommunicationLib
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
package de.unistuttgart.ipvs.pmp.jpmpps.io.request;

/**
 * Message should be submitted when a RGIS is requested.
 * 
 * @author Jakob Jarosch
 */
public class RequestRGIS extends AbstractRequest {
    
    private static final long serialVersionUID = 1L;
    
    private String packageName;
    
    
    /**
     * Creates a new request for the given package name.
     * 
     * @param packageName
     *            Name of the package for which the RGIS should be returned.
     */
    public RequestRGIS(String packageName) {
        this.packageName = packageName;
    }
    
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    
    public String getPackageName() {
        return this.packageName;
    }
}
