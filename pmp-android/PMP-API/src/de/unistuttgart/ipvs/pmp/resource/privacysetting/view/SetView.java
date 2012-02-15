package de.unistuttgart.ipvs.pmp.resource.privacysetting.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.IPrivacySettingView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.SetPrivacySetting;

/**
 * {@link IPrivacySettingView} for {@link SetPrivacySetting}
 * 
 * @author Jakob Jarosch
 * 
 */
public class SetView<T extends Serializable> extends LinearLayout implements IPrivacySettingView<Set<T>> {
    
    /**
     * All child views in store
     */
    private List<IPrivacySettingView<T>> editViews;
    
    /**
     * amount of actually used views to input data
     */
    private int usedViews;
    
    private Context context;
    private Constructor<? extends IPrivacySettingView<T>> childViewConstructor;
    private Object[] childViewConstructorInvocation;
    
    
    public SetView(Context context, Constructor<? extends IPrivacySettingView<T>> childViewConstructor,
            Object[] childViewConstructorInvocation) {
        super(context);
        
        this.editViews = new ArrayList<IPrivacySettingView<T>>();
        this.usedViews = 0;
        
        this.context = context;
        this.childViewConstructor = childViewConstructor;
        this.childViewConstructorInvocation = childViewConstructorInvocation;
        
        setOrientation(LinearLayout.VERTICAL);
        
        // TODO fix layout, "add" button, "remove" button
        
        TextView description = new TextView(context);
        description.setText("Enter the entries separated by '" + "'.");
        addView(description);
        
        /*this.editText = new EditText(context);
        this.editText.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(this.editText);*/
    }
    
    
    private IPrivacySettingView<T> newView() throws PrivacySettingValueException {
        try {
            return this.childViewConstructor.newInstance(this.context, this.childViewConstructorInvocation);
        } catch (IllegalArgumentException e) {
            throw new PrivacySettingValueException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new PrivacySettingValueException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new PrivacySettingValueException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new PrivacySettingValueException(e.getMessage(), e);
        }
    }
    
    
    private void makeViews(int count) throws PrivacySettingValueException {
        // add necessary
        for (int i = this.editViews.size(); i < count; i++) {
            IPrivacySettingView<T> insert = newView();
            this.editViews.add(insert);
            addView(insert.asView());
        }
        
        // remove unnecessary
        for (int i = count; i < this.editViews.size(); i++) {
            removeView(this.editViews.get(i).asView());
        }
    }
    
    
    @Override
    public View asView() {
        return this;
    }
    
    
    @Override
    public void setViewValue(Set<T> value) throws PrivacySettingValueException {
        this.usedViews = value.size();
        makeViews(this.usedViews);
        
        int i = 0;
        for (T item : value) {
            this.editViews.get(i).setViewValue(item);
        }
    }
    
    
    @Override
    public String getViewValue() {
        Set<T> set = new HashSet<T>();
        
        for (int i = 0; i < this.usedViews; i++) {
            set.add(this.editViews.get(i).getViewValueObject());
        }
        
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                try {
                    oos.writeObject(set);
                    return baos.toString("UTF-8");
                } finally {
                    oos.close();
                }
            } finally {
                baos.close();
            }
        } catch (IOException e) {
            Log.e(this, "Could not get view value:", e);
            return "";
        }
    }
    
    
    @Override
    public Set<T> getViewValueObject() {
        throw new UnsupportedOperationException();
    }
}
