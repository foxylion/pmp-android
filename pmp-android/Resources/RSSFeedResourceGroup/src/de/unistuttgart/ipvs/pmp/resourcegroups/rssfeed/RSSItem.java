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
