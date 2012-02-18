package de.unistuttgart.ipvs.pmp.gui.setting;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class SettingListItem extends LinearLayout {
    
    /**
     * Constructor
     * 
     * @param context
     *            the context
     * @param setting
     *            the current {@link SettingAbstract}
     */
    public SettingListItem(Context context, SettingAbstract<?> setting) {
        super(context);
        addView(setting.getView(context));
    }
    
}
