/*
 * Copyright 2011 pmp-android development team
 * Project: FileSystemResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupSingleApp;

/**
 * Application class encapsulating all resource-groups and its services. Currently we have only one resource-group, so
 * we are using "ResourceGroupSingleApp" as our base-class instead of "ResourceGroupApp". This keeps our application
 * class simple.
 * 
 * @author Patrick Strobel
 * @version 0.1.0
 * 
 */
public class FileSystemResourceGroupApp extends ResourceGroupSingleApp<FileSystemResourceGroup> {
    
    static {
        Log.setTagSufix("FilesystemRG");
    }
    
    
    @Override
    protected FileSystemResourceGroup createResourceGroup() {
        return new FileSystemResourceGroup(getApplicationContext());
    }
    
}
