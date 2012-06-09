/*
 * Copyright 2012 pmp-android development team
 * Project: ProfileResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.profile.observer;

import android.database.ContentObserver;
import android.os.Handler;
import de.unistuttgart.ipvs.pmp.resourcegroups.profile.ProfileService;

public class SMSObserver extends ContentObserver {
    
    ProfileService ps;
    
    
    public SMSObserver(ProfileService ps) {
        super(new Handler());
        this.ps = ps;
    }
    
    
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        this.ps.processSMSEvent();
    }
}
