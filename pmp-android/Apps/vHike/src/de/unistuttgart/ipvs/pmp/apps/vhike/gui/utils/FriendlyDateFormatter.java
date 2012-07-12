/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
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
package de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils;

import java.util.Calendar;

import android.content.Context;
import android.text.format.DateFormat;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;

public class FriendlyDateFormatter {
    
    private Context context;
    
    
    public FriendlyDateFormatter(Context context) {
        this.context = context;
    }
    
    
    public String format(Calendar date) {
        
        Calendar d1 = Calendar.getInstance(); // Today at 0:0:00
        d1.set(Calendar.HOUR, 0);
        d1.set(Calendar.MINUTE, 0);
        d1.set(Calendar.SECOND, 0);
        d1.set(Calendar.MILLISECOND, 0);
        
        Calendar d2 = Calendar.getInstance(); // Tomorrow
        d2.setTimeInMillis(d1.getTimeInMillis() + 86400000);
        
        Calendar d3 = Calendar.getInstance(); // Tomorrow + 24h
        d3.setTimeInMillis(d2.getTimeInMillis() + 86400000);
        
        String result = "";
        
        if (date.compareTo(d3) > 0 || date.before(d1)) {
            result = DateFormat.format("E", date).toString() + " ";
            result += java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT,
                    java.text.DateFormat.SHORT).format(date.getTime());
        } else if (date.after(d1) && date.before(Calendar.getInstance())) {
            result = this.context.getString(R.string.now);
        } else {
            if (date.after(d1) && date.before(d2)) {
                result = this.context.getString(R.string.Today);
            } else if (date.after(d2) && date.before(d3)) {
                result = this.context.getString(R.string.tomorrow);
            }
            result += " "
                    + java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(date.getTime());
        }
        
        return result;
    }
}
