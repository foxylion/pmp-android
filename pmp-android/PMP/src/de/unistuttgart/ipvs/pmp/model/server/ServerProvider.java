package de.unistuttgart.ipvs.pmp.model.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * @see IServerProvider
 * @author Tobias Kuhn
 * 
 */
public class ServerProvider implements IServerProvider {
    
    /*
     * constants
     */
    private static final String SERVER_URL = "http://pmp-android.no-ip.org/";
    private static final String SEARCH_FOR = "?q=";
    private static final long CALLBACK_INTERVAL = 100L;
    private static final long CACHE_DURATION = 3600L * 1000L;
    
    private static final int BUFFER_SIZE = 32 * 1024;
    
    private static final String APK_STR = ".apk";
    private static final String XML_STR = ".xml";
    
    private static final String TEMPORARY_PATH = PMPApplication.getContext().getCacheDir().getAbsolutePath() + "/";
    
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
        if (!PMPApplication.getContext().getCacheDir().mkdirs() && !PMPApplication.getContext().getCacheDir().exists()) {
            Log.e("Error while creating directory in ServerProvider.");
        }
        this.callback = NullServerDownloadCallback.instance;
    }
    
    
    /**
     * Downloads a file from the server.
     * 
     * @param address
     *            the non-standard address part
     * @param writeTo
     *            the {@link OutputStream} to write to
     * @return true, if and only if the download succeeded
     */
    private boolean downloadFile(String address, OutputStream writeTo) {
        try {
            InputStream inputStream = null;
            int length = 0;
            FileOutputStream cacheTo = null;
            
            // try to use the cache
            File cacheFile = new File(TEMPORARY_PATH + String.valueOf(address.hashCode()));
            if (cacheFile.exists()) {
                if (cacheFile.lastModified() + CACHE_DURATION < System.currentTimeMillis()) {
                    cacheFile.delete();
                    
                } else {
                    inputStream = new FileInputStream(cacheFile);
                    length = (int) cacheFile.length();
                    
                }
            }
            
            // nothing found
            if (inputStream == null) {
                URLConnection urlc = new URL(SERVER_URL + address).openConnection();
                inputStream = urlc.getInputStream();
                length = urlc.getContentLength();
                cacheTo = new FileOutputStream(cacheFile);
            }
            byte[] buffer = new byte[BUFFER_SIZE];
            
            // callback stuff
            int position = 0;
            long lastCallback = System.currentTimeMillis();
            this.callback.download(0, length);
            
            while (position < length) {
                int read = inputStream.read(buffer);
                if (read < 0) {
                    // not finished, but end of stream reached
                    break;
                }
                writeTo.write(buffer, 0, read);
                if (cacheTo != null) {
                    cacheTo.write(buffer, 0, read);
                }
                
                // just callback
                position += read;
                if (System.currentTimeMillis() < lastCallback + CALLBACK_INTERVAL) {
                    this.callback.download(position, length);
                    lastCallback = System.currentTimeMillis();
                }
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
            if (cacheTo != null) {
                cacheTo.close();
            }
            
            // callback            
            this.callback.download(length, length);
            
            return true;
        } catch (MalformedURLException murle) {
            Log.e("URL was malformed during downloading file " + address, murle);
        } catch (IOException ioe) {
            Log.e("IOException during downloading file " + address, ioe);
        }
        return false;
    }
    
    
    @Override
    public RGIS[] findResourceGroups(String searchPattern) {
        Assert.nonNull(searchPattern, ModelMisuseError.class, Assert.ILLEGAL_NULL, "searchString", searchPattern);
        this.callback.tasks(0, -1);
        
        // load the package names of all RGs
        String[] rgs;
        RGIS[] result;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (!downloadFile(SEARCH_FOR + searchPattern, baos)) {
                return new RGIS[0];
            }
            
            String response = baos.toString("UTF-8");
            if (response.trim().length() > 0) {
                rgs = response.split("\n");
            } else {
                return new RGIS[0];
            }
            
        } catch (UnsupportedEncodingException uee) {
            Log.e("UTF-8 is missing. This should not happen.", uee);
            rgs = new String[0];
            
        }
        
        result = new RGIS[rgs.length];
        
        // load all the XMLs
        ByteArrayStreamBridge basb = new ByteArrayStreamBridge(BUFFER_SIZE);
        
        for (int i = 0; i < rgs.length; i++) {
            this.callback.tasks(1 + i, 1 + rgs.length);
            
            if (!downloadFile(rgs[i] + XML_STR, basb)) {
                return new RGIS[0];
            }
            
            // the BASBridge prevents us from copying a huge amount of arrays
            long before = System.currentTimeMillis();
            result[i] = XMLUtilityProxy.parseRGISXML(basb.toInputStream());
            long after = System.currentTimeMillis();
            
            Log.v("Parsing one XML took us " + (after - before) + "ms");
            
            this.callback.tasks(1 + i + 1, 1 + rgs.length);
        }
        
        return result;
    }
    
    
    @Override
    public File downloadResourceGroup(String rgPackage) {
        Assert.nonNull(rgPackage, ModelMisuseError.class, Assert.ILLEGAL_NULL, "rgPackage", rgPackage);
        try {
            this.callback.tasks(0, 1);
            
            File tmp = new File(TEMPORARY_PATH + rgPackage + APK_STR);
            FileOutputStream fos = new FileOutputStream(tmp);
            try {
                if (!downloadFile(rgPackage + APK_STR, fos)) {
                    return null;
                }
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            
            this.callback.tasks(1, 1);
            return tmp;
            
        } catch (FileNotFoundException ioe) {
            Log.e("IO exception during downloading RG " + rgPackage, ioe);
            return null;
        }
    }
    
    
    @Override
    public void setCallback(IServerDownloadCallback callback) {
        if (callback == null) {
            this.callback = NullServerDownloadCallback.instance;
        } else {
            this.callback = callback;
        }
    }
    
    
    @Override
    public void cleanCache() {
        for (File f : new File(TEMPORARY_PATH).listFiles()) {
            f.delete();
        }
        
    }
    
}
