/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.apps.infoapp.panels.connections;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.infoapp.R;

/**
 * List view adapter for the expandable list view
 * 
 * @author Thorsten Berberich
 * 
 */
public class ListViewAdapater extends BaseExpandableListAdapter {
    
    /**
     * Title of all group elements
     */
    private static final String groups[] = new String[4];
    
    /**
     * List for the wifi data
     */
    private ArrayList<String> wifiList;
    
    /**
     * List for the bluetooth data
     */
    private ArrayList<String> btList;
    
    /**
     * List for the data connection data
     */
    private ArrayList<String> dataList;
    
    /**
     * List for the cellular phone network data
     */
    private ArrayList<String> cellPhoneList;
    
    /**
     * {@link Context}
     */
    private Context context;
    
    
    /**
     * Constructor for a new {@link ListViewAdapater}
     * 
     * @param wifiList
     *            list with the wifi data
     * @param btList
     *            list with the bluetooth data
     * @param dataList
     *            list with the data connection data
     * @param cellPhoneList
     *            list with the cellular phone network data
     */
    public ListViewAdapater(Context context, ArrayList<String> wifiList, ArrayList<String> btList,
            ArrayList<String> dataList, ArrayList<String> cellPhoneList) {
        this.context = context;
        this.wifiList = wifiList;
        this.btList = btList;
        this.dataList = dataList;
        this.cellPhoneList = cellPhoneList;
        
        // Initialize groups
        groups[0] = context.getString(R.string.connection_panel_wifi);
        groups[1] = context.getString(R.string.connection_panel_bluetooth);
        groups[2] = context.getString(R.string.connection_panel_data_connection);
        groups[3] = context.getString(R.string.connection_panel_cell_phone);
    }
    
    
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     */
    public Object getChild(int group, int index) {
        switch (group) {
            case 0:
                return this.wifiList.get(index);
            case 1:
                return this.btList.get(index);
            case 2:
                return this.dataList.get(index);
            case 3:
                return this.cellPhoneList.get(index);
        }
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getChildId(int, int)
     */
    public long getChildId(int group, int childposition) {
        return childposition;
    }
    
    
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View, android.view.ViewGroup)
     */
    public View getChildView(int group, int index, boolean isLastChild, View convertView, ViewGroup parent) {
        // Inflate the layout
        LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View entryView = infalInflater.inflate(R.layout.list_item_connection_list, null);
        
        // Set name and description of one Privacy Setting
        TextView text = (TextView) entryView.findViewById(R.id.TextView_Name);
        
        switch (group) {
            case 0:
                text.setText(this.wifiList.get(index));
                break;
            case 1:
                text.setText(this.btList.get(index));
                break;
            case 2:
                text.setText(this.dataList.get(index));
                break;
            case 3:
                text.setText(this.cellPhoneList.get(index));
                break;
        }
        
        return entryView;
    }
    
    
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
     */
    public int getChildrenCount(int group) {
        switch (group) {
            case 0:
                return this.wifiList.size();
            case 1:
                return this.btList.size();
            case 2:
                return this.dataList.size();
            case 3:
                return this.cellPhoneList.size();
        }
        return 0;
    }
    
    
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getGroup(int)
     */
    public Object getGroup(int pos) {
        return groups[pos];
    }
    
    
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getGroupCount()
     */
    public int getGroupCount() {
        return groups.length;
    }
    
    
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getGroupId(int)
     */
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    
    
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View, android.view.ViewGroup)
     */
    public View getGroupView(int group, boolean arg1, View arg2, ViewGroup arg3) {
        // Inflate the layout
        LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View entryView = infalInflater.inflate(R.layout.group_item_connection_list, null);
        
        // Set name and description of one Privacy Setting
        TextView text = (TextView) entryView.findViewById(R.id.TextView_Name);
        text.setText(groups[group]);
        return entryView;
    }
    
    
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#hasStableIds()
     */
    public boolean hasStableIds() {
        return true;
    }
    
    
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
     */
    public boolean isChildSelectable(int group, int index) {
        String item;
        boolean result = false;
        switch (group) {
            case 0:
                item = this.wifiList.get(index);
                if (item.toLowerCase().contains(context.getString(R.string.connection_panel_connected_cities))) {
                    result = true;
                }
                
                if (item.contains(context.getString(R.string.sf_insufficient))) {
                    result = true;
                }
                return result;
            case 1:
                item = this.btList.get(index);
                if (item.toLowerCase().contains(context.getString(R.string.connection_panel_connected_cities))) {
                    result = true;
                }
                
                if (item.contains(context.getString(R.string.sf_insufficient))) {
                    result = true;
                }
                return result;
            case 2:
                item = this.dataList.get(index);
                return item.contains(context.getString(R.string.sf_insufficient));
            case 3:
                item = this.cellPhoneList.get(index);
                return item.contains(context.getString(R.string.sf_insufficient));
        }
        return false;
    }
    
    
    /**
     * Update the lists with the information and updates the view
     * 
     * @param wifiList
     *            new wifi list
     * @param btList
     *            new bluetooth list
     * @param dataList
     *            new data connection list
     * @param cellPhoneList
     *            new cellular phone network list
     */
    public void updateLists(ArrayList<String> wifiList, ArrayList<String> btList, ArrayList<String> dataList,
            ArrayList<String> cellPhoneList) {
        this.wifiList = wifiList;
        this.btList = btList;
        this.dataList = dataList;
        this.cellPhoneList = cellPhoneList;
        this.notifyDataSetChanged();
    }
    
}
