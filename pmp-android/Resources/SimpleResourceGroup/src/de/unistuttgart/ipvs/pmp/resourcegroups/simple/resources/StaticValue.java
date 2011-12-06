/*
 * Copyright 2011 pmp-android development team
 * Project: DatabaseResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.simple.resources;

/**
 * @author Frieder Schüler
 * 
 */
@SuppressWarnings("rawtypes")
public class StaticValue extends ISimpleConnection.Stub {
    /**
     * Reads a file into a string
     * 
     * @param path
     *            File to read
     * @return Read data of the selected file
     * @throws IllegalAccessError
     *             Thrown, if app's privacy-level is not set
     * @throws RemoteException
     *             Thrown, if file is not readable
     */
    @Override
    public int getValue() {
        // Check if application is allowed to use this function
        return 42;
    }
}
