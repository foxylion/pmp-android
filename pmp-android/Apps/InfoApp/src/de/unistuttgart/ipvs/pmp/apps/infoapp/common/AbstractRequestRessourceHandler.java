/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp
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
package de.unistuttgart.ipvs.pmp.apps.infoapp.common;

import java.util.concurrent.Semaphore;

import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;

/**
 * Abstract class for getting the url of a resource group
 * 
 * @author Thorsten Berberich
 * 
 */
public abstract class AbstractRequestRessourceHandler extends PMPRequestResourceHandler {
    
    /**
     * Stored url
     */
    private String URL = null;
    
    /**
     * Semaphore where the caller is aquired
     */
    protected Semaphore sem;
    
    
    /**
     * Constructor
     * 
     * @param sem
     *            {@link Semaphore}
     */
    public AbstractRequestRessourceHandler(Semaphore sem) {
        this.sem = sem;
    }
    
    
    /**
     * @return the uRL
     */
    public String getURL() {
        return URL;
    }
    
    
    /**
     * @param uRL
     *            the uRL to set
     */
    public void setURL(String url) {
        URL = url;
    }
}
