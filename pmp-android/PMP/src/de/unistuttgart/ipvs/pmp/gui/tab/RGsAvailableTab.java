package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.Arrays;

import android.app.ListActivity;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.gui.adapter.RGsAdapter;
import de.unistuttgart.ipvs.pmp.gui.placeholder.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

public class RGsAvailableTab extends ListActivity {
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        IResourceGroup[] resourceGroups = ModelProxy.get().getResourceGroups();
        RGsAdapter rgsAdapter = new RGsAdapter(this, Arrays.asList(resourceGroups));
        this.setListAdapter(rgsAdapter);
        
        /*
        appsList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(AppsActivity.this, "Tapped on item " + arg2, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AppsActivity.this, AppActivity.class);
                AppsActivity.this.startActivity(i);
            }
            */
    }
}
