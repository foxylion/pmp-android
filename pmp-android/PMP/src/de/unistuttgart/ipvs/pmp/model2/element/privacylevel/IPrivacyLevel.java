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
package de.unistuttgart.ipvs.pmp.model2.element.privacylevel;

import de.unistuttgart.ipvs.pmp.model2.element.resourcegroup.IResourceGroup;
import android.os.RemoteException;

/**
 * The {@link IPrivacyLevel} interface represents a PrivacyLevel of a {@link IResourceGroup}. It can be fetched from an
 * {@link IServiceLevel}, {@link IPreset} or {@link IResourceGroup}.<br>
 * 
 * If it is fetch from {@link IPreset} or {@link IServiceLevel} the {@link IPrivacyLevel#getValue()} will return the set
 * value for this {@link IPrivacyLevel}.<br>
 * 
 * The {@link IPrivacyLevel} from the {@link IResourceGroup} is only a representation of an {@link IPrivacyLevel}
 * assigned to this {@link IResourceGroup} and has not its own value, so {@link IPrivacyLevel#getValue()} will always
 * return null.
 * 
 * @author Jakob Jarosch
 */
public interface IPrivacyLevel {
    
    /**
     * @return a system-wide unique identifier for this privacy level
     */
    public String getUniqueIdentifier();
    
    
    /**
     * @return Returns the {@link IResourceGroup} wide unique identifier of the {@link IPrivacyLevel}.
     */
    public String getIdentifier();
    
    
    /**
     * @return Returns the parent of the {@link IPrivacyLevel} (a {@link IResourceGroup}).
     */
    public IResourceGroup getResourceGroup();
    
    
    /**
     * @return Returns the localized name of the {@link IPrivacyLevel}.
     */
    public String getName();
    
    
    /**
     * @return Returns the localized description of the {@link IPrivacyLevel}.
     */
    public String getDescription();
    
    
    /**
     * Returns the set value of the {@link IPrivacyLevel}.
     * 
     * @return Returns the set value, null if it is fetched from an {@link IResourceGroup} instance, see
     *         {@link IPrivacyLevel} JavaDoc comment for more details.
     */
    @Deprecated
    public String getValue();
    
    
    /**
     * Returns an human readable representation of the a given value.<br>
     * <b>This method uses service communication and is slow, use with caution.</b>
     * 
     * <p>
     * <b>This method is uses blocking access to other Services, you must call this method in another Thread than the
     * Main-Worker-Thread! Otherwise you will end in a deadlock</b>
     * </p>
     * 
     * @param value
     *            Value which should be returned as a human readable representation.
     * @return the human readable representation of the given value.
     * 
     * @throws RemoteException
     *             Is thrown when the connection to the {@link IResourceGroup}s service can not be correctly
     *             established.
     */
    public String getHumanReadableValue(String value) throws RemoteException;
    
    
    /**
     * Checks is a given value satisfies the given reference, in general it means value is better than or equals
     * reference.<br>
     * <b>This method uses service communication and is slow, use with caution.</b>
     * 
     * <p>
     * <b>This method is uses blocking access to other Services, you must call this method in another Thread than the
     * Main-Worker-Thread! Otherwise you will end in a deadlock</b>
     * </p>
     * 
     * @param reference
     *            The reference value which should be used to check satisfaction.
     * @param value
     *            The value which should be checked against the reference value.
     * @return True when value satisfies reference, otherwise false.
     * @throws RemoteException
     *             Is thrown when the connection to the {@link IResourceGroup}s service can not be correctly
     *             established.
     */
    public boolean permits(String reference, String value) throws RemoteException;
    
}
