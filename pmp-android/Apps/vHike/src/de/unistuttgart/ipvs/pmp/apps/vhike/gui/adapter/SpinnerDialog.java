package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;

/**
 * SpinnerDialog contains only one button to remove a stop over
 * 
 * @author Andre Nguyen
 * 
 */
public class SpinnerDialog extends Dialog {
    
    int sNumber;
    
    
    public SpinnerDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_remove_dest);
        setTitle("Remove destination");
        
        Button remove = (Button) findViewById(R.id.btn_remove_dest);
        remove.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                if (ViewModel.getInstance().getDestinationSpinners().size() > 1) {
                    
                    //                    int sNumber = ViewModel.getInstance().getClickedSpinner().getSelectedItemPosition();
                    for (int i = 0; i < ViewModel.getInstance().getDestinationSpinners().size(); i++) {
                        if (ViewModel.getInstance().getClickedSpinner() == ViewModel.getInstance()
                                .getDestinationSpinners().get(i)) {
                            SpinnerDialog.this.sNumber = i;
                        }
                    }
                    Log.i(this, "Selcted Item Pos: "
                            + ViewModel.getInstance().getClickedSpinner().getSelectedItemPosition());
                    ViewModel.getInstance().getDestinationSpinners().remove(SpinnerDialog.this.sNumber);
                    ViewModel.getInstance().getClickedSpinner().setVisibility(View.GONE);
                    Log.i(this, "Added spinner, Size"
                            + ViewModel.getInstance().getDestinationSpinners().size()
                            + ", Clicked ");
                    v.getParent().requestLayout();
                } else {
                    Toast.makeText(v.getContext(), "At least one destination must be given",
                            Toast.LENGTH_SHORT).show();
                }
                cancel();
            }
        });
    }
    
}
