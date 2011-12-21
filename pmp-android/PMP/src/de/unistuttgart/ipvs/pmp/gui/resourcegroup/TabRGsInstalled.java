package de.unistuttgart.ipvs.pmp.gui.resourcegroup;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

public class TabRGsInstalled extends Activity {
    
    private List<IResourceGroup> resourceGroups;
    private ListView installedRgsListView;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.tab_rgs_installed);
        
        installedRgsListView = (ListView) findViewById(R.id.ListView_RGs);
        
        addListener();
        
        refreshList();
    }
    
    
    public void refreshList() {
        resourceGroups = Arrays.asList(ModelProxy.get().getResourceGroups());
        AdapterRGsInstalled rgsAdapter = new AdapterRGsInstalled(this, resourceGroups);
        
        installedRgsListView.setAdapter(rgsAdapter);
    }
    
    
    private void addListener() {
        installedRgsListView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int item, long arg3) {
                new DialogRGInstalledDetails(TabRGsInstalled.this, TabRGsInstalled.this, resourceGroups.get(item))
                        .show();
            }
        });
    }
}
