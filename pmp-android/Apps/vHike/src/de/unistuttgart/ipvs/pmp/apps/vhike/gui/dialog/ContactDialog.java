package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.RouteOverlay;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;

public class ContactDialog extends Dialog {
    
    private MapView mapView;
    
    private Button phone;
    private Button sms;
    private Button email;
    private Button route;
    
    
    public ContactDialog(Context context, MapView mapView, String userName) {
        super(context);
        setTitle(userName);
        setContentView(R.layout.dialog_contact);
        this.mapView = mapView;
        
        setButtons();
    }
    
    
    private void setButtons() {
        
        phone = (Button) findViewById(R.id.btn_phone);
        phone.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
            }
        });
        
        sms = (Button) findViewById(R.id.btn_sms);
        sms.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
            }
        });
        
        email = (Button) findViewById(R.id.btn_email);
        email.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
            }
        });
        
        route = (Button) findViewById(R.id.btn_route);
        route.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                ViewModel.getInstance().getDriverOverlayList(mapView).add(new RouteOverlay(null, null, 0));
            }
        });
    }
    
}
