package de.unistuttgart.ipvs.pmp.resourcegroups.rssfeed;

import android.content.Context;
import android.os.RemoteException;

public class RSSFeedMockAdapter extends IRSSFeedAdapter.Stub {
    
    public RSSFeedMockAdapter(Context context, RSSFeedResource rssFeedResource, String appIdentifier) {
        // TODO Auto-generated constructor stub
    }
    
    
    @Override
    public RSSFeed fetch(String url) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
