package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PickUp extends Dialog {
    
    private int userID;
    
    
    public PickUp(Context context, int userID) {
        super(context);
        setContentView(R.layout.dialog_pick_up);
        this.userID = userID;
        
        addButton();
    }
    
    
    private void addButton() {
        Button pickUp = (Button) findViewById(R.id.btn_pick_up);
        pickUp.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Controller ctrl = new Controller();
                ctrl.pick_up(Model.getInstance().getSid(), userID);
                Toast.makeText(v.getContext(), "Picked up", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
}
