package de.unistuttgart.ipvs.pmp.resource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import android.content.Context;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevel;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevelValueException;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceRegistration;
import de.unistuttgart.ipvs.pmp.service.resource.ResourceGroupService;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * <p>
 * A resource group that bundles {@link Resource}s and {@link PrivacyLevel}s. You can register them by using the methods
 * {@link ResourceGroup#registerResource(String, Resource)} and
 * {@link ResourceGroup#registerPrivacyLevel(String, PrivacyLevel)}.
 * </p>
 * 
 * <p>
 * In order to work, a ResourceGroup needs a service defined in the manifest file which simply is
 * {@link ResourceGroupService}, and the app containing the ResourceGroup and its service must extend
 * {@link ResourceGroupApp}.
 * </p>
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class ResourceGroup {
    
    /**
     * Stores the associated signee.
     */
    private PMPSignee signee;
    
    /**
     * The resources present in that resource group.
     */
    private final Map<String, Resource> resources;
    
    /**
     * The privacy levels present in that resource group.
     */
    private final Map<String, PrivacyLevel<?>> privacyLevels;
    
    
    /**
     * Creates a new {@link ResourceGroup}.
     * 
     * @param serviceContext
     *            context of the service for this resource group
     */
    public ResourceGroup(Context serviceContext) {
        this.signee = new PMPSignee(PMPComponentType.RESOURCE_GROUP, getServiceAndroidName(), serviceContext);
        
        this.resources = new HashMap<String, Resource>();
        this.privacyLevels = new HashMap<String, PrivacyLevel<?>>();
    }
    
    
    /**
     * 
     * @param locale
     *            the ISO-639 locale string available from {@link Locale#getLanguage()}
     * @return the name of this resource group for the given locale
     */
    public abstract String getName(String locale);
    
    
    /**
     * 
     * @param locale
     *            the ISO-639 locale string available from {@link Locale#getLanguage()}
     * @return the description of this resource group for the given locale
     */
    public abstract String getDescription(String locale);
    
    
    /**
     * Overwrite this method to return the <b>exact same</b> identifier you have put in the manifest file for the
     * service for this Resource Group: &lt;service>...&lt;intent-filter>...&lt;action android:name="<b>HERE</b>">. If
     * the identifier differ, the service will not work.
     * 
     * @return the specified identifier
     */
    protected abstract String getServiceAndroidName();
    
    
    /**
     * 
     * @return the signee
     */
    public PMPSignee getSignee() {
        return this.signee;
    }
    
    
    /**
     * Registers resource as resource "identifier" in this resource group.
     * 
     * @param identifier
     * @param resource
     */
    public void registerResource(String identifier, Resource resource) {
        resource.assignResourceGroup(this);
        this.resources.put(identifier, resource);
    }
    
    
    /**
     * 
     * @param identifier
     * @return the resource identified by "identifier", if present, null otherwise
     */
    public Resource getResource(String identifier) {
        return this.resources.get(identifier);
    }
    
    
    /**
     * 
     * @return a list of all the valid resource identifiers.
     */
    public List<String> getResources() {
        return new ArrayList<String>(this.resources.keySet());
    }
    
    
    /**
     * Registers privacyLevel as privacy level "identifier" in this resource group.
     * 
     * @param identifier
     * @param privacyLevel
     */
    public void registerPrivacyLevel(String identifier, PrivacyLevel<?> privacyLevel) {
        this.privacyLevels.put(identifier, privacyLevel);
    }
    
    
    /**
     * 
     * @param identifier
     * @return the privacy level identified by "identifier", if present, null otherwise
     */
    public PrivacyLevel<?> getPrivacyLevel(String identifier) {
        return this.privacyLevels.get(identifier);
    }
    
    
    /**
     * 
     * @return a list of all the valid resource identifiers.
     */
    public List<String> getPrivacyLevels() {
        return new ArrayList<String>(this.privacyLevels.keySet());
    }
    
    
    /**
     * Used for getting changed access rules from the {@link ResourceGroupService}. <b>Do not call this method yourself
     * or allow a different context to call it.</b>
     * 
     * @param privacyLevelIdentifier
     *            the identifier of the privacy level to update
     * @param privacyLevelValues
     *            a map from appIdentifier to privacy level value.
     * @throws PrivacyLevelValueException
     *             if any of the supplied values did not match the privacy levels criteria
     */
    public final void updateAccess(String privacyLevelIdentifier, Map<String, String> privacyLevelValues)
            throws PrivacyLevelValueException {
        PrivacyLevel<?> pl = getPrivacyLevel(privacyLevelIdentifier);
        if (pl == null) {
            Log.e("Should update access for a privacy level which does not exist.");
        } else {
            pl.setValues(privacyLevelValues);
            
            savePrivacyLevels(privacyLevelIdentifier, privacyLevelValues);
        }
    }
    
    
    /**
     * Effectively starts this resource group, loads all previously known privacy level values and registers it with
     * PMP. Note that it needs to be "connected" to a {@link ResourceGroupService} via a {@link ResourceGroupApp}. You
     * can implement reacting to the result of this operation by implementing onRegistrationSuccess() or
     * onRegistrationFailed()
     * 
     * @param context
     *            {@link Context} to use for the connection
     * 
     */
    protected void start(Context context) {
        
        // load saved privacy level values
        for (Entry<String, PrivacyLevel<?>> e : this.privacyLevels.entrySet()) {
            try {
                Map<String, String> values = loadPrivacyLevel(e.getKey());
                if (values != null) {
                    e.getValue().setValues(values);
                }
            } catch (PrivacyLevelValueException e1) {
                Log.e("Saved values for " + e.getKey() + " were parsed with " + e1.toString() + " thrown.");
            }
        }
        
        // connect to PMP
        final PMPServiceConnector pmpsc = new PMPServiceConnector(context, this.signee);
        
        pmpsc.addCallbackHandler(new IConnectorCallback() {
            
            @Override
            public void disconnected() {
            }
            
            
            @Override
            public void connected() {
                if (!pmpsc.isRegistered()) {
                    // register with PMP
                    IPMPServiceRegistration ipmpsr = pmpsc.getRegistrationService();
                    try {
                        byte[] pmpPublicKey = ipmpsr.registerResourceGroup(ResourceGroup.this.signee
                                .getLocalPublicKey());
                        
                        // save the returned public key to be PMP's
                        ResourceGroup.this.signee.setRemotePublicKey(PMPComponentType.PMP, Constants.PMP_IDENTIFIER,
                                pmpPublicKey);
                        
                    } catch (RemoteException e) {
                        Log.e("RemoteException during registering resource group", e);
                    }
                }
                pmpsc.unbind();
            }
            
            
            @Override
            public void bindingFailed() {
                Log.e("Binding failed during registering resource group.");
            }
        });
        pmpsc.bind();
    }
    
    
    /**
     * This saves the privacy level values specified into a local file.
     * 
     * @param privacyLevelIdentifier
     *            identifier of the privacy level
     * @param privacyLevelValues
     *            the values in a map using app identifier => privacy level value
     */
    private void savePrivacyLevels(String privacyLevelIdentifier, Map<String, String> privacyLevelValues) {
        Properties props = new Properties();
        props.putAll(privacyLevelValues);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(privacyLevelIdentifier);
            props.storeToXML(fos, null);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(e.toString() + " during saving privacy level values of " + privacyLevelIdentifier + ".");
        } catch (IOException e) {
            Log.e(e.toString() + " during saving privacy level values of " + privacyLevelIdentifier + ".");
        }
    }
    
    
    /**
     * Loads the privacy level values of privacyLevelIdentifier and returns them.
     * 
     * @param privacyLevelIdentifier
     *            the identifier of the privacy level
     * @return a map containing the privacy level values in the fashion app identifier => privacy level value, or null
     *         if no data was found
     */
    private Map<String, String> loadPrivacyLevel(String privacyLevelIdentifier) {
        Properties props = new Properties();
        try {
            FileInputStream fis = new FileInputStream(privacyLevelIdentifier);
            props.loadFromXML(fis);
            fis.close();
            
            Map<String, String> result = new HashMap<String, String>();
            for (Entry<Object, Object> e : props.entrySet()) {
                result.put((String) e.getKey(), (String) e.getValue());
            }
            return result;
        } catch (FileNotFoundException e) {
            Log.e(e.toString() + " during loading privacy level values of " + privacyLevelIdentifier + ".");
        } catch (IOException e) {
            Log.e(e.toString() + " during loading privacy level values of " + privacyLevelIdentifier + ".");
        }
        
        return null;
    }
    
    
    /**
     * Callback called when the preceding call to start() registered this resource group successfully with PMP.
     */
    public abstract void onRegistrationSuccess();
    
    
    /**
     * Callback called when the preceding call to start() could not register this resource group with PMP due to errors.
     * 
     * @param message
     *            returned message from the PMP service
     */
    public abstract void onRegistrationFailed(String message);
}
