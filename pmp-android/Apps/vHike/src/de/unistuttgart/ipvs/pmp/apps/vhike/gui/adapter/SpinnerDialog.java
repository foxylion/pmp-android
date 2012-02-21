package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * SpinnerDialog contains only one button to remove a stop over
 * 
 * @author Andre Nguyen
 * 
 */
public class SpinnerDialog extends Dialog {
    
    public SpinnerDialog(Context context) {
        super(context);
        setTitle("Remove destination");
        setContentView(R.layout.dialog_remove_dest);
        
        Button remove = (Button) findViewById(R.id.btn_remove_dest);
        remove.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (ViewModel.getInstance().getSpinners().size() > 1) {
                    ViewModel.getInstance().getClickedSpinner().setVisibility(View.GONE);
                    ViewModel.getInstance().getSpinners().remove(ViewModel.getInstance().getClickedSpinner());
                    Log.i(this, "Spinners size: " + ViewModel.getInstance().getSpinners().size());
                } else {
                    Toast.makeText(v.getContext(), "At least one destination must be given", Toast.LENGTH_SHORT).show();
                }
                cancel();
            }
        });
    }
    
}
