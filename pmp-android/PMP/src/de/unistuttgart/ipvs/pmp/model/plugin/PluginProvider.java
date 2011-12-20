package de.unistuttgart.ipvs.pmp.model.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidPluginException;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSetParser;

/**
 * @see IPluginProvider
 * @author Tobias Kuhn
 * 
 */
public class PluginProvider implements IPluginProvider {
    
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
    
    // errors
    private static final String ERROR = " (Trying to install '%s' from '%s' using main class '%s')";
    private static final String ERROR_CLASS_NOT_FOUND = "Main class not found" + ERROR;
    private static final String ERROR_CLASS_NOT_CASTABLE = "Main class of wrong type" + ERROR;
    private static final String ERROR_CLASS_CONSTRUCTOR_ACCESS_NOT_ALLOWED = "Accessing constructor of main class not allowed"
            + ERROR;
    private static final String ERROR_CLASS_CONSTRUCTOR_NOT_FOUND = "Constructor of main class not found" + ERROR;
    private static final String ERROR_CLASS_CONSTRUCTOR_INVALID_ARGUMENT = "Constructor of main class expects invalid arguments"
            + ERROR;
    private static final String ERROR_CLASS_NOT_INSTANTIABLE = "Main class is not instantiable" + ERROR;
    private static final String ERROR_CLASS_CONSTRUCTOR_NOT_ACCESSIBLE = "Constructor of main class not accessible"
            + ERROR;
    private static final String ERROR_CLASS_CONSTRUCTOR_THROWS_EXCEPTION = "Constructor of main class throws exception"
            + ERROR;
    private static final String ERROR_ASSETS_NOT_ACCESSIBLE = "Accessing the assets failed" + ERROR;
    
    /*
     * fields
     */
    private Map<String, ResourceGroup> cache;
    private Map<String, RgInformationSet> cacheRGIS;
    
    /*
     * singleton stuff
     */
    
    private static final IPluginProvider instance = new PluginProvider();
    
    
    public static IPluginProvider getInstance() {
        return instance;
    }
    
    
    private PluginProvider() {
        this.cache = new HashMap<String, ResourceGroup>();
        this.cacheRGIS = new HashMap<String, RgInformationSet>();
        
        if (!PLUGIN_BASE_DIR.mkdirs() && !PLUGIN_BASE_DIR.exists()) {
            Log.e("Error while creating directory in PluginProvider: " + PLUGIN_BASE_DIR.getAbsolutePath());
        }
        
        if (!PLUGIN_APK_DIR.mkdirs() && !PLUGIN_APK_DIR.exists()) {
            Log.e("Error while creating directory in PluginProvider: " + PLUGIN_APK_DIR.getAbsolutePath());
        }
        
        if (!PLUGIN_DEX_DIR.mkdirs() && !PLUGIN_DEX_DIR.exists()) {
            Log.e("Error while creating directory in PluginProvider: " + PLUGIN_DEX_DIR.getAbsolutePath());
        }
        
        if (!PLUGIN_ASSET_DIR.mkdirs() && !PLUGIN_ASSET_DIR.exists()) {
            Log.e("Error while creating directory in PluginProvider: " + PLUGIN_ASSET_DIR.getAbsolutePath());
        }
    }
    
    
    /**
     * Assures that the identified resource group is loaded.
     * 
     * @param rgPackage
     */
    private void checkCached(String rgPackage) {
        // object
        if (this.cache.get(rgPackage) == null) {
            String apkName = PLUGIN_APK_DIR_STR + rgPackage + APK_STR;
            String className = getClassName(rgPackage);
            
            try {
                this.cache.put(rgPackage, loadRGObject(rgPackage, apkName, className));
            } catch (ClassNotFoundException cnfe) {
                throw new ModelMisuseError(Assert.ILLEGAL_UNINSTALLED_ACCESS, rgPackage, cnfe);
            } catch (NoSuchMethodException nsme) {
                throw new ModelMisuseError(Assert.ILLEGAL_UNINSTALLED_ACCESS, rgPackage, nsme);
            } catch (InstantiationException ie) {
                throw new ModelMisuseError(Assert.ILLEGAL_UNINSTALLED_ACCESS, rgPackage, ie);
            } catch (IllegalAccessException iae) {
                throw new ModelMisuseError(Assert.ILLEGAL_UNINSTALLED_ACCESS, rgPackage, iae);
            } catch (InvocationTargetException ite) {
                throw new ModelMisuseError(Assert.ILLEGAL_UNINSTALLED_ACCESS, rgPackage, ite);
            }
            
        }
        
        // RGIS
        if (this.cacheRGIS.get(rgPackage) == null) {
            
            try {
                this.cacheRGIS.put(rgPackage, loadRGIS(rgPackage));
            } catch (FileNotFoundException fnfe) {
                throw new ModelMisuseError(Assert.ILLEGAL_UNINSTALLED_ACCESS, rgPackage, fnfe);
            } catch (IOException ioe) {
                throw new ModelMisuseError(Assert.ILLEGAL_UNINSTALLED_ACCESS, rgPackage, ioe);
            }
            
        }
    }
    
    
    /**
     * 
     * @param rgPackage
     * @return the must-have class name of the main class for that package
     */
    private String getClassName(String rgPackage) {
        String[] packageNames = rgPackage.split("\\.");
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
     * @param fileName
     */
    private void deleteFile(String fileName) {
        if (!(new File(fileName).delete())) {
            Log.e("Error while deleting " + fileName);
        }
    }
    
    
    @Override
    public void injectFile(String rgPackage, InputStream input) {
        Assert.nonNull(rgPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "rgPackage", rgPackage));
        Assert.nonNull(input, new ModelMisuseError(Assert.ILLEGAL_NULL, "input", input));
        copyFile(input, PLUGIN_APK_DIR_STR + rgPackage + APK_STR);
    }
    
    
    @Override
    public void install(String rgPackage) throws InvalidPluginException {
        Assert.nonNull(rgPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "rgPackage", rgPackage));
        
