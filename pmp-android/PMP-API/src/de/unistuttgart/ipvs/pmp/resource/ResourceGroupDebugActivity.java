/*
 * Copyright 2011 pmp-android development team
 * Project: SwitchesResourceGroup
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
package de.unistuttgart.ipvs.pmp.resource;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;

public class ResourceGroupDebugActivity extends Activity {
    
    private Dialog d;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final Map<String, ResourceGroup> pls = new HashMap<String, ResourceGroup>();
        final List<String> plList = new ArrayList<String>();
        if (!(getApplication() instanceof ResourceGroupApp)) {
            Log.e("ResourceGroupActivity started without ResourceGroupApp.");
            return;
        } else {
            for (ResourceGroup rg : ((ResourceGroupApp) getApplication()).getAllResourceGroups()) {
                plList.addAll(rg.getPrivacyLevels());
                for (String pl : rg.getPrivacyLevels()) {
                    pls.put(pl, rg);
                }
            }
        }
        
        ListView list = new ListView(this);
        list.setAdapter(new BaseAdapter() {
            
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv;
                if ((convertView == null) || (!(convertView instanceof TextView))) {
                    tv = new TextView(ResourceGroupDebugActivity.this);
                } else {
                    tv = (TextView) convertView;
                }
                tv.setText(plList.get(position));
                return tv;
            }
            
            
            @Override
            public long getItemId(int position) {
                return position;
            }
            
            
            @Override
            public Object getItem(int position) {
                return plList.get(position);
            }
            
            
            @Override
            public int getCount() {
                return plList.size();
            }
        });
        list.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResourceGroup rg = pls.get(plList.get(position));
                
                try {
                    FileInputStream fis = rg.getSignee().getContext().openFileInput(plList.get(position));
                    Properties props = new Properties();
                    props.loadFromXML(fis);
                    fis.close();
                    
                    StringBuilder sb = new StringBuilder();
                    for (Entry<Object, Object> e : props.entrySet()) {
                        sb.append(e.getKey() + " = '" + e.getValue() + "'" + System.getProperty("line.separator"));
                    }
                    
                    TextView tv = new TextView(ResourceGroupDebugActivity.this);
                    tv.setText(sb.toString());
                    
                    ResourceGroupDebugActivity.this.d = new Dialog(ResourceGroupDebugActivity.this);
                    ResourceGroupDebugActivity.this.d.setContentView(tv);
                    ResourceGroupDebugActivity.this.d.show();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        });
        setContentView(list);
    }
    
    
    @Override
    protected void onPause() {
        if (this.d != null) {
            this.d.dismiss();
        }
        super.onPause();
    }
}
