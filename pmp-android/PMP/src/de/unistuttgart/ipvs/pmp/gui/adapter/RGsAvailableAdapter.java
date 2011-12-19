package de.unistuttgart.ipvs.pmp.gui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
//import de.unistuttgart.ipvs.pmp.pluginmanager.AvailablePlugins;

/**
 * 
 * @author Frieder Schüler
 */
//public class RGsAvailableAdapter extends BaseAdapter {
//    
//    private Context context;
////    private AvailablePlugins availablePlugins;
//    
//    
////    public RGsAvailableAdapter(Context context, AvailablePlugins availablePlugins) {
////        this.context = context;
////        this.availablePlugins = availablePlugins;
////    }
//    
////    
////    @Override
////    public int getCount() {
//////        return availablePlugins.getPlugins().size();
////    }
//    
//    
////    @Override
////    public Object getItem(int position) {
//////        return availablePlugins.getPlugins().get(position);
////    }
//    
//    
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//    
//    
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
////        AvailablePlugins.Plugin plugin = availablePlugins.getPlugins().get(position);
//        
//        /* load the layout from the xml file */
//        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout entryView = (LinearLayout) inflater.inflate(R.layout.listitem_resourcegroups_available, null);
//                
//        TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);
////        name.setText(plugin.getName());
//        
//        TextView description = (TextView) entryView.findViewById(R.id.TextView_Description);
////        description.setText(plugin.getDescription());
//        
//        TextView status = (TextView) entryView.findViewById(R.id.TextView_Status);
//        String text;
////        if (plugin.getInstalledRevision() == 0) {
////            status.setTextColor(Color.GRAY);
////            text = context.getString(R.string.rgs_notinstalled);
////        } else {
////            if (plugin.getInstalledRevision() == plugin.getLatestRevision()) {
////                status.setTextColor(Color.GREEN);
////                text = context.getString(R.string.rgs_uptodate);
////                text += " (r" + String.valueOf(plugin.getInstalledRevision()) + ")";
////            } else {
////                status.setTextColor(Color.YELLOW);
////                text = context.getString(R.string.rgs_outdated);
////                text += " (r" + String.valueOf(plugin.getInstalledRevision());
////                text += " to r" + String.valueOf(plugin.getLatestRevision()) + ")";
////            }
////        }
////        status.setText(text);
//        return entryView;
//    }
//}
