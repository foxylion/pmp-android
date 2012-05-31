package de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils;

import java.text.SimpleDateFormat;
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
        d1.clear(Calendar.HOUR);
        d1.clear(Calendar.MINUTE);
        d1.clear(Calendar.MILLISECOND);
        
        Calendar d2 = Calendar.getInstance(); // Tomorrow
        d2.setTimeInMillis(d1.getTimeInMillis() + 86400000);
        
        Calendar d3 = Calendar.getInstance(); // Tomorrow + 24h
        d3.setTimeInMillis(d2.getTimeInMillis() + 86400000);
        
        String result = "";
        
        if (date.compareTo(d3) >= 0 || date.before(d1)) {
            result = DateFormat.format("E", date).toString() + " ";
            result += SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(
                    date.getTime());
        } else if (date.after(d1) && date.before(Calendar.getInstance())) {
            result = context.getString(R.string.now);
        } else {
            if (date.after(d1) && date.before(d2)) {
                result = context.getString(R.string.Today);
            } else if (date.after(d2) && date.before(d3)) {
                result = context.getString(R.string.tomorrow);
            }
            result += " " + SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(date.getTime());
        }
        
        return result;
    }
}
