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
import de.unistuttgart.ipvs.pmp.xmlutil.revision.RevisionReader;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

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
    private List<RGIS> rgs;
    
    
    public AdapterAvailable(Context context, List<RGIS> rgs) {
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
        RGIS rgis = this.rgs.get(position);
        
        String rgId = rgis.getIdentifier();
        
        /*
         * TODO: Get revision out of the apk! Tobis task :-)
         *          Must be communicated from server side, we do not send APKs without a good reason ;)
         */
        long rgRev = -880658238000L;
        String rgRevHR = RevisionReader.get().toHumanReadable(rgRev);
        
        /* load the layout from the xml file */
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout entryView = (LinearLayout) inflater.inflate(R.layout.listitem_resourcegroups_available, null);
        /* Set name, description and state of the requested Resource Group */
        TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);
        String nameString = rgis.getNameForLocale(Locale.getDefault());
        if (nameString == null) {
            nameString = rgis.getNameForLocale(Locale.ENGLISH);
        }
        name.setText(nameString);
        
        TextView description = (TextView) entryView.findViewById(R.id.TextView_Description);
        String descriptionString = rgis.getDescriptionForLocale(Locale.getDefault());
        if (descriptionString == null) {
            descriptionString = rgis.getDescriptionForLocale(Locale.ENGLISH);
        }
        description.setText(descriptionString);
        
        TextView state = (TextView) entryView.findViewById(R.id.TextView_Status);
        if (ModelProxy.get().getResourceGroup(rgId) != null) {
            /* RG is already installed. */
            if (ModelProxy.get().getResourceGroup(rgId).getRevision() < rgRev) {
                /* A newer version is available */
                state.setText(this.context.getResources().getString(R.string.update) + " - rev. " + rgRevHR);
            } else {
                /* already up to date */
                state.setText(this.context.getResources().getString(R.string.installed));
            }
        } else {
            /* RG is not installed. */
            state.setText(this.context.getResources().getString(R.string.new_string) + " - rev. " + rgRevHR);
        }
        
        return entryView;
    }
}
