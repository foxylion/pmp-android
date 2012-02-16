package de.unistuttgart.ipvs.pmp.resource.privacysetting.view;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.IPrivacySettingView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.SetPrivacySetting;

/**
 * {@link IPrivacySettingView} for {@link SetPrivacySetting}
 * 
 * @author Jakob Jarosch
 * 
 */
public class SetView<T> extends LinearLayout implements IPrivacySettingView<Set<T>> {
    
    /**
     * All child views in store
     */
    private List<IPrivacySettingView<T>> editViews;
    
    /**
     * amount of actually used views to input data
     */
    private int usedViews;
    
    private LinearLayout viewsContainer;
    
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
        
        TextView description = new TextView(context);
        description.setText("Enter the entries separated by '" + "'.");
        addView(description);
        
        this.viewsContainer = new LinearLayout(context);
        this.viewsContainer.setOrientation(LinearLayout.VERTICAL);
        addView(this.viewsContainer);
        
        new LinearLayout(context);
        
        Button buttonAdd = new Button(context);
        buttonAdd.setText("Add");
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                try {
                    makeViews(SetView.this.usedViews + 1);
                } catch (PrivacySettingValueException e) {
                    e.printStackTrace();
                }
            }
        });
        
        Button buttonRemove = new Button(context);
        buttonRemove.setText("Remove");
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                try {
                    makeViews(SetView.this.usedViews - 1);
                } catch (PrivacySettingValueException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    
    private IPrivacySettingView<T> newView() throws PrivacySettingValueException {
        try {
            if (this.childViewConstructorInvocation.length == 0) {
                return this.childViewConstructor.newInstance(this.context);
            } else {
                return this.childViewConstructor.newInstance(this.context, this.childViewConstructorInvocation);
            }
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
            this.editViews.get(i++).setViewValue(item);
        }
    }
    
    
    @Override
    public Set<T> getViewValue() {
        Set<T> set = new HashSet<T>();
        
        for (int i = 0; i < this.usedViews; i++) {
            set.add(this.editViews.get(i).getViewValue());
        }
        
        return set;
    }
    
}
