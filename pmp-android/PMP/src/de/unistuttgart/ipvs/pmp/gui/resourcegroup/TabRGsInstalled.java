package de.unistuttgart.ipvs.pmp.gui.resourcegroup;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

/**
 * The {@link TabRGsInstalled} contains all installed Resourcegroups.
 * 
 * @author Jakob Jarosch
 */
public class TabRGsInstalled extends Activity {
    
    /**
     * List of all installed Resourcegroups.
     */
    protected List<IResourceGroup> resourceGroups;
    
    /**
     * ListView of all installed Resourcegroups.
     */
    private ListView installedRgsListView;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.tab_rgs_installed);
        
        this.installedRgsListView = (ListView) findViewById(R.id.ListView_RGs);
        
        addListener();
        
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        refreshList();
    }
    
    
    /**
     * Refreshs the list of installed Resourcegroups.
     */
    public void refreshList() {
        this.resourceGroups = Arrays.asList(ModelProxy.get().getResourceGroups());
        AdapterRGsInstalled rgsAdapter = new AdapterRGsInstalled(this, this.resourceGroups);
        
        this.installedRgsListView.setAdapter(rgsAdapter);
    }
    
    
    /**
     * Adds the listener to the Activity layout.
     */
    private void addListener() {
        this.installedRgsListView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int item, long arg3) {
                new DialogRGInstalledDetails(TabRGsInstalled.this, TabRGsInstalled.this,
                        TabRGsInstalled.this.resourceGroups.get(item)).show();
            }
        });
    }
}
