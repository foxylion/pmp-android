package de.unistuttgart.ipvs.pmp.model.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;

import de.unistuttgart.ipvs.pmp.model.plugin.IPluginProvider;
import de.unistuttgart.ipvs.pmp.model.plugin.PluginProvider;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;

/**
 * @see IServerProvider
 * @author Tobias Kuhn
 * 
 */
public class ServerProvider implements IServerProvider {
    
    /*
     * constants
     */
    private static final String SERVER_URL = "http://???/";
    
    /*
     * fields
     */
    private IServerDownloadCallback callback;
    
    /*
     * singleton stuff
     */
    
    private static final IServerProvider instance = new ServerProvider();
    
    
    public static IServerProvider getInstance() {
        return instance;
    }
    
    
    private ServerProvider() {
        this.callback = null;
    }
    
    
    private void downloadFile(String address, OutputStream writeTo) {
        
    }
    
    
    @Override
    public RgInformationSet[] findResourceGroups(String searchString) {
        // TODO Auto-generated method stub        
        return null;
    }
    
    
    @Override
    public File downloadResourceGroup(String rgPackage) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void setCallback(IServerDownloadCallback callback) {
        this.callback = callback;
    }
    
}
