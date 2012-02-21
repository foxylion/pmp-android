/*
 * Copyright 2012 pmp-android development team
 * Project: JPMPPS
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.jpmpps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import de.unistuttgart.ipvs.pmp.jpmpps.model.LocalizedResourceGroup;
import de.unistuttgart.ipvs.pmp.jpmpps.model.Model;
import de.unistuttgart.ipvs.pmp.jpmpps.model.ResourceGroup;
import de.unistuttgart.ipvs.pmp.jpmpps.server.TCPObjectServer;
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetSet;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;

/**
 * Java Privacy Management Platform Package Server Class for Connection handling
 * and initializing of service.
 * 
 * @author Jakob Jarosch
 */
public class JPMPPS {
    
    /**
     * Singleton instance of the {@link JPMPPS}.
     */
    private static JPMPPS instance = null;
    
    /**
     * Debug flag, if set to true debugging logs will be enabled.
     */
    public static final boolean DEBUG = true;
    
    /**
     * Limit of the maximum entries returned by {@link Model#getResourceGroups()}.
     */
    public static final int LIMIT = 10;
    
    /**
     * Update delay between resource group package searches.
     */
    public static final int PACKAGE_UPDATE_DELAY = 10 * 60 * 1000;
    
    /**
     * Update delay between checking of a newer version for a package.
     */
    public static final int REVISION_UPDATE_DELAY = 60 * 1000;
    
    /**
     * Path to the packages.
     */
    private File packagesPath;
    
    /**
     * Path to the {@link PresetSet} data store.
     */
    private File presetSetDatastorePath;
    
    /**
     * Instance of the {@link TCPObjectServer}.
     */
    private TCPObjectServer server = null;
    
    /**
     * Timer for updating the packages.
     */
    private Timer timer = new Timer();
    
    /**
     * Symbols for generating a new unique id.
     */
    private static final char[] CHAR_SYMBOLS = { '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
    
    /**
     * Verification pattern for checking whether a id is a valid one or not.
     */
    private static final Pattern CHAR_PATTERN = Pattern.compile("^[a-z0-9]{1,20}$");
    
    
    /**
     * Creates a new instance of the service.
     * 
     * @param packagesPath
     */
    private JPMPPS(File packagesPath, File presetSetDatastorePath) {
        this.packagesPath = packagesPath;
        this.presetSetDatastorePath = presetSetDatastorePath;
    }
    
    
    /**
     * Singleton getter for the initialization of the {@link JPMPPS} class.
     * 
     * @param packagesPath
     *            Path to the folder where all packages are stored.
     * @param presetSetDatastorePath
     *            Path to a writable folder where the PresetSets can be stored.
     * @return An instance of {@link JPMPPS}.
     */
    public static JPMPPS get(File packagesPath, File presetSetDatastorePath) {
        if (instance == null) {
            instance = new JPMPPS(packagesPath, presetSetDatastorePath);
        }
        return instance;
    }
    
    
    /**
     * @return An instance of {@link JPMPPS}.
     */
    public static JPMPPS get() {
        if (instance == null) {
            throw new IllegalStateException("JPMPPS must be initialized by calling JPMPPS.get(File)");
        }
        
        return instance;
    }
    
    
    /**
     * Initializes the server and loads all informations sets from the apk
     * files.
     */
    public void initialize() {
        refreshPackages();
        
        /* Updates the ResoruceGroups every PACKAGE_UPDATE_DELAY milliseconds. */
        this.timer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                refreshPackages();
            }
        }, PACKAGE_UPDATE_DELAY, PACKAGE_UPDATE_DELAY);
    }
    
    
    /**
     * Starts the server and begins to listen on the specified port.
     */
    public void startServer() {
        if (this.server == null) {
            this.server = new TCPObjectServer(JPMPPSConstants.PORT);
        }
    }
    
    
    /**
     * Stops the Server.
     */
    public void stopServer() {
        this.server.stop();
    }
    
    
    /**
     * Returns all ResourceGroups, which matches the filter, in a localized variant.
     * The comma "," is the separator between to different search strings, they are connected with an OR statement.
     * "package:" before the search string will try to find a specific package.
     * 
     * @param locale
     *            Locale which should be used for the result.
     * @param filter
     *            Filter which should be applied on the search.
     * @return Filtered and localized ResourceGroups.
     */
    public LocalizedResourceGroup[] findResourceGroups(String locale, String filter) {
        
        List<LocalizedResourceGroup> list = new ArrayList<LocalizedResourceGroup>();
        
        String[] filters = filter.split(",");
        
        /*
         * Iterating over all resource groups and each time as well over all filters [O(n)=n*m].
         * For performance this isn't very good, but in the moment the JPMPPS has not to handle
         * more than 10 packages and just a very low amount of request per hour.
         */
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
                    
                    /* End the search when more then 10 results where found. */
                    if (list.size() == LIMIT) {
                        break;
                    }
                }
            }
            
            /* End the search when mor then 10 results where found. */
            if (list.size() == LIMIT) {
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
        
        for (File pack : this.packagesPath.listFiles()) {
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
        
        /* Updating the model with the newer resource groups versions. */
        Model.get().replaceResourceGroups(resourceGroups);
    }
    
    
    /**
     * Saves a {@link PresetSet} in the data store and returns a unique identifier to access the stored
     * {@link PresetSet} later via {@link JPMPPS#getPresetSet(String)}.
     * 
     * @param presetSet
     *            The {@link PresetSet} which should be stored.
     * @return The id of the saved {@link PresetSet} or null if saving failed.
     */
    public synchronized String savePresetSet(IPresetSet presetSet) {
        String id = generateRandomId();
        
        try {
            InputStream is = XMLUtilityProxy.getPresetUtil().compile(presetSet);
            
            OutputStream out = new FileOutputStream(this.presetSetDatastorePath + "/" + id);
            byte buf[] = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            is.close();
            
            return id;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    /**
     * Loads a stored {@link PresetSet} from the data store.
     * 
     * @param id
     *            The unique identifier of the {@link PresetSet} which should be loaded.
     * @return The found {@link PresetSet} or null if none was found.
     */
    public IPresetSet getPresetSet(String id) {
        id = id.toLowerCase();
        
        /* Check the id for validness. */
        if (!isIdValid(id)) {
            return null;
        }
        
        /* Read out the file from data store */
        try {
            InputStream is = new FileInputStream(this.presetSetDatastorePath + "/" + id);
            return XMLUtilityProxy.getPresetUtil().parse(is);
        } catch (Exception e) {
            return null;
        }
    }
    
    
    /**
     * @return Generates a new randomly generated id.
     */
    private String generateRandomId() {
        StringBuilder sb = new StringBuilder();
        
        /*
         * Try to find an unique id for a id length at most triesPerLength times.
         * If no unique id was found the id length will be increased by one.
         */
        
        int triesPerLength = 3;
        int triesCount = 4 * triesPerLength; // Start with 4 characters length
        
        do {
            for (int i = 0; i < (triesCount / triesPerLength) + 1; i++) {
                sb.append(CHAR_SYMBOLS[new Random().nextInt(CHAR_SYMBOLS.length)]);
            }
            triesCount++;
            /* Check now if the id already exists, if not unique id was found. */
        } while (new File(this.presetSetDatastorePath + "/" + sb.toString()).isFile());
        
        return sb.toString();
    }
    
    
    /**
     * Checks the unique identifier for validness.
     * 
     * @param id
     *            The id which should be checked.
     * @return True when the id is a valid one, otherwise false.
     */
    private boolean isIdValid(String id) {
        return CHAR_PATTERN.matcher(id).matches();
    }
}
