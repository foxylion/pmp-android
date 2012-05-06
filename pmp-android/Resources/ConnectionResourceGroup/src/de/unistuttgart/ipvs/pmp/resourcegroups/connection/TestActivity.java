/*
 * Copyright 2012 pmp-android development team
 * Project: ConnectionResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.connection;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.DBConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.EventEnum;

/**
 * @author Thorsten
 * 
 */
public class TestActivity extends Activity {
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.setContentView(R.layout.main);
        DBConnector.getInstance(this).open();
        DBConnector.getInstance(this).storeWifiEvent(100, EventEnum.ON, "bla");
        DBConnector.getInstance(this).storeWifiEvent(110, EventEnum.OFF, "bla");
        DBConnector.getInstance(this).storeWifiEvent(120, EventEnum.ON, "bla");
        DBConnector.getInstance(this).storeWifiEvent(150, EventEnum.OFF, "bla");
        DBConnector.getInstance(this).storeWifiEvent(160, EventEnum.ON, "bla");
        DBConnector.getInstance(this).storeWifiEvent(170, EventEnum.OFF, "bla");
        
        DBConnector.getInstance(this).storeWifiEvent(170, EventEnum.OFF, null);
        DBConnector.getInstance(this).storeWifiEvent(170, EventEnum.OFF, null);
        
        List<String> res = DBConnector.getInstance(this).getConnectedCities(DBConstants.TABLE_WIFI);
        for (String res2 : res) {
            Toast.makeText(this, res2, Toast.LENGTH_LONG).show();
        }
    }
}
