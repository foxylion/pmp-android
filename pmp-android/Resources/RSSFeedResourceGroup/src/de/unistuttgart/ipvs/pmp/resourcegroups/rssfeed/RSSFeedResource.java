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
    
    
    @Override
    public IBinder getMockedAndroidInterface(String appIdentifier) {
        RSSFeedResourceGroup ressFeed = (RSSFeedResourceGroup) getResourceGroup();
        return new RSSFeedMockAdapter(ressFeed.getContext(), this, appIdentifier);
    }
    
    
    @Override
    public IBinder getCloakedAndroidInterface(String appIdentifier) {
        RSSFeedResourceGroup ressFeed = (RSSFeedResourceGroup) getResourceGroup();
        return new RSSFeedCloakAdapter(ressFeed.getContext(), this, appIdentifier);
    }
    
}