        // identify the important attributes first
        String apkName = PLUGIN_APK_DIR_STR + rgPackage + APK_STR;
        String className = getClassName(rgPackage);
        String errorMsg;
        
        try {
            
            // extract the XML, so we have the information from there
            ZipFile zipApk = new ZipFile(apkName);
            try {
                ZipEntry xmlEntry = zipApk.getEntry("assets/" + className + XML_STR);
                if (xmlEntry != null) {
                    copyFile(zipApk.getInputStream(xmlEntry), PLUGIN_ASSET_DIR_STR + rgPackage + XML_STR);
                } else {
                    throw new IOException("assets/" + className + XML_STR + " missing.");
                }
                
                // create the RGIS
                RgInformationSet rgis = loadRGIS(rgPackage);
                
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
                    copyFile(zipApk.getInputStream(iconEntry), PLUGIN_ASSET_DIR_STR + rgPackage + PNG_STR);
                } else {
                    throw new IOException("res/drawable-*dpi/icon.png missing.");
                }
                
                // load main class
                ResourceGroup rg = loadRGObject(rgPackage, apkName, className);
                
                // store in cache
                this.cache.put(rgPackage, rg);
                this.cacheRGIS.put(rgPackage, rgis);
                
            } finally {
                zipApk.close();
            }
            
        } catch (ClassNotFoundException cnfe) {
            errorMsg = String.format(ERROR_CLASS_NOT_FOUND, rgPackage, apkName, className);
            Log.e(errorMsg, cnfe);
            throw new InvalidPluginException(errorMsg, cnfe);
        } catch (ClassCastException cce) {
            errorMsg = String.format(ERROR_CLASS_NOT_CASTABLE, rgPackage, apkName, className);
            Log.e(errorMsg, cce);
            throw new InvalidPluginException(errorMsg, cce);
        } catch (SecurityException se) {
            errorMsg = String.format(ERROR_CLASS_CONSTRUCTOR_ACCESS_NOT_ALLOWED, rgPackage, apkName, className);
            Log.e(errorMsg, se);
            throw new InvalidPluginException(errorMsg, se);
        } catch (NoSuchMethodException nsme) {
            errorMsg = String.format(ERROR_CLASS_CONSTRUCTOR_NOT_FOUND, rgPackage, apkName, className);
            Log.e(errorMsg, nsme);
            throw new InvalidPluginException(errorMsg, nsme);
        } catch (IllegalArgumentException iae) {
            errorMsg = String.format(ERROR_CLASS_CONSTRUCTOR_INVALID_ARGUMENT, rgPackage, apkName, className);
            Log.e(errorMsg, iae);
            throw new InvalidPluginException(errorMsg, iae);
        } catch (InstantiationException ie) {
            errorMsg = String.format(ERROR_CLASS_NOT_INSTANTIABLE, rgPackage, apkName, className);
            Log.e(errorMsg, ie);
            throw new InvalidPluginException(errorMsg, ie);
        } catch (IllegalAccessException iae) {
            errorMsg = String.format(ERROR_CLASS_CONSTRUCTOR_NOT_ACCESSIBLE, rgPackage, apkName, className);
            Log.e(errorMsg, iae);
            throw new InvalidPluginException(errorMsg, iae);
        } catch (InvocationTargetException ite) {
            errorMsg = String.format(ERROR_CLASS_CONSTRUCTOR_THROWS_EXCEPTION, rgPackage, apkName, className);
            Log.e(errorMsg, ite);
            throw new InvalidPluginException(errorMsg, ite);
        } catch (IOException ioe) {
            errorMsg = String.format(ERROR_ASSETS_NOT_ACCESSIBLE, rgPackage, apkName, className);
            Log.e(errorMsg, ioe);
            throw new InvalidPluginException(errorMsg, ioe);
        }
    }
    
    
    /**
     * Creates a new {@link ResourceGroup} object for a resource group.
     * 
     * @param rgPackage
     *            the package identifying the resource group
     * @param apkName
     *            the apk containing the resource group
     * @param className
     *            the name of the main class of the resource group
     * @return a (new) object for the resource group
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private ResourceGroup loadRGObject(String rgPackage, String apkName, String className)
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            InvocationTargetException {
        DexClassLoader classLoader = new DexClassLoader(apkName, PLUGIN_DEX_DIR_STR, null, CLASS_LOADER);
        
        Class<?> clazz = classLoader.loadClass(rgPackage + "." + className);
        Class<? extends ResourceGroup> rgClazz = clazz.asSubclass(ResourceGroup.class);
        Constructor<? extends ResourceGroup> rgConstruct = rgClazz.getConstructor(IPMPConnectionInterface.class);
        ResourceGroup rg = rgConstruct.newInstance(PMPConnectionInterface.getInstance());
        return rg;
    }
    
    
    /**
     * Loads the RGIS for a specific resource group.
     * 
     * @param rgPackage
     *            identifier of the resource group
     * @return the RGIS for the resource group
     * @throws FileNotFoundException
     * @throws IOException
     */
    private RgInformationSet loadRGIS(String rgPackage) throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(PLUGIN_ASSET_DIR_STR + rgPackage + ".xml");
        try {
            return RgInformationSetParser.createRgInformationSet(fis);
        } finally {
            fis.close();
        }
    }
    
    
    @Override
    public void uninstall(String rgPackage) {
        Assert.nonNull(rgPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "rgPackage", rgPackage));
        deleteFile(PLUGIN_ASSET_DIR_STR + rgPackage + PNG_STR);
        deleteFile(PLUGIN_ASSET_DIR_STR + rgPackage + XML_STR);
        deleteFile(PLUGIN_APK_DIR + rgPackage + APK_STR);
    }
    
    
    @Override
    public ResourceGroup getResourceGroupObject(String rgPackage) {
        Assert.nonNull(rgPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "rgPackage", rgPackage));
        checkCached(rgPackage);
        return this.cache.get(rgPackage);
    }
    
    
    @Override
    public RgInformationSet getRGIS(String rgPackage) {
        Assert.nonNull(rgPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "rgPackage", rgPackage));
        checkCached(rgPackage);
        return this.cacheRGIS.get(rgPackage);
        
    }
    
    
    @Override
    public Drawable getIcon(String rgPackage) {
        Assert.nonNull(rgPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "rgPackage", rgPackage));
        checkCached(rgPackage);
        return Drawable.createFromPath(PLUGIN_ASSET_DIR_STR + rgPackage + ".png");
    }
    
}
