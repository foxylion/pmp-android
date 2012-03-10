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

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;

/**
 * Resource Group for fetching RSS feeds. Has the privacy settings
 * 
 * <ul>
 * <li>RSSFeedResourceGroup.PRIVACY_SETTING_ALLOW_RSS_FEED (true or false)</li>
 * </ul>
 * 
 * And the resources
 * 
 * <ul>
 * <li>SwitchesResourceGroup.RESOURCE_RSS_FEED (IRSSFeed)</li>
 * </ul>
 * 
 * @author Frieder Schüler
 * 
 */
public class RSSFeedResourceGroup extends ResourceGroup {
    
    public static final String PRIVACY_SETTING_ALLOW_RSS_FEED = "AllowRSSFeed";
    public static final String RESOURCE_RSS_FEED = "RSSFeedResourceGroup";
    
    
    public RSSFeedResourceGroup(IPMPConnectionInterface pmpci) {
        super("de.unistuttgart.ipvs.pmp.resourcegroups.rssfeed", pmpci);
        
        registerResource(RESOURCE_RSS_FEED, new RSSFeedResource());
        registerPrivacySetting(PRIVACY_SETTING_ALLOW_RSS_FEED, new BooleanPrivacySetting());
    }
    
    
    public void onRegistrationSuccess() {
        Log.d(this, "Registration success.");
    }
    
    
    public void onRegistrationFailed(String message) {
        Log.e(this, "Registration failed with \"" + message + "\"");
    }
    
}
