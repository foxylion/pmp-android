package de.unistuttgart.ipvs.pmp.gui.context;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.privacysetting.DialogPrivacySettingEdit;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.util.view.AlwaysClickableButton;
import de.unistuttgart.ipvs.pmp.model.context.IContext;
import de.unistuttgart.ipvs.pmp.model.context.IContextView;
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.IContextAnnotation;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidConditionException;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

public class DialogContextChange extends Dialog {
    
    public interface ICallback {
        
        public void callback();
    }
    
    private IPreset preset;
    private IPrivacySetting privacySetting;
    private IContextAnnotation contextAnnotation;
    private String contextCondition;
    private String overrideValue;
    private IContext usedContext;
    private IContextView usedView;
    private ICallback callback;
    
    
    public DialogContextChange(Context context, IPreset preset, IPrivacySetting privacySetting,
            IContextAnnotation contextAnnotation, ICallback callback) {
        super(context);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_context_edit);
        
        this.preset = preset;
        this.privacySetting = privacySetting;
        this.contextAnnotation = contextAnnotation;
        this.callback = callback;
        
        if (contextAnnotation != null) {
            this.contextCondition = contextAnnotation.getContextCondition();
            this.overrideValue = contextAnnotation.getOverridePrivacySettingValue();
            this.usedContext = contextAnnotation.getContext();
            showDialog();
        } else {
            showContextTypeSelectionDialog();
        }
        
        refresh();
        addListener();
    }
    
    
    @Override
    public void show() {
        /* Ignore the show command... */
    }
    
    
    private void showDialog() {
        super.show();
    }
    
    
    private void showContextTypeSelectionDialog() {
        final List<IContext> contexts = ModelProxy.get().getContexts();
        String[] contextStrings = new String[contexts.size()];
        
        for (int i = 0; i < contexts.size(); i++) {
            contextStrings[i] = contexts.get(i).getName();
        }
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select a context type");
        builder.setItems(contextStrings, new OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                usedContext = contexts.get(which);
                refresh();
                showDialog();
            }
        });
        builder.setOnCancelListener(new OnCancelListener() {
            
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                dismiss();
            }
        });
        builder.create().show();
    }
    
    
    private void refresh() {
        ((Button) findViewById(R.id.Button_Delete)).setEnabled((contextAnnotation != null));
        
        if (usedContext != null) {
            if (usedView == null) {
                usedView = usedContext.getView(getContext());
            }
            
            try {
                if (contextCondition != null) {
                    usedView.setViewCondition(contextCondition);
                }
            } catch (InvalidConditionException e) {
                Log.e(this, "The condition which should be assigned seems to be an invalid one.", e);
            }
            
            ((LinearLayout) findViewById(R.id.LinearLayout_Context)).removeAllViews();
            ((LinearLayout) findViewById(R.id.LinearLayout_Context)).addView(usedView.asView());
        }
    }
    
    
    private void addListener() {
        ((Button) findViewById(R.id.Button_ChangePS)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new DialogPrivacySettingEdit(getContext(), privacySetting, overrideValue,
                        new DialogPrivacySettingEdit.ICallback() {
                            
                            @Override
                            public void result(boolean changed, String newValue) {
                                if (changed) {
                                    overrideValue = newValue;
                                    refresh();
                                }
                            }
                        }).show();
            }
        });
        
        ((AlwaysClickableButton) findViewById(R.id.Button_Save)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (overrideValue == null) {
                    Toast.makeText(getContext(),
                            "Please first configure the Privacy Setting over the 'Change value' button.",
                            Toast.LENGTH_LONG).show();
                } else {
                    contextCondition = usedView.getViewCondition();
                    
                    try {
                        /* First add, then delete, 'cause if something went wrong during assign the old value will still be set. */
                        preset.assignContextAnnotation(privacySetting, usedContext, contextCondition, overrideValue);
                        
                        if (contextAnnotation != null) {
                            preset.removeContextAnnotation(privacySetting, contextAnnotation);
                        }
                    } catch (InvalidConditionException e) {
                        Log.e(DialogContextChange.this, "Couldn't set new value for ContextAnnotaion, ICE", e);
                        GUITools.showToast(getContext(),
                                getContext().getString(R.string.failure_invalid_context_value), Toast.LENGTH_LONG);
                    } catch (PrivacySettingValueException e) {
                        Log.e(DialogContextChange.this, "Couldn't set new value for PrivacySetting, PSVE", e);
                        GUITools.showToast(getContext(), getContext().getString(R.string.failure_invalid_ps_value),
                                Toast.LENGTH_LONG);
                    }
                    
                    dismiss();
                }
            }
        });
        
        ((Button) findViewById(R.id.Button_Delete)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                preset.removeContextAnnotation(privacySetting, contextAnnotation);
                dismiss();
            }
        });
        
        ((Button) findViewById(R.id.Button_Cancel)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    
    
    @Override
    public void dismiss() {
        super.dismiss();
        
        ((LinearLayout) findViewById(R.id.LinearLayout_Context)).removeAllViews();
        
        if (callback != null) {
            callback.callback();
        }
    }
}
