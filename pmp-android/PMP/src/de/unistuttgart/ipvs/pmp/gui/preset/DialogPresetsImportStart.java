package de.unistuttgart.ipvs.pmp.gui.preset;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.PresetSetTools;
import de.unistuttgart.ipvs.pmp.gui.util.PresetSetTools.ICallback;
import de.unistuttgart.ipvs.pmp.gui.util.PresetSetTools.ICallbackDownload;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;

public class DialogPresetsImportStart extends Dialog {
    
    private ICallback callback;
    
    
    public DialogPresetsImportStart(Context context, ICallback callback) {
        super(context);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_presets_import_start);
        
        this.callback = callback;
        
        addListener();
    }
    
    
    private void addListener() {
        ((Button) findViewById(R.id.Button_Confirm)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                String id = ((EditText) findViewById(R.id.EditText_ID)).getText().toString();
                
                PresetSetTools.downloadPresetSet(getContext(), id, new ICallbackDownload() {
                    
                    @Override
                    public void ended(final IPresetSet presetSet) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            
                            @Override
                            public void run() {
                                if (presetSet == null) {
                                    Toast.makeText(getContext(),
                                            getContext().getString(R.string.presets_import_download_failed),
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    dismiss();
                                    new DialogPresetsImportExport(getContext(), presetSet, callback).show();
                                }
                            }
                        });
                    }
                });
            }
        });
        
        ((Button) findViewById(R.id.Button_Cancel)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
