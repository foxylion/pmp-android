package de.unistuttgart.ipvs.pmp.gui.context;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.IContextAnnotation;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;

public class DialogContextChange extends Dialog {
    
    public DialogContextChange(Context context, IPreset preset, IPrivacySetting privacySetting,
            IContextAnnotation anotationContext) {
        super(context);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.dialog_context_edit);
    }
}
