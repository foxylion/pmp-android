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
package de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.apps.infoapp.Constants;
import de.unistuttgart.ipvs.pmp.apps.infoapp.R;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy.data.EnergyCurrentValues;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy.data.EnergyLastBootValues;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy.data.EnergyTotalValues;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class EnergyExtListViewAdapter extends BaseExpandableListAdapter {
    
    /**
     * The group item names
     */
    private static final String groupItems[] = new String[3];
    
    /**
     * {@link Context}
     */
    private Context context;
    
    /**
     * {@link Activity}
     */
    private Activity activity;
    
    /**
     * The data objects
     */
    private EnergyCurrentValues cv;
    private EnergyLastBootValues lbv;
    private EnergyTotalValues tv;
    
    /**
     * Flags, if the groups are enabled (service features)
     */
    private boolean cvEnabled = false;
    private boolean lbvEnabled = false;
    private boolean tvEnabled = false;
    
    
    /**
     * 
     * @param context
     * @param activity
     * @param cv
     * @param lbv
     * @param tv
     */
    public EnergyExtListViewAdapter(Context context, Activity activity, EnergyCurrentValues cv,
            EnergyLastBootValues lbv, EnergyTotalValues tv) {
        // Set the context and activity
        this.context = context;
        this.activity = activity;
        
        // Set the data objects
        this.cv = cv;
        this.lbv = lbv;
        this.tv = tv;
        
        // Initialize groups
        groupItems[0] = context.getString(R.string.energy_panel_current_values);
        groupItems[1] = context.getString(R.string.energy_panel_last_boot_values);
        groupItems[2] = context.getString(R.string.energy_panel_total_values);
    }
    
    
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     */
    public Object getChild(int group, int index) {
        switch (group) {
            case 0:
                return this.cv;
            case 1:
                return this.lbv;
            case 2:
                return this.tv;
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
        LayoutInflater inflater;
        View entryView = null;
        switch (group) {
            case 0:
                // Inflate the layout
                inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                entryView = inflater.inflate(R.layout.energy_current_values, null);
                
                if (this.cvEnabled) {
                    // Get the textviews
                    TextView tvLevel = (TextView) entryView.findViewById(R.id.energyCVLevel);
                    TextView tvHealth = (TextView) entryView.findViewById(R.id.energyCVHealth);
                    TextView tvStatus = (TextView) entryView.findViewById(R.id.energyCVStatus);
                    TextView tvPlugged = (TextView) entryView.findViewById(R.id.energyCVPlugged);
                    TextView tvStatusTime = (TextView) entryView.findViewById(R.id.energyCVStatusTime);
                    TextView tvTemperature = (TextView) entryView.findViewById(R.id.energyCVTemperature);
                    
                    // Set the values
                    tvLevel.setText(this.cv.getLevel());
                    tvHealth.setText(this.cv.getHealth());
                    tvStatus.setText(this.cv.getStatus());
                    tvPlugged.setText(this.cv.getPlugged());
                    tvStatusTime.setText(this.cv.getStatusTime());
                    tvTemperature.setText(this.cv.getTemperature());
                    
                    // Enable the table
                    TableLayout tlCV = (TableLayout) entryView.findViewById(R.id.energyCVtableLayout);
                    tlCV.setVisibility(TableLayout.VISIBLE);
                    
                    // Disable the "not available" text view
                    TextView tvCVnotAvailable = (TextView) entryView.findViewById(R.id.energyCVNotAvailable);
                    tvCVnotAvailable.setVisibility(TextView.GONE);
                    
                } else {
                    // Add on click listener
                    entryView.setOnClickListener(new OnClickListener() {
                        
                        public void onClick(View v) {
                            List<String> sfs = new ArrayList<String>();
                            sfs.add("energy-current");
                            PMP.get(EnergyExtListViewAdapter.this.activity.getApplication()).requestServiceFeatures(
                                    EnergyExtListViewAdapter.this.activity, sfs);
                        }
                    });
                    
                    // Disable the table
                    TableLayout tlCV = (TableLayout) entryView.findViewById(R.id.energyCVtableLayout);
                    tlCV.setVisibility(TableLayout.GONE);
                    
                    // Enable the "not available" text view
                    TextView tvCVnotAvailable = (TextView) entryView.findViewById(R.id.energyCVNotAvailable);
                    tvCVnotAvailable.setVisibility(TextView.VISIBLE);
                }
                
                return entryView;
            case 1:
                // Inflate the layout
                inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                entryView = inflater.inflate(R.layout.energy_last_boot_values, null);
                
                if (this.lbvEnabled) {
                    // Get the textviews
                    TextView tvLBVDate = (TextView) entryView.findViewById(R.id.energyLBVDate);
                    TextView tvLBVUptime = (TextView) entryView.findViewById(R.id.energyLBVUptime);
                    TextView tvLBVUptimeBattery = (TextView) entryView.findViewById(R.id.energyLBVUptimeBattery);
                    TextView tvLBVDurationCharging = (TextView) entryView.findViewById(R.id.energyLBVDurationCharging);
                    TextView tvLBVRatio = (TextView) entryView.findViewById(R.id.energyLBVRatio);
                    TextView tvLBVCountCharging = (TextView) entryView.findViewById(R.id.energyLBVCountOfCharging);
                    TextView tvLBVTemperaturePeak = (TextView) entryView.findViewById(R.id.energyLBVTemperaturePeak);
                    TextView tvLBVTemperatureAverage = (TextView) entryView
                            .findViewById(R.id.energyLBVTemperatureAverage);
                    TextView tvLBVScreenOn = (TextView) entryView.findViewById(R.id.energyLBVScreenOn);
                    
                    // Set the values
                    // Format the date
                    try {
                        String date = Constants.DATE_FORMAT.format(new Date(Long.valueOf(this.lbv.getDate())));
                        tvLBVDate.setText(date);
                    } catch (Exception e) {
                        tvLBVDate.setText(this.lbv.getDate());
                    }
                    tvLBVUptime.setText(this.lbv.getUptime());
                    tvLBVUptimeBattery.setText(this.lbv.getUptimeBattery());
                    tvLBVDurationCharging.setText(this.lbv.getDurationOfCharging());
                    tvLBVRatio.setText(this.lbv.getRatio());
                    tvLBVCountCharging.setText(this.lbv.getCountOfCharging());
                    tvLBVTemperaturePeak.setText(this.lbv.getTemperaturePeak());
                    tvLBVTemperatureAverage.setText(this.lbv.getTemperatureAverage());
                    tvLBVScreenOn.setText(this.lbv.getScreenOn());
                    
                    // Enable the table
                    TableLayout tlLBV = (TableLayout) entryView.findViewById(R.id.energyLBVtableLayout);
                    tlLBV.setVisibility(TableLayout.VISIBLE);
                    
                    // Disable the "not available" text view
                    TextView tvLBVnotAvailable = (TextView) entryView.findViewById(R.id.energyLBVNotAvailable);
                    tvLBVnotAvailable.setVisibility(TextView.GONE);
                    
                } else {
                    // Add on click listener
                    entryView.setOnClickListener(new OnClickListener() {
                        
                        public void onClick(View v) {
                            List<String> sfs = new ArrayList<String>();
                            sfs.add("energy-since-last-boot");
                            PMP.get(EnergyExtListViewAdapter.this.activity.getApplication()).requestServiceFeatures(
                                    EnergyExtListViewAdapter.this.activity, sfs);
                        }
                    });
                    
                    // Disable the table
                    TableLayout tlLBV = (TableLayout) entryView.findViewById(R.id.energyLBVtableLayout);
                    tlLBV.setVisibility(TableLayout.GONE);
                    
                    // Enable the "not available" text view
                    TextView tvLBVnotAvailable = (TextView) entryView.findViewById(R.id.energyLBVNotAvailable);
                    tvLBVnotAvailable.setVisibility(TextView.VISIBLE);
                }
                
                return entryView;
            case 2:
                // Inflate the layout
                inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                entryView = inflater.inflate(R.layout.energy_total_values, null);
                
                if (this.tvEnabled) {
                    // Get the textviews
                    TextView tvTVDate = (TextView) entryView.findViewById(R.id.energyTVDate);
                    TextView tvTVUptime = (TextView) entryView.findViewById(R.id.energyTVUptime);
                    TextView tvTVUptimeBattery = (TextView) entryView.findViewById(R.id.energyTVUptimeBattery);
                    TextView tvTVDurationCharging = (TextView) entryView.findViewById(R.id.energyTVDurationCharging);
                    TextView tvTVRatio = (TextView) entryView.findViewById(R.id.energyTVRatio);
                    TextView tvTVCountCharging = (TextView) entryView.findViewById(R.id.energyTVCountOfCharging);
                    TextView tvTVTemperaturePeak = (TextView) entryView.findViewById(R.id.energyTVTemperaturePeak);
                    TextView tvTVTemperatureAverage = (TextView) entryView
                            .findViewById(R.id.energyTVTemperatureAverage);
                    TextView tvTVScreenOn = (TextView) entryView.findViewById(R.id.energyTVScreenOn);
                    
                    // Set the values
                    // Format the date
                    try {
                        String date = Constants.DATE_FORMAT.format(new Date(Long.valueOf(this.tv.getDate())));
                        tvTVDate.setText(date);
                    } catch (Exception e) {
                        tvTVDate.setText(this.tv.getDate());
                    }
                    tvTVUptime.setText(this.tv.getUptime());
                    tvTVUptimeBattery.setText(this.tv.getUptimeBattery());
                    tvTVDurationCharging.setText(this.tv.getDurationOfCharging());
                    tvTVRatio.setText(this.tv.getRatio());
                    tvTVCountCharging.setText(this.tv.getCountOfCharging());
                    tvTVTemperaturePeak.setText(this.tv.getTemperaturePeak());
                    tvTVTemperatureAverage.setText(this.tv.getTemperatureAverage());
                    tvTVScreenOn.setText(this.tv.getScreenOn());
                    
                    // Enable the table
                    TableLayout tlTV = (TableLayout) entryView.findViewById(R.id.energyTVtableLayout);
                    tlTV.setVisibility(TableLayout.VISIBLE);
                    
                    // Disable the "not available" text view
                    TextView tvTVnotAvailable = (TextView) entryView.findViewById(R.id.energyTVNotAvailable);
                    tvTVnotAvailable.setVisibility(TextView.GONE);
                    
                } else {
                    // Add on click listener
                    entryView.setOnClickListener(new OnClickListener() {
                        
                        public void onClick(View v) {
                            List<String> sfs = new ArrayList<String>();
                            sfs.add("energy-total");
                            PMP.get(EnergyExtListViewAdapter.this.activity.getApplication()).requestServiceFeatures(
                                    EnergyExtListViewAdapter.this.activity, sfs);
                        }
                    });
                    
                    // Disable the table
                    TableLayout tlTV = (TableLayout) entryView.findViewById(R.id.energyTVtableLayout);
                    tlTV.setVisibility(TableLayout.GONE);
                    
                    // Enable the "not available" text view
                    TextView tvTVnotAvailable = (TextView) entryView.findViewById(R.id.energyTVNotAvailable);
                    tvTVnotAvailable.setVisibility(TextView.VISIBLE);
                }
                
                return entryView;
        }
        
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
     */
    public int getChildrenCount(int group) {
        return 1;
    }
    
    
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getGroup(int)
     */
    public Object getGroup(int pos) {
        return groupItems[pos];
    }
    
    
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getGroupCount()
     */
    public int getGroupCount() {
        return groupItems.length;
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
        View entryView = infalInflater.inflate(R.layout.energy_list_group_item, null);
        
        // Set name and description of one Privacy Setting
        TextView text = (TextView) entryView.findViewById(R.id.EnergyGroupItemName);
        text.setText(groupItems[group]);
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
        return true;
    }
    
    
    /**
     * @param cv
     *            the cv to set
     */
    public void setCv(EnergyCurrentValues cv) {
        this.cv = cv;
    }
    
    
    /**
     * @param lbv
     *            the lbv to set
     */
    public void setLbv(EnergyLastBootValues lbv) {
        this.lbv = lbv;
    }
    
    
    /**
     * @param tv
     *            the tv to set
     */
    public void setTv(EnergyTotalValues tv) {
        this.tv = tv;
    }
    
    
    /**
     * @param cvEnabled
     *            the cvEnabled to set
     */
    public void setCvEnabled(boolean cvEnabled) {
        this.cvEnabled = cvEnabled;
    }
    
    
    /**
     * @param lbvEnabled
     *            the lbvEnabled to set
     */
    public void setLbvEnabled(boolean lbvEnabled) {
        this.lbvEnabled = lbvEnabled;
    }
    
    
    /**
     * @param tvEnabled
     *            the tvEnabled to set
     */
    public void setTvEnabled(boolean tvEnabled) {
        this.tvEnabled = tvEnabled;
    }
    
}
