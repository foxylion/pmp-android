package de.unistuttgart.ipvs.pmp.resourcegroups.rssfeed;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class RSSFeed implements Parcelable {
    
    public String name;
    public String url;
    public List<RSSItem> itemList = new ArrayList<RSSItem>();
    
    public static final Parcelable.Creator<RSSFeed> CREATOR = new Parcelable.Creator<RSSFeed>() {
        
        @Override
        public RSSFeed createFromParcel(Parcel source) {
            return RSSFeed.createFromParcel(source);
        }
        
        
        @Override
        public RSSFeed[] newArray(int size) {
            return new RSSFeed[size];
        }
    };
    
    
    public RSSFeed() {
    }
    
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.name);
        out.writeString(this.url);
        out.writeTypedList(this.itemList);
    }
    
    
    private static RSSFeed createFromParcel(Parcel in) {
        RSSFeed feed = new RSSFeed();
        feed.name = in.readString();
        feed.url = in.readString();
        in.readTypedList(feed.itemList, RSSItem.CREATOR);
        return feed;
    }
}
