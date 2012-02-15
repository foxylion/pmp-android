package de.unistuttgart.ipvs.pmp.resource.privacysetting.library;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.AbstractPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.IPrivacySettingView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.view.IntegerView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.view.SetView;

/**
 * Privacy setting for set types.
 * 
 * @author Tobias Kuhn, Jakob Jarosch
 * 
 * @param <T>
 *            the {@link Serializable} type to be stored
 * @param <U>
 *            the {@link IntegerView} used to display the type
 */
public class SetPrivacySetting<T extends Serializable> extends AbstractPrivacySetting<Set<T>> {
    
    private SetView<T> view = null;
    private Constructor<? extends IPrivacySettingView<T>> childViewConstructor;
    private Object[] childViewConstructorInvocation;
    
    
    public SetPrivacySetting(Constructor<? extends IPrivacySettingView<T>> childViewConstructor,
            Object... childViewConstructorInvocation) {
        super();
        this.childViewConstructor = childViewConstructor;
        this.childViewConstructorInvocation = childViewConstructorInvocation;
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public Set<T> parseValue(String value) throws PrivacySettingValueException {
        if ((value == null) || value.equals("")) {
            return new HashSet<T>();
        }
        
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(value.getBytes("UTF-8")));
            try {
                return (Set<T>) ois.readObject();
            } finally {
                ois.close();
            }
            
        } catch (IOException e) {
            throw new PrivacySettingValueException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new PrivacySettingValueException(e.getMessage(), e);
        }
    }
    
    
    @Override
    public boolean permits(Set<T> value, Set<T> reference) {
        return value.containsAll(reference);
    }
    
    
    @Override
    public String getHumanReadableValue(String value) throws PrivacySettingValueException {
        Set<T> set = parseValue(value);
        if (set.isEmpty()) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (T t : set) {
            sb.append(t.toString());
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
    
    
    @Override
    public IPrivacySettingView<Set<T>> getView(Context context) {
        if (this.view == null) {
            this.view = new SetView<T>(context, this.childViewConstructor, this.childViewConstructorInvocation);
        }
        return this.view;
    }
    
}
