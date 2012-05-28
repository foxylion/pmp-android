package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.MyTripAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.CompactTrip;

/**
 * 
 * @author Dang Huynh
 * 
 */
public class MyTripsActivity extends Activity {
    
    private ArrayList<CompactTrip> t;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);
        
        t = new ArrayList<CompactTrip>(10);
        t.add(new CompactTrip(1, "Berlin", 1000000000, 2, 3, 1));
        t.add(new CompactTrip(2, "Stuttgart", 1000000000, 0, 3, 1));
        t.add(new CompactTrip(3, "Muenchen", 1000000000, 2, 0, 1));
        t.add(new CompactTrip(4, "Koeln", 1000000000, 2, 3, 0));
        t.add(new CompactTrip(5, "Duesseldorf", 1000000000, 2, 3, 1));
        t.add(new CompactTrip(6, "Hamburg", 1000000000, 0, 0, 0));
        t.add(new CompactTrip(7, "Dortmund", 1000000000, 2, 3, 1));
        t.add(new CompactTrip(8, "Bremen", 1000000000, 2, 3, 1));
        t.add(new CompactTrip(9, "Hanover", 1000000000, 2, 3, 1));
        t.add(new CompactTrip(10, "Leipzig", 1000000000, 2, 3, 1));
        
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new MyTripAdapter(this, t));
        listView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyTripsActivity.this, TripDetailActivity.class);
                intent.putExtra("tripId", t.get(position).id);
                MyTripsActivity.this.startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Click ListItem Number " + position, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
    }
}
