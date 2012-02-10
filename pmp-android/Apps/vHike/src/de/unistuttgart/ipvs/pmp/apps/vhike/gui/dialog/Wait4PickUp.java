package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import de.unistuttgart.ipvs.pmp.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class Wait4PickUp extends Dialog {
    
    public Wait4PickUp(final Context context) {
        super(context);
        setContentView(R.layout.dialog_query_finito);
        
        Button btn = (Button) findViewById(R.id.btn_query_finish);
        btn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                cancel();
                ((Activity) context).finish();
            }
        });
    }
    
}
