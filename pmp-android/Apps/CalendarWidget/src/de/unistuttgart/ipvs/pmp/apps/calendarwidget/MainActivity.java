package de.unistuttgart.ipvs.pmp.apps.calendarwidget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView tv = new TextView(this);
        tv.setText("You can now add the Widget to one of your home screens.");
        
        addContentView(tv, new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        Intent broadcast = new Intent();
        broadcast.setAction("PMP_APP.CALENDAR_MODIFIED");
        sendBroadcast(broadcast);
    }
}
