package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.MyTripAdapter;

/**
 * 
 * @author Dang Huynh
 * 
 */
public class MyTripActivity extends ListActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String[] names = new String[] { "Linux", "Windows7", "Eclipse", "Suse", "Ubuntu", "Solaris", "Android",
                "iPhone", "Linux", "Windows7", "Eclipse" };
        //        View header = getLayoutInflater().inflate(R.layout.header, null);
        //        View footer = getLayoutInflater().inflate(R.layout.footer, null);
        ListView listView = getListView();
        //        listView.addHeaderView(header);
        //        listView.addFooterView(footer);
        listView.setAdapter(new MyTripAdapter(this, names));
    }
}
