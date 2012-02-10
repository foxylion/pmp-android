package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class RateProfileConfirm extends Dialog {
    
    public RateProfileConfirm(Context context) {
        super(context);
        setContentView(R.layout.dialog_rate_profile);
        
        Button cancel = (Button) findViewById(R.id.btn_cancel_rate);
        cancel.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        
        Button confirm = (Button) findViewById(R.id.btn_ok_rate);
        confirm.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // rate
//                Controller ctrl = new Controller();
//                ctrl.
                cancel();
            }
        });
    }
    
}
