package de.unistuttgart.ipvs.pmp.jpmpps;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.unistuttgart.ipvs.pmp.jpmpps.model.LocalizedResourceGroup;
import de.unistuttgart.ipvs.pmp.jpmpps.model.Model;
import de.unistuttgart.ipvs.pmp.jpmpps.model.ResourceGroup;
import de.unistuttgart.ipvs.pmp.jpmpps.server.TCPServer;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;

/**
 * Java Privacy Management Platform Package Server Class for Connection handling
 * and initializing of service.
 * 
 * @author Jakob Jarosch
 */
public class JPMPPS {
    
    private static JPMPPS instance = null;
    
    
    /**
     * Creates a new instance of the service.
     * 
     * @param path
     */
    private JPMPPS(File path) {
        this.path = path;
    }
    
    
    public static JPMPPS get(File path) {
        if (instance == null) {
            instance = new JPMPPS(path);
        }
        return instance;
    }
    
    
    public static JPMPPS get() {
        if (instance == null) {
            throw new IllegalStateException("JPMPPS must be initialized by calling JPMPPS.get(File)");
        }
        
        return instance;
    }
    
    public static final boolean DEBUG = true;
    
    public static final int LIMIT = 10;

    private static final int UPDATE_DELAY = 10 * 60 * 1000;
    
    private File path;
    
    private TCPServer server = null;
    
    private Timer timer = new Timer();
    
    
    /**
     * Initializes the server and loads all informations sets from the apk
     * files.
     */
    public void initialize() {
		refreshPackages();
		
		/* Updates the ResoruceGroups every UPDATE_DELAY milliseconds. */
		timer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                refreshPackages();
            }
        }, UPDATE_DELAY, UPDATE_DELAY);
	}
    
    
    /**
     * Starts the server and begins to listen to the specified port.
     */
    public void startServer() {
        if (this.server == null) {
            this.server = new TCPServer(JPMPPSConstants.PORT);
        }
    }
    
    
    /**
     * Stops the Server.
     */
    public void stopServer() {
        this.server.stop();
    }
    
    
    /**
     * Returns all ResourceGroups with the specified filter.
     * 
     * @param locale
     *            Locale which should be used for the answer.
     * @param filter
     *            Filter which should be applied to the search.
     * @return Filtered and localized ResourceGroups.
     */
    public LocalizedResourceGroup[] findResourceGroups(String locale, String filter, int limit) {
        
        List<LocalizedResourceGroup> list = new ArrayList<LocalizedResourceGroup>();
        
        String[] filters = filter.split(",");
        
        for (Map.Entry<String, ResourceGroup> entry : Model.get().getResourceGroups().entrySet()) {
            if (filter.length() == 0) {
                list.add(entry.getValue().getLocalized(locale));
            } else {
                for (String f : filters) {
                    if (f.startsWith("package:") && f.substring(8).equals(entry.getKey())) {
                        list.add(entry.getValue().getLocalized(locale));
                    } else if (entry.getValue().getRGIS().getNameForLocale(Locale.ENGLISH).contains(f)) {
                        list.add(entry.getValue().getLocalized(locale));
                    }
                    
                    if (list.size() == limit) {
                        break;
                    }
                }
            }
            
            if (list.size() == limit) {
                break;
            }
        }
        
        return list.toArray(new LocalizedResourceGroup[list.size()]);
    }
    
    /**
     * Updates the available {@link ResourceGroup}s.
     */
    private void refreshPackages() {
        /* All during the refresh algorithm found {@link ResourceGroup}s are cached here. */
        Map<String, ResourceGroup> resourceGroups = new HashMap<String, ResourceGroup>();
        
        for (File pack : path.listFiles()) {
            try {
                ResourceGroup rg = new ResourceGroup(pack);
                IRGIS rgis = rg.getRGIS();
                if (rgis == null) {
                    System.out.println("[E] Skipping package " + pack.getName());
                } else {
                    resourceGroups.put(rgis.getIdentifier(), rg);
                    System.out.println("[I] Loaded package " + rgis.getIdentifier() + " from " + pack.getName());
                }
            } catch (FileNotFoundException e) {
                System.out.println("[E] Skipping package " + pack.getName() + ", file not found.");
            }
        }
        
        Model.get().replaceResourceGroups(resourceGroups);
    }
}
