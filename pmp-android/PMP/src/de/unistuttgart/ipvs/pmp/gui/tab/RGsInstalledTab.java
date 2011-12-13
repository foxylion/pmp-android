package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.Arrays;

import android.app.ListActivity;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.gui.adapter.RGsAdapter;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

public class RGsInstalledTab extends ListActivity {
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IResourceGroup[] resourceGroups = ModelProxy.get().getInstalledResourceGroups();
        RGsAdapter rgsAdapter = new RGsAdapter(this, Arrays.asList(resourceGroups));
        this.setListAdapter(rgsAdapter);
    }
}
