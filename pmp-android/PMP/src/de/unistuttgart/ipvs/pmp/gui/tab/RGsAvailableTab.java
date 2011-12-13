package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.Arrays;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.gui.activity.RGDetailsActivity;
import de.unistuttgart.ipvs.pmp.gui.adapter.RGsAdapter;
import de.unistuttgart.ipvs.pmp.gui.placeholder.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

public class RGsAvailableTab extends ListActivity {
    
    protected IResourceGroup[] resourceGroups;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        resourceGroups = ModelProxy.get().getResourceGroups();
        RGsAdapter rgsAdapter = new RGsAdapter(this, Arrays.asList(resourceGroups));
        this.setListAdapter(rgsAdapter);
        
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
            
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(RGsAvailableTab.this, RGDetailsActivity.class);
                String identifier = resourceGroups[position].getIdentifier();
                i.putExtra("identifier", identifier);
                RGsAvailableTab.this.startActivity(i);
            }
            
        });
        
    }
}
