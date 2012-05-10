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
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.infoapp.Constants;
import de.unistuttgart.ipvs.pmp.apps.infoapp.R;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.IPanel;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection;

/**
 * Displays the connection panel
 * 
 * @author Thorsten Berberich
 * 
 */
public class ConnectionsPanel implements IPanel {
    
    /**
     * The view
     */
    private LinearLayout view;
    
    /**
     * The adapter of the {@link ExpandableListView}
     * 
     */
    private ListViewAdapater adapter;
    
    
    /**
     * Constructor for the panel
     * 
     * @param context
     *            {@link Context}
     */
    public ConnectionsPanel(Context context) {
        // load the layout from the xml file
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = (LinearLayout) inflater.inflate(R.layout.connection_panel, null);
        
        ExpandableListView listView = (ExpandableListView) this.view.findViewById(R.id.expandable_list_view_connection);
        ArrayList<String> test = new ArrayList<String>();
        test.add("test");
        test.add("test");
        test.add("test");
        test.add("test");
        test.add("test");
        test.add("test");
        test.add("test");
        test.add("iefnweifujn cIties");
        adapter = new ListViewAdapater(context, test, test, test, test);
        listView.setAdapter(adapter);
        
        //        createLists();
    }
    
    
    /**
     * Get the view of this panel
     */
    public View getView() {
        return this.view;
    }
    
    
    /**
     * Get the title of this panel
     */
    public String getTitle() {
        return "Connections";
    }
    
    
    private void createLists() {
        //Try to get the cached ressource
        PMPResourceIdentifier identifier = PMPResourceIdentifier.make(Constants.CONNECTION_RG_IDENTIFIER,
                Constants.CONNECTION_RG_RESOURCE);
        PMP.get().getResource(identifier, new PMPRequestResourceHandler() {
            
            @Override
            public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder, boolean isMocked) {
                IConnection connectionStub = IConnection.Stub.asInterface(binder);
                
                // New lists
                ArrayList<String> wifiList = new ArrayList<String>();
                ArrayList<String> btList = new ArrayList<String>();
                ArrayList<String> dataList = new ArrayList<String>();
                ArrayList<String> cellPhoneList = new ArrayList<String>();
                
                // Fill the wifi list
                if (PMP.get().isServiceFeatureEnabled(Constants.CONNECTION_WIFI_INFO)) {
                    
                } else {
                    wifiList.add("The service features are insufficient. Tap here to enable service features...");
                }
                
                if (PMP.get().isServiceFeatureEnabled(Constants.CONNECTION_BT_INFO)) {
                    
                } else {
                    btList.add("The service features are insufficient. Tap here to enable service features...");
                }
                
                if (PMP.get().isServiceFeatureEnabled(Constants.CONNECTION_CELL_INFO)) {
                    
                } else {
                    cellPhoneList.add("The service features are insufficient. Tap here to enable service features...");
                }
                
                if (PMP.get().isServiceFeatureEnabled(Constants.CONNECTION_DATA_INFO)) {
                    
                } else {
                    dataList.add("The service features are insufficient. Tap here to enable service features...");
                }
                
                // Update the view
                adapter.updateLists(wifiList, btList, dataList, cellPhoneList);
            }
        });
        
    }
}
