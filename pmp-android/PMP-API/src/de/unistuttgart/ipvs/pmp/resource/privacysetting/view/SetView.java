package de.unistuttgart.ipvs.pmp.resource.privacysetting.view;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
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
    
    private LinearLayout mainContainer;
    
    private List<IPrivacySettingView<T>> views = new ArrayList<IPrivacySettingView<T>>();
    
    private Map<IPrivacySettingView<T>, LinearLayout> viewContainers = new HashMap<IPrivacySettingView<T>, LinearLayout>();
    
    private Constructor<? extends IPrivacySettingView<T>> childViewConstructor;
    private Object[] childViewConstructorInvocation;
    
    
    public SetView(Context context, Constructor<? extends IPrivacySettingView<T>> childViewConstructor,
            Object[] childViewConstructorInvocation) {
        super(context);
        
        this.childViewConstructor = childViewConstructor;
        this.childViewConstructorInvocation = childViewConstructorInvocation;
        
        setOrientation(LinearLayout.VERTICAL);
        
        /* Create a new main Container */
        this.mainContainer = new LinearLayout(context);
        this.mainContainer.setOrientation(LinearLayout.VERTICAL);
        this.mainContainer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        addView(this.mainContainer);
        
        /* Create an Add-Icon */
        LinearLayout addButtonLayout = new LinearLayout(getContext());
        ImageView addButtonImage = new ImageView(getContext());
        addButtonImage.setImageResource(R.drawable.pmp_api_icon_add_small);
        addButtonLayout.addView(addButtonImage);
        TextView addButtonText = new TextView(getContext());
        addButtonText.setText(getContext().getString(R.string.pmp_api_add_item));
        addButtonLayout.addView(addButtonText);
        LayoutParams addButtonTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addButtonTextParams.gravity = Gravity.CENTER_VERTICAL;
        addButtonText.setLayoutParams(addButtonTextParams);
        addButtonLayout.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                try {
                    addSubView(null);
                } catch (PrivacySettingValueException e) {
                    Log.e(SetView.this, "Can't add a new view with null as start value.");
                }
            }
        });
        
        addView(addButtonLayout);
    }
    
    
    @Override
    public View asView() {
        return this;
    }
    
    
    @Override
    public void setViewValue(Set<T> values) throws PrivacySettingValueException {
        /* Remove all currently displayed views. Creating a temporary new ArrayList to prevent a ConcurrentModificationException. */
        List<IPrivacySettingView<T>> views = new ArrayList<IPrivacySettingView<T>>(this.views);
        for (IPrivacySettingView<T> view : views) {
            removeView(view);
        }
        
        for (T value : values) {
            addSubView(value);
        }
    }
    
    
    @Override
    public Set<T> getViewValue() {
        Set<T> set = new HashSet<T>();
        
        for (IPrivacySettingView<T> view : this.views) {
            set.add(view.getViewValue());
        }
        
        return set;
    }
    
    
    private void addSubView(T value) throws PrivacySettingValueException {
        /* Create a new view and assign value */
        final IPrivacySettingView<T> newView = createSubView();
        newView.setViewValue(value);
        
        /* Create a horizontal linear layout which will contain the new view and a remove button */
        LinearLayout container = new LinearLayout(getContext());
        container.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
        /* Add the new view */
        View realView = newView.asView();
        LayoutParams realViewParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        realViewParams.weight = 1.0f;
        realView.setLayoutParams(realViewParams);
        
        container.addView(realView);
        
        /* Create a remove button */
        ImageView removeIcon = new ImageView(getContext());
        removeIcon.setImageResource(R.drawable.pmp_api_icon_delete_small);
        removeIcon.setClickable(true);
        LayoutParams removeIconParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        removeIconParams.weight = 1.0f;
        removeIconParams.gravity = Gravity.CENTER_VERTICAL;
        removeIcon.setLayoutParams(removeIconParams);
        removeIcon.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                removeView(newView);
            }
        });
        
        container.addView(removeIcon);
        
        /* Add the created elements to the SetView model */
        views.add(newView);
        viewContainers.put(newView, container);
        
        /* Add the new view to the mainContainer, to be displayed. */
        mainContainer.addView(container);
    }
    
    
    private void removeView(IPrivacySettingView<T> view) {
        mainContainer.removeView(viewContainers.get(view));
        viewContainers.remove(view);
        views.remove(view);
    }
    
    
    private IPrivacySettingView<T> createSubView() throws PrivacySettingValueException {
        try {
            if (this.childViewConstructorInvocation.length == 0) {
                return this.childViewConstructor.newInstance(getContext());
            } else {
                return this.childViewConstructor.newInstance(getContext(), this.childViewConstructorInvocation);
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
    
}
