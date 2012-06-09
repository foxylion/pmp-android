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

import android.os.Parcel;
import android.os.Parcelable;

public class RSSItem implements Parcelable {
    
    public String title;
    public String date;
    public String link;
    public String description;
    
    public static final Parcelable.Creator<RSSItem> CREATOR = new Parcelable.Creator<RSSItem>() {
        
        @Override
        public RSSItem createFromParcel(Parcel source) {
            return RSSItem.createFromParcel(source);
        }
        
        
        @Override
        public RSSItem[] newArray(int size) {
            return new RSSItem[size];
        }
    };
    
    
    public RSSItem() {
    }
    
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.title);
        out.writeString(this.date);
        out.writeString(this.link);
        out.writeString(this.description);
    }
    
    
    private static RSSItem createFromParcel(Parcel in) {
        RSSItem rss = new RSSItem();
        rss.title = in.readString();
        rss.date = in.readString();
        rss.link = in.readString();
        rss.description = in.readString();
        return rss;
    }
}
