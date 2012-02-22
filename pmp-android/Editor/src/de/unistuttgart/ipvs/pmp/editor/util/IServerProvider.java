package de.unistuttgart.ipvs.pmp.editor.util;

import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

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