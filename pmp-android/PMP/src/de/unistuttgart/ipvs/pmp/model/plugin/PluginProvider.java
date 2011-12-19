package de.unistuttgart.ipvs.pmp.model.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.graphics.drawable.Drawable;
import dalvik.system.DexClassLoader;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSetParser;

/**
 * Provider for managing all {@link ResourceGroup} plugins in PMP.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PluginProvider {
    
    /*
     * constants 
     */
    
    private static final Context context = PMPApplication.getContext();
    
    private static final String PLUGIN_BASE_DIR_STR = "plugins";
    private static final File PLUGIN_BASE_DIR = context.getDir(PLUGIN_BASE_DIR_STR, Context.MODE_PRIVATE);
    
    private static final String PLUGIN_APK_DIR_STR = PLUGIN_BASE_DIR.getAbsolutePath() + "/apk/";
    private static final String PLUGIN_DEX_DIR_STR = PLUGIN_BASE_DIR.getAbsolutePath() + "/dex/";
    private static final String PLUGIN_ASSET_DIR_STR = PLUGIN_BASE_DIR.getAbsolutePath() + "/ass/";
    
    private static final File PLUGIN_APK_DIR = new File(PLUGIN_APK_DIR_STR);
    private static final File PLUGIN_DEX_DIR = new File(PLUGIN_DEX_DIR_STR);
    private static final File PLUGIN_ASSET_DIR = new File(PLUGIN_ASSET_DIR_STR);
    
    private static final String APK_STR = ".apk";
    private static final String XML_STR = ".xml";
    private static final String PNG_STR = ".png";
    
    private static final ClassLoader CLASS_LOADER = context.getClassLoader();
    
    /*
     * fields
     */
    private Map<String, ResourceGroup> cache;
    private Map<String, RgInformationSet> cacheRGIS;
    
    /*
     * singleton stuff
     */
    
    private static final PluginProvider instance = new PluginProvider();
    
    
    public static PluginProvider getInstance() {
        return instance;
    }
    
    
    private PluginProvider() {
        this.cache = new HashMap<String, ResourceGroup>();
        this.cacheRGIS = new HashMap<String, RgInformationSet>();
        
        PLUGIN_BASE_DIR.mkdirs();
        PLUGIN_APK_DIR.mkdirs();
        PLUGIN_DEX_DIR.mkdirs();
        PLUGIN_ASSET_DIR.mkdirs();
    }
    
    
    /**
     * Assures that the identified resource group is loaded.
     * 
     * @param identifier
     */
    private void checkCached(String identifier) {
        if (this.cache.get(identifier) == null) {
            install(identifier);
        }
    }
    
    
    /**
     * 
     * @param identifier
     * @return the must-have class name of the main class for that package
     */
    private String getClassName(String identifier) {
        String[] packageNames = identifier.split("\\.");
        String result = packageNames[packageNames.length - 1];
        return Character.toUpperCase(result.charAt(0)) + result.substring(1);
    }
    
    
    /**
     * Copies the data in input stream to the target.
     * 
     * @param inputStream
     * @param target
     */
    private void copyFile(InputStream inputStream, String target) {
        try {
            FileOutputStream fos = new FileOutputStream(target);
            
            try {
                byte[] buffer = new byte[32 * 1024];
                
                while (inputStream.available() > 0) {
                    int read = inputStream.read(buffer);
                    fos.write(buffer, 0, read);
                }
                
            } finally {
                fos.close();
                
            }
        } catch (IOException ioe) {
            Log.e("IO exception during copy file to " + target, ioe);
        }
        
    }
    
    
    /**
     * Well duh-huh.
     * 
     * @param string
     */
    private void deleteFile(String fileName) {
        new File(fileName).delete();
    }
    
    
    /**
     * Injects a random apk from an {@link InputStream}. Does not install it.
     * 
     * @param identifier
     * @param input
     */
    public void injectFile(String identifier, InputStream input) {
        copyFile(input, PLUGIN_APK_DIR_STR + identifier + APK_STR);
    }
    
    
    /**
     * Installs a specific resource group from input apk.
     * 
     * @param identifier
     * @return true, if and only if the operation succeeded
     */
    public boolean install(String identifier) {
        try {
            // identify the important attributes first
            String apkName = PLUGIN_APK_DIR_STR + identifier + APK_STR;
            String className = getClassName(identifier);
            
            // extract the XML, so we have the information from there
            ZipFile zipApk = new ZipFile(apkName);
            ZipEntry xmlEntry = zipApk.getEntry("assets/" + className + XML_STR);
            if (xmlEntry != null) {
                copyFile(zipApk.getInputStream(xmlEntry), PLUGIN_ASSET_DIR_STR + identifier + XML_STR);
            } else {
                throw new IOException("assets/" + className + XML_STR + " missing.");
            }
            
            // create the RGIS
            RgInformationSet rgis = RgInformationSetParser.createRgInformationSet(new FileInputStream(
                    PLUGIN_ASSET_DIR_STR + identifier + ".xml"));
            
            DexClassLoader classLoader = new DexClassLoader(apkName, PLUGIN_DEX_DIR_STR, null, CLASS_LOADER);
           
            // extract icon
            // TODO Marcus should include an <icon> Tag sooner or later
            ZipEntry iconEntry = zipApk.getEntry("res/drawable-hdpi/icon.png");
            if (iconEntry == null) {
                iconEntry = zipApk.getEntry("res/drawable-mdpi/icon.png");
                if (iconEntry == null) {
                    iconEntry = zipApk.getEntry("res/drawable-ldpi/icon.png");
                }
            }
            if (iconEntry != null) {
                copyFile(zipApk.getInputStream(iconEntry), PLUGIN_ASSET_DIR_STR + identifier + PNG_STR);
            } else {
                throw new IOException("res/drawable-*dpi/icon.png missing.");
            }
            
            // load main class
            Class<?> clazz = classLoader.loadClass(identifier + "." + className);
            Class<? extends ResourceGroup> rgClazz = clazz.asSubclass(ResourceGroup.class);
            Constructor<? extends ResourceGroup> rgConstruct = rgClazz.getConstructor(IPMPConnectionInterface.class);
            
            // store in cache
            this.cache.put(identifier, rgConstruct.newInstance(PMPConnectionInterface.getInstance()));
            this.cacheRGIS.put(identifier, rgis);
            
            return true;
            
            // TODO we might wanna handle some of these nicer? 
        } catch (ClassNotFoundException cnfe) {
            Log.e("Could not find class for " + identifier, cnfe);
        } catch (ClassCastException cce) {
            Log.e("Could not cast main class for " + identifier, cce);
        } catch (SecurityException se) {
            Log.e("Security manager denied loading constructor for " + identifier, se);
        } catch (NoSuchMethodException nsme) {
            Log.e("Could not find constructor(IPMPCI) for main class for " + identifier, nsme);
        } catch (IllegalArgumentException iae) {
            Log.e("Could not pass arguments to constructor for " + identifier, iae);
        } catch (InstantiationException ie) {
            Log.e("Cannot instantiate main class for " + identifier, ie);
        } catch (IllegalAccessException iae) {
            Log.e("Could not access constructor for " + identifier, iae);
        } catch (InvocationTargetException ite) {
            Log.e("Constructor threw exception for " + identifier, ite);
        } catch (IOException ioe) {
            Log.e("IO exception for " + identifier, ioe);
        }
        return false;
    }
    
    
    /**
     * Uninstalls all data associated with the identified resource group.
     * 
     * @param identifier
     */
    public void uninstall(String identifier) {
        deleteFile(PLUGIN_ASSET_DIR_STR + identifier + PNG_STR);
        deleteFile(PLUGIN_ASSET_DIR_STR + identifier + XML_STR);
        deleteFile(PLUGIN_APK_DIR + identifier + APK_STR);
    }
    
    
    /**
     * 
     * @param identifier
     * @return the one and only instance of the identified resource group in PMP
     */
    public ResourceGroup getResourceGroupClass(String identifier) {
        checkCached(identifier);
        return this.cache.get(identifier);
    }
    
    
    /**
     * 
     * @param identifier
     * @return the XML stream for the specified resource group or null if it wasn't found which should not happen
     */
    public RgInformationSet getRGIS(String identifier) {
        checkCached(identifier);
        return this.cacheRGIS.get(identifier);
        
    }
    
    
    /**
     * 
     * @param identifier
     * @return the icon for the specified resource group or null if it wasn't found which should not happen
     */
    public Drawable getIcon(String identifier) {
        checkCached(identifier);
        return Drawable.createFromPath(PLUGIN_ASSET_DIR_STR + identifier + ".png");
    }
    
}
