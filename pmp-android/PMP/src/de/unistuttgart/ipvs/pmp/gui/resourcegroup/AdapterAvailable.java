package de.unistuttgart.ipvs.pmp.gui.resourcegroup;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;

/**
 * The {@link RgAvailableAdapter} is the list of available Resourcegroups in the {@link TabAvailable}.
 * 
 * @author Jakob Jarosch
 */
public class AdapterAvailable extends BaseAdapter {
    
    /**
     * {@link Context} which is used to create the Views of each Resource Groups.
     */
    private Context context;
    
    /**
     * List of all Resource Groups which should be displayed.
     */
    private List<RgInformationSet> rgs;
    
    
    public AdapterAvailable(Context context, List<RgInformationSet> rgs) {
        this.context = context;
        this.rgs = rgs;
    }
    
    
    @Override
    public int getCount() {
        return this.rgs.size();
    }
    
    
    @Override
    public Object getItem(int position) {
        return this.rgs.get(position);
    }
    
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RgInformationSet rgis = this.rgs.get(position);
        
        String rgId = rgis.getIdentifier();
        int rgRev = Integer.parseInt(rgis.getRevision());
        
        /* load the layout from the xml file */
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout entryView = (LinearLayout) inflater.inflate(R.layout.listitem_resourcegroups_available, null);
        /* Set name, description and state of the requested Resource Group */
        TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);
        String nameString = rgis.getNames().get(Locale.getDefault());
        if (nameString == null) {
            nameString = rgis.getNames().get(Locale.ENGLISH);
        }
        name.setText(nameString);
        
        TextView description = (TextView) entryView.findViewById(R.id.TextView_Description);
        String descriptionString = rgis.getDescriptions().get(Locale.getDefault());
        if (descriptionString == null) {
            descriptionString = rgis.getDescriptions().get(Locale.ENGLISH);
        }
        description.setText(descriptionString);
        
        TextView state = (TextView) entryView.findViewById(R.id.TextView_Status);
        if (ModelProxy.get().getResourceGroup(rgId) != null) {
            /* RG is already installed. */
            if (ModelProxy.get().getResourceGroup(rgId).getRevision() < rgRev) {
                /* A newer version is available */
                state.setText(this.context.getResources().getString(R.string.update) + " - rev. " + rgRev);
            } else {
                /* already up to date */
                state.setText(this.context.getResources().getString(R.string.installed));
            }
        } else {
            /* RG is not installed. */
            state.setText(this.context.getResources().getString(R.string.new_string) + " - rev. " + rgRev);
        }
        
        return entryView;
    }
}