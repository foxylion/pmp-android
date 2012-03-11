package de.unistuttgart.ipvs.pmp.resourcegroups.rssfeed;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;

/**
 * @author Frieder Schüler
 * 
 */
public class RSSFeedResource extends Resource {
    
    @Override
    public IBinder getAndroidInterface(String appIdentifier) {
        RSSFeedResourceGroup ressFeed = (RSSFeedResourceGroup) getResourceGroup();
        return new RSSFeedAdapter(ressFeed.getContext(), this, appIdentifier);
    }
    
}
