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
package de.unistuttgart.ipvs.pmp.resourcegroups.database;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupSingleApp;

/**
 * @author Dang Huynh
 * 
 */
public class DatabaseResourceGroupApp extends ResourceGroupSingleApp<DatabaseResourceGroup> {
    
    static {
        Log.setTagSufix("DatabaseRG");
    }
    
    
    @Override
    protected DatabaseResourceGroup createResourceGroup() {
        return new DatabaseResourceGroup(getApplicationContext());
    }
}
