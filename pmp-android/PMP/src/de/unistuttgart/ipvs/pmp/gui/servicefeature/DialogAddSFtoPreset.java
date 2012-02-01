package de.unistuttgart.ipvs.pmp.gui.servicefeature;

import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.preset.AdapterPresets;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;

public class DialogAddSFtoPreset extends Dialog {
    
    /**
     * 
     * @param context
     *            Context which is used to create the Dialog
     * @param dialog
     *            The dialog which invoked this.
     */
    public DialogAddSFtoPreset(Context context, final DialogServiceFeature dialog) {
        super(context);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.dialog_add_sf_to_preset);
        
        ListView lv = (ListView) findViewById(R.id.ListView_Presets);
        final List<IPreset> presets = Arrays.asList(ModelProxy.get().getPresets());
        
        if (ModelProxy.get().getPresets().length == 0) {
            ((TextView) findViewById(R.id.Presets_Text_View_No_Presets_Existing)).setVisibility(View.VISIBLE);
        } else {
            lv.setAdapter(new AdapterPresets(context, presets));
        }
        
        lv.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                /* Update the selected Preset */
                IPreset preset = presets.get(position);
                IServiceFeature serviceFeature = dialog.getServiceFeature();
                preset.startUpdate();
                preset.assignApp(serviceFeature.getApp());
                preset.assignServiceFeature(serviceFeature);
                preset.endUpdate();
                
                /* Close the Dialog and close the underlying one as well */
                dismiss();
                dialog.refresh();
                dialog.dismiss();
            }
        });
        
        addListener();
    }
    
    
    private void addListener() {
        ((Button) findViewById(R.id.Button_Cancel)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
