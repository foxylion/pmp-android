package de.unistuttgart.ipvs.pmp.gui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.AppsAdapter;
import de.unistuttgart.ipvs.pmp.gui.placeholder.App;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

public class AppsActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.pmp_apps);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        /* Temporary bad stuff, to Test the Activity */
        
        List<IApp> apps = new ArrayList<IApp>();
        apps.add(new App("Barcode Scanner",
                "Barcode Scanner can scan barcodes and view details about the scanned product.",
                loadBitmap(R.drawable.test_icon1)));
        apps.add(new App("Calendar", "The Calendar allows you to manage your appointments.",
                loadBitmap(R.drawable.test_icon2)));
        apps.add(new App("Facebook", "With this App you can surf on your facebook wall.",
                loadBitmap(R.drawable.test_icon3)));
        apps.add(new App("Google Mail", "With Google Mail you can manage your mails.",
                loadBitmap(R.drawable.test_icon4)));
        apps.add(new App("IMDb", "IMDb gives you access to the greatest movie database worldwide.",
                loadBitmap(R.drawable.test_icon5)));
        apps.add(new App("SMS", "Use the App to send short messages to your friends.",
                loadBitmap(R.drawable.test_icon6)));
        apps.add(new App("TV App", "Brings all the TV channels to your mobile device.",
                loadBitmap(R.drawable.test_icon7)));
        apps.add(new App("Compass", "Gives you the ability to get your current position.",
                loadBitmap(R.drawable.test_icon8)));
        apps.add(new App("RSS Feeds", "Access news feeds directly without any troubles.",
                loadBitmap(R.drawable.test_icon9)));
        apps.add(new App("Wikipedia", "Become access to the greatest lexica in the world.",
                loadBitmap(R.drawable.test_icon10)));
        
        ListView appsList = (ListView) findViewById(R.id.ListView_Apps);
        appsList.setClickable(true);
        
        AppsAdapter appsAdapter = new AppsAdapter(this, apps);
        appsList.setAdapter(appsAdapter);
        
        appsList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(AppsActivity.this, "Tapped on item " + arg2, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AppsActivity.this, AppActivity.class);
                AppsActivity.this.startActivity(i);
            }
        });
    }
    
    
    private Drawable loadBitmap(int id) {
        return getResources().getDrawable(id);
    }
}
