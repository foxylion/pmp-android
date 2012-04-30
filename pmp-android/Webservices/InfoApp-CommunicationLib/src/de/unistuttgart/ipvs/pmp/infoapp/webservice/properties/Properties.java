/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp-CommunicationLib
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
package de.unistuttgart.ipvs.pmp.infoapp.webservice.properties;

import java.io.IOException;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InternalDatabaseException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidParameterException;

public abstract class Properties {
    
    protected Service service;
    
    
    public Properties(Service service) {
        this.service = service;
    }
    
    
    /**
     * Uploads the properteis to the webservice
     * 
     * @throws InternalDatabaseException
     *             Thrown, if an internal database error occurred while the webservice was running
     * @throws InvalidParameterException
     *             Thrown, if one parameter set by the constructor or a set-methode was not accepted by the webservice
     * @throws IOException
     *             Thrown, if another communication error occured while contacting the webserviced
     */
    public abstract void commit() throws InternalDatabaseException, InvalidParameterException, IOException;
    
}
