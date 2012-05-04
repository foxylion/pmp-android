package de.unistuttgart.ipvs.pmp.gui.context;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.privacysetting.ViewPrivacySettingPreset;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.IContextAnnotation;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

public class DialogConflictingContexts extends Dialog {
    
    private IContextAnnotation contextAnnotation;
    private ViewPrivacySettingPreset vpsp;
    
    
    public DialogConflictingContexts(Context context, IContextAnnotation contextAnnotation,
            ViewPrivacySettingPreset vpsp) {
        super(context);
        
        this.contextAnnotation = contextAnnotation;
        this.vpsp = vpsp;
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_context_conflicting);
        
        refresh();
        addListener();
    }
    
    
    private void refresh() {
        List<IPreset> presets = new ArrayList<IPreset>();
        for (IPreset pr : ModelProxy.get().getPresets()) {
            if (!pr.equals(this.contextAnnotation.getPreset())
                    && (this.contextAnnotation.isPrivacySettingConflicting(pr) || this.contextAnnotation
                            .getConflictingContextAnnotations(pr).size() > 0)) {
                presets.add(pr);
            }
        }
        
        ListView listView = (ListView) findViewById(R.id.ListView_PresetConflicts);
        AdapterConflictingPresets adapter = new AdapterConflictingPresets(getContext(), this.contextAnnotation, presets);
        listView.setAdapter(adapter);
    }
    
    
    private void addListener() {
        ((Button) findViewById(R.id.Button_Close)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                DialogConflictingContexts.this.vpsp.refresh();
                dismiss();
            }
        });
    }
    
}
