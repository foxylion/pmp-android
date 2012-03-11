package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.ProfileActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.HistoryPersonObject;

public class HistoryRideAdapter extends BaseAdapter {
    
    TextView tv_username;
    RatingBar rating_bar;
    Context context;
    List<HistoryPersonObject> hPersonObjects;
    int tripid;
    
    
    public HistoryRideAdapter(Context context, List<HistoryPersonObject> hPersonObjects, int tripid) {
        this.hPersonObjects = hPersonObjects;
        this.context = context;
        this.tripid = tripid;
    }
    
    
    @Override
    public int getCount() {
        return this.hPersonObjects.size();
    }
    
    
    @Override
    public Object getItem(int arg0) {
        return this.hPersonObjects.get(arg0);
    }
    
    
    @Override
    public long getItemId(int arg0) {
        return arg0;
    }
    
    
    @Override
    public View getView(int position, View arg1, ViewGroup arg2) {
        final HistoryPersonObject hPersonObject = this.hPersonObjects.get(position);
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout entryView = (LinearLayout) inflater.inflate(R.layout.history_profile_item, null);
        this.tv_username = (TextView) entryView.findViewById(R.id.tv_username);
        this.rating_bar = (RatingBar) entryView.findViewById(R.id.ratingbar);
        
        this.tv_username.setText(hPersonObject.getUsername());
        this.rating_bar.setRating(hPersonObject.getRating());
        // Get and prepare Title
        entryView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryRideAdapter.this.context, ProfileActivity.class);
                intent.putExtra("RATING_MODUS", 1);
                intent.putExtra("MY_PROFILE", 1);
                intent.putExtra("PROFILE_ID", hPersonObject.getUserid());
                intent.putExtra("TRIP_ID", HistoryRideAdapter.this.tripid);
                HistoryRideAdapter.this.context.startActivity(intent);
            }
        });
        return entryView;
    }
    
}
