package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.MyTripAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.CompactTrip;

/**
 * 
 * @author Dang Huynh
 * 
 */
public class MyTripActivity extends ListActivity {
    
    private ArrayList<CompactTrip> t;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        t = new ArrayList<CompactTrip>(10);
        t.add(new CompactTrip(1, "Berlin", 1000000000, 2, 3, 1));
        t.add(new CompactTrip(2, "Stuttgart", 1000000000, 0, 3, 1));
        t.add(new CompactTrip(3, "München", 1000000000, 2, 0, 1));
        t.add(new CompactTrip(4, "Köln", 1000000000, 2, 3, 0));
        t.add(new CompactTrip(5, "Düsseldorf", 1000000000, 2, 3, 1));
        t.add(new CompactTrip(6, "Hamburg", 1000000000, 0, 0, 0));
        t.add(new CompactTrip(7, "Text", 1000000000, 2, 3, 1));
        t.add(new CompactTrip(8, "test", 1000000000, 2, 3, 1));
        t.add(new CompactTrip(9, "Hallihallo", 1000000000, 2, 3, 1));
        t.add(new CompactTrip(10, "alohodsnheif", 1000000000, 2, 3, 1));
        
        ListView listView = getListView();
        listView.setAdapter(new MyTripAdapter(this, t));
        listView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyTripActivity.this, TripDetailActivity.class);
                intent.putExtra("tripId", t.get(position).id);
                MyTripActivity.this.startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Click ListItem Number " + position, Toast.LENGTH_LONG).show();
            }
        });
        
        //        View header = getLayoutInflater().inflate(R.layout.header, null);
        //        View footer = getLayoutInflater().inflate(R.layout.footer, null);
        //        listView.addHeaderView(header);
        //        listView.addFooterView(footer);
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        
        //        Controller ctrl = new Controller(ViewModel.getInstance().getvHikeRG());
        //        
        //        if (vHikeService.isServiceFeatureEnabled(Constants.SF_HIDE_CONTACT_INFO)) {
        //            ctrl.enableAnonymity(Model.getInstance().getSid());
        //        } else {
        //            ctrl.disableAnonymity(Model.getInstance().getSid());
        //        }
        //        Log.i(this, "");
    }
}
