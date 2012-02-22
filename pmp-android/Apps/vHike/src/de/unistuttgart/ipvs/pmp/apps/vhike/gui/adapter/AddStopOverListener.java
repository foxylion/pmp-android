package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * Listener to add multiple stopvers(destinations)
 * 
 * @author Andre Nguyen
 * 
 */
public class AddStopOverListener implements OnClickListener {
    
    private Spinner spinner;
    private LinearLayout layout;
    
    
    @Override
    public void onClick(View v) {
        
        View parent = (View) v.getParent();
        layout = (LinearLayout) parent.findViewById(R.id.layout_dest);
        
        // "remove" add-Button temprarily
        layout.removeView(v);
        
        spinner = new Spinner(v.getContext());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.array_cities,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // add Spinner to "StopOver-List"/Spinner-List
        ViewModel.getInstance().getSpinners().add(spinner);
        spinner.setOnLongClickListener(new OnLongClickListener() {
            
            @Override
            public boolean onLongClick(View v) {
                for (Spinner s : ViewModel.getInstance().getSpinners()) {
                    if (s == (Spinner) v) {
                        ViewModel.getInstance().setClickedSpinner(s);
                    }
                }
                vhikeDialogs.getInstance().spDialog(v.getContext()).show();
                return false;
            }
            
        });
        
        // add to layout 
        layout.addView(spinner);
        
        // readd add-Button
        layout.addView(v);
        
    }
    
}
