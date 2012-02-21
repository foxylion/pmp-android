package de.unistuttgart.ipvs.pmp.gui.preset;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import de.unistuttgart.ipvs.pmp.R;

public class DialogPresetExportEnd extends Dialog {
    
    private String id;
    
    
    public DialogPresetExportEnd(Context context, String id) {
        super(context);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_presets_export_end);
        
        this.id = id;
        
        refresh();
        addListener();
    }
    
    
    private void refresh() {
        ((EditText) findViewById(R.id.EditText_ID)).setText(this.id);
    }
    
    
    private void addListener() {
        ((Button) findViewById(R.id.Button_Email)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Send the ID via email.
            }
        });
        
        ((Button) findViewById(R.id.Button_Close)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    
}
