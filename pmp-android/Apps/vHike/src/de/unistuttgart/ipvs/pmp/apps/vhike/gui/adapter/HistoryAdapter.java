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
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.HistoryRideActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.HistoryRideObject;

public class HistoryAdapter extends BaseAdapter {
    
    Context context;
    List<HistoryRideObject> historyRides;
    String role;
    
    
    public HistoryAdapter(Context context, List<HistoryRideObject> historyRides, String role) {
        this.context = context;
        this.historyRides = historyRides;
        this.role = role;
    }
    
    
    @Override
    public int getCount() {
        return this.historyRides.size();
    }
    
    
    @Override
    public Object getItem(int position) {
        return this.historyRides.get(position);
    }
    
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final HistoryRideObject hRideObject = this.historyRides.get(position);
        
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout entryView = (LinearLayout) inflater.inflate(R.layout.history_layout_list, null);
        
        TextView tv_date = (TextView) entryView.findViewById(R.id.history_date);
        TextView tv_destination = (TextView) entryView.findViewById(R.id.history_destination);
        
        tv_date.setText("Date: " + hRideObject.getCreation());
        tv_destination.setText("Destination:" + hRideObject.getDestination());
        if (hRideObject.getTripid() == 0) {
            ;
        } else {
            entryView.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HistoryAdapter.this.context, HistoryRideActivity.class);
                    intent.putExtra("ID", position);
                    intent.putExtra("ROLE", HistoryAdapter.this.role);
                    HistoryAdapter.this.context.startActivity(intent);
                }
            });
        }
        
        return entryView;
    }
    
}
