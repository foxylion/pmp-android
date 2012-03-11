/*
 * Copyright 2012 pmp-android development team
 * Project: Editor
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
package de.unistuttgart.ipvs.pmp.editor.util;

import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * Interface that defines the method to update and download the list of {@link RGIS} from the RG Server
 * 
 * @author Thorsten Berberich
 * 
 */
public interface IServerProvider {
    
    /**
     * Returns a list of all available resource-groups. If this list hasn't been
     * fetched from the server before, a connection to the server will be
     * established to gather all available resource-groups (see {@link
     * updateResourceGroupList()}
     * 
     * @return
     * @throws IOException
     */
    public List<RGIS> getAvailableRessourceGroups() throws IOException;
    
    
    /**
     * Contacts the server to to gather all currently available resource-groups
     * and update the RGIS-List.
     * 
     * @throws IOException
     *             Thrown, if server is not reachable or returns invalid values
     */
    public void updateResourceGroupList() throws IOException;
}
