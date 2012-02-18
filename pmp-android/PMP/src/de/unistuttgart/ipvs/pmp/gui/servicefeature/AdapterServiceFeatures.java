package de.unistuttgart.ipvs.pmp.gui.servicefeature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.gui.app.ActivityApps;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;

/**
 * The {@link AdapterServiceFeatures} is the list of Apps in the {@link ActivityApps}.
 * 
 * @author Jakob Jarosch
 */
public class AdapterServiceFeatures extends BaseAdapter {
    
    /**
     * {@link Context} which is used to create the Views of each App.
     */
    private Context context;
    
    /**
     * List of all Service Features which should be displayed.
     */
    private List<IServiceFeature> serviceFeatures;
    
    private Map<IServiceFeature, ListItemServiceFeature> serviceFeatureViews = new HashMap<IServiceFeature, ListItemServiceFeature>();
    
    
    public AdapterServiceFeatures(Context context, List<IServiceFeature> serviceFeatures) {
        this.context = context;
        this.serviceFeatures = serviceFeatures;
    }
    
    
    @Override
    public int getCount() {
        return this.serviceFeatures.size();
    }
    
    
    @Override
    public Object getItem(int position) {
        return this.serviceFeatures.get(position);
    }
    
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IServiceFeature serviceFeature = this.serviceFeatures.get(position);
        
        ListItemServiceFeature entryView;
        entryView = new ListItemServiceFeature(this.context, serviceFeature, this);
        this.serviceFeatureViews.put(serviceFeature, entryView);
        
        Log.v(this, "Returned view for " + serviceFeature.getIdentifier());
        
        return entryView;
    }
    
    
    /**
     * Refreshes all listed views.
     */
    public void refreshAllViews() {
        for (Entry<IServiceFeature, ListItemServiceFeature> view : this.serviceFeatureViews.entrySet()) {
            view.getValue().refresh(false);
            Log.v(this, "Refreshed ServiceFeatureView for SF " + view.getKey().getIdentifier());
        }
    }
}
