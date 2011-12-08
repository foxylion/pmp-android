package de.unistuttgart.ipvs.pmp.gui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import de.unistuttgart.ipvs.pmp.gui.activity.AppsActivity;
import de.unistuttgart.ipvs.pmp.gui.view.ServiceFeatureView;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;

;

/**
 * The {@link ServiceFeaturesAdapter} is the list of Apps in the {@link AppsActivity}.
 * 
 * @author Jakob Jarosch
 */
public class ServiceFeaturesAdapter extends BaseAdapter {
    
    private Context context;
    private List<IServiceFeature> serviceFeatures;
    
    
    public ServiceFeaturesAdapter(Context context, List<IServiceFeature> serviceFeatures) {
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
        
        ServiceFeatureView entryView = new ServiceFeatureView(this.context, serviceFeature);
        
        return entryView;
    }
}
