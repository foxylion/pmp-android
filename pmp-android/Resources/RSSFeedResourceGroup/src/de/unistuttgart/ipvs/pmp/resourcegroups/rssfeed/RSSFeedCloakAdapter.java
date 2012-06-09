/*
 * Copyright 2012 pmp-android development team
 * Project: RSSFeedResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.rssfeed;

import android.content.Context;
import android.os.RemoteException;

public class RSSFeedCloakAdapter extends IRSSFeedAdapter.Stub {
    
    public RSSFeedCloakAdapter(Context context, RSSFeedResource rssFeedResource, String appIdentifier) {
        // TODO Auto-generated constructor stub
    }
    
    
    @Override
    public RSSFeed fetch(String url) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
