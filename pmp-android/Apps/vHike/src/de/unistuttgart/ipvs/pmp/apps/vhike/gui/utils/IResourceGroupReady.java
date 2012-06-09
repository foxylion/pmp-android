/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
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
package de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils;

import android.os.IInterface;

/**
 * This interface provides easy access to all the needed resource groups
 * 
 * @author Dang Huynh
 * 
 */
public interface IResourceGroupReady {
    
    /**
     * Callback function when your requested resource group is ready. This method set the local resource group variables
     * to real RG-object by default. Override this method and add your own logic.
     * 
     * Remember to catch Exceptions.
     * 
     * @param resourceGroup
     *            The returned resource group
     * @param resourceGroupId
     *            The ID of that resource group
     */
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId);
}
