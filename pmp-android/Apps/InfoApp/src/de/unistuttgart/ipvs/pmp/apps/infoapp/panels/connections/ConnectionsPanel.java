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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import de.unistuttgart.ipvs.pmp.apps.infoapp.R;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.IPanel;

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
        listView.setAdapter(new ListViewAdapater(context, test, test, test, test));
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
    
}
