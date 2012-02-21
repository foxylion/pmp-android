package de.unistuttgart.ipvs.pmp.gui.preset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.PresetSetTools;
import de.unistuttgart.ipvs.pmp.gui.util.PresetSetTools.ICallback;
import de.unistuttgart.ipvs.pmp.gui.util.PresetSetTools.ICallbackUpload;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.xml.InvalidPresetSetException;
import de.unistuttgart.ipvs.pmp.model.xml.XMLInterface;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;

public class DialogPresetsImportExport extends Dialog {
    
    private ICallback callback;
    
    boolean isExport;
    private List<IPreset> presets;
    private IPresetSet presetsNew;
    
    private ListView listView;
    
    
    public DialogPresetsImportExport(Context context) {
        super(context);
        
        this.isExport = true;
        this.presets = ModelProxy.get().getPresets();
        
        initializeDialog();
    }
    
    
    public DialogPresetsImportExport(Context context, IPresetSet presets, ICallback callback) {
        super(context);
        
        isExport = false;
        this.presetsNew = presets;
        this.callback = callback;
        
        initializeDialog();
    }
    
    
    private void initializeDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.dialog_presets_import_export);
        
        this.listView = (ListView) findViewById(R.id.ListView_Presets);
        
        refresh();
        addListener();
    }
    
    
    /**
     * Updates the UI.
     */
    private void refresh() {
        /*
         * Set the correct label for confirm button and display the checkbox if required.
         */
        Button confirmButton = (Button) findViewById(R.id.Button_Confirm);
        CheckBox importCheckbox = (CheckBox) findViewById(R.id.CheckBox_ImportOverride);
        if (this.isExport) {
            confirmButton.setText(getContext().getString(R.string.export_presets));
            importCheckbox.setVisibility(View.GONE);
        } else {
            confirmButton.setText(getContext().getString(R.string.import_presets));
            importCheckbox.setVisibility(View.VISIBLE);
        }
        
        /*
         * Initialize the ListView with the names of the presets.
         */
        List<String> items = new ArrayList<String>();
        if (this.isExport) {
            for (IPreset preset : this.presets) {
                items.add(preset.getName());
            }
        } else {
            for (de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset preset : this.presetsNew.getPresets()) {
                items.add(preset.getName());
            }
        }
        
        /*
         * Configure the ListView.
         */
        this.listView.setItemsCanFocus(false);
        this.listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        this.listView.setAdapter(new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_multiple_choice, items));
    }
    
    
    private boolean importPresets(boolean override) {
        /* 
         * First of all we have to remove all presets from the set the user don't like
         */
        List<de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset> newPresets = this.presetsNew.getPresets();
        int listSize = this.presetsNew.getPresets().size();
        for (int i = listSize - 1; i >= 0; i--) {
            if (!this.listView.isItemChecked(i)) {
                newPresets.remove(i);
            }
        }
        
        try {
            XMLInterface.instance.importPresets(newPresets, override);
            return true;
        } catch (InvalidPresetSetException e) {
            return false;
        }
    }
    
    
    private IPresetSet exportPresets() {
        List<IPreset> exportPresets = new ArrayList<IPreset>();
        Collections.copy(exportPresets, this.presets);
        
        int listSize = exportPresets.size();
        for (int i = listSize - 1; i >= 0; i--) {
            if (!this.listView.isItemChecked(i)) {
                exportPresets.remove(i);
            }
        }
        
        return XMLInterface.instance.exportPresets(exportPresets);
    }
    
    
    /**
     * Adds the listener to all clickable elements.
     */
    private void addListener() {
        ((Button) findViewById(R.id.Button_Confirm)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (isExport) {
                    IPresetSet presetSet = exportPresets();
                    PresetSetTools.uploadPresetSet(getContext(), presetSet, new ICallbackUpload() {
                        
                        @Override
                        public void ended(final String id) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                
                                @Override
                                public void run() {
                                    if (id != null) {
                                        dismiss();
                                        new DialogPresetExportEnd(getContext(), id).show();
                                    } else {
                                        Toast.makeText(getContext(),
                                                getContext().getString(R.string.presets_export_upload_failed),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                    
                    // TODO Upload the item.
                } else {
                    boolean override = ((CheckBox) findViewById(R.id.CheckBox_ImportOverride)).isChecked();
                    boolean importSuccess = importPresets(override);
                    if (importSuccess) {
                        Toast.makeText(getContext(), getContext().getString(R.string.presets_import_succeed),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), getContext().getString(R.string.presets_import_failed),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
            
        });
        ((Button) findViewById(R.id.Button_Cancel)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
                if (callback != null) {
                    callback.ended(false);
                }
            }
        });
    }
}
