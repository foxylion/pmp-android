package de.unistuttgart.ipvs.pmp.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class AttributeSetUtil {
    
    public static final String NAMESPACE = "http://schemas.android.com/res/de.unistuttgart.ipvs.pmp";
    
    /*
     * <declare-styleable name="BasicTitleView">
     *    <attr format="reference" name="icon" />
     *    <attr format="string" name="name" />
     *    <attr format="boolean" name="backButton" />
     *    <attr format="color" name="textColor" />
     *    <attr format="color" name="borderColor" />
     *</declare-styleable>
     */
    public static final String ViewBasicTitle_icon = "icon";
    public static final String ViewBasicTitle_name = "name";
    public static final String ViewBasicTitle_backButton = "backButton";
    public static final String ViewBasicTitle_textColor = "textColor";
    public static final String ViewBasicTitle_borderColor = "borderColor";
    
    private AttributeSet attrs;
    
    private Context context;
    
    
    public AttributeSetUtil(Context context, AttributeSet attrs) {
        this.context = context;
        this.attrs = attrs;
    }
    
    
    public String getString(String name) {
        String text = null;
        if (this.attrs.getAttributeResourceValue(NAMESPACE, name, -1) == -1) {
            text = this.attrs.getAttributeValue(NAMESPACE, name);
        } else {
            text = this.context.getString(this.attrs.getAttributeResourceValue(NAMESPACE, name, -1));
        }
        
        return text;
    }
    
    
    public boolean getBoolean(String name, boolean defaultValue) {
        return this.attrs.getAttributeBooleanValue(NAMESPACE, name, defaultValue);
    }
    
    
    public int getColor(String name, int defaultColor) {
        int color;
        if (this.attrs.getAttributeResourceValue(NAMESPACE, name, -1) == -1) {
            color = this.attrs.getAttributeIntValue(NAMESPACE, name, defaultColor);
        } else {
            color = this.context.getResources().getColor(this.attrs.getAttributeResourceValue(NAMESPACE, name, -1));
        }
        
        return color;
    }
    
    
    public Drawable getDrawable(String name, int defaultDrawableResource) {
        if (this.attrs.getAttributeResourceValue(NAMESPACE, name, -1) != -1) {
            return this.context.getResources().getDrawable(
                    this.attrs.getAttributeResourceValue(NAMESPACE, name, defaultDrawableResource));
        }
        return null;
    }
    
    
    public Drawable getDrawable(String name, Drawable defaultDrawable) {
        if (this.attrs.getAttributeResourceValue(NAMESPACE, name, -1) != -1) {
            return this.context.getResources().getDrawable(this.attrs.getAttributeResourceValue(NAMESPACE, name, -1));
        }
        return defaultDrawable;
    }
}
