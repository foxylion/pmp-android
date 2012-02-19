package de.unistuttgart.ipvs.pmp.gui.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;

/**
 * The {@link DialogConfirmDelete} can be used to confirm a delete or remove action which should be made carefully.
 * 
 * @author Jakob Jarosch
 */
public class DialogConfirmDelete extends android.app.Dialog {
    
    /**
     * The {@link ICallback} which is being invoked when the Dialog is dismissed.
     * 
     * @author Jakob Jarosch
     */
    public interface ICallback {
        
        /**
         * Method is called when the {@link Dialog} was dismissed.
         * 
         * @param confirmed
         *            True when the user clicked on confirm, otherwise false.
         */
        public void callback(boolean confirmed);
    }
    
    private int iconResource;
    private String title;
    private String description;
    private ICallback callback;
    
    
    /**
     * Create a new {@link DialogConfirmDelete}.
     * 
     * @param context
     *            {@link Context} required to create the {@link Dialog}.
     * @param title
     *            Title which should be displayed.
     * @param description
     *            Description which should be displayed.
     * @param callback
     *            {@link ICallback} is called on dismiss() of the {@link Dialog}.
     */
    public DialogConfirmDelete(Context context, String title, String description, ICallback callback) {
        super(context);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_delete);
        setCancelable(false);
        
        this.title = title;
        this.description = description;
        this.callback = callback;
        
        refresh();
        addListener();
    }
    
    
    /**
     * Update all UI elements.
     */
    private void refresh() {
        ((BasicTitleView) findViewById(R.id.Title)).setTitle(this.title);
        
        ((TextView) findViewById(R.id.TextView_Description)).setText(this.description);
    }
    
    
    /**
     * Add listener to clickable UI elements.
     */
    private void addListener() {
        ((Button) findViewById(R.id.Button_Confirm)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
                if (callback != null) {
                    callback.callback(true);
                }
            }
        });
        
        ((Button) findViewById(R.id.Button_Cancel)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
                if (callback != null) {
                    callback.callback(false);
                }
            }
        });
    }
    
}
