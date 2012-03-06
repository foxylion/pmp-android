package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import java.util.List;

import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Listener to add multiple stopovers(destinations)
 * 
 * @author Andre Nguyen, Dang Huynh
 * 
 */
public class AddStopOverListener implements OnClickListener {
    
    private Spinner spinner;
    private LinearLayout layout;
    
    
    @Override
    public void onClick(View v) {
        
        List<Spinner> l = ViewModel.getInstance().getDestinationSpinners();
        
        if (l.size()==0 || l.size() >= l.get(0).getCount()) {
            Toast.makeText(v.getContext(), R.string.destination_max_reached, Toast.LENGTH_LONG).show();
            return;
        }
        
        View root = (View) v.getRootView();
        layout = (LinearLayout) root.findViewById(R.id.layout_dest);
        
        spinner = new Spinner(v.getContext());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.array_cities,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        // add Spinner to "StopOver-List"/Spinner-List
        l.add(spinner);
        spinner.setOnLongClickListener(new OnLongClickListener() {
            
            @Override
            public boolean onLongClick(View v) {
                for (Spinner s : ViewModel.getInstance().getDestinationSpinners()) {
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
    }
}