package de.unistuttgart.ipvs.pmp.gui.app;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    private Activity activity;
    
    /**
     * List of all Service Features which should be displayed.
     */
    private List<IServiceFeature> serviceFeatures;
    
    
    public AdapterServiceFeatures(Activity activity, List<IServiceFeature> serviceFeatures) {
        this.activity = activity;
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
        
        ListItemServiceFeature entryView = new ListItemServiceFeature(this.activity, serviceFeature);
        
        return entryView;
    }
}
