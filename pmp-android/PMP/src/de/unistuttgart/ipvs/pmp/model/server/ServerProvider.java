package de.unistuttgart.ipvs.pmp.model.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSetParser;

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
            URLConnection urlc = new URL(SERVER_URL + address).openConnection();
            
            InputStream inputStream = urlc.getInputStream();
            byte[] buffer = new byte[32 * 1024];
            
            // callback stuff
            int length = urlc.getContentLength();
            int position = 0;
            long lastCallback = System.currentTimeMillis();
            this.callback.download(0, length);
            
            while (inputStream.available() > 0) {
                int read = inputStream.read(buffer);
                writeTo.write(buffer, 0, read);
                
                // just callback
                position += read;
                if (System.currentTimeMillis() < lastCallback + CALLBACK_INTERVAL) {
                    this.callback.download(position, length);
                    lastCallback = System.currentTimeMillis();
                }
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
    public RgInformationSet[] findResourceGroups(String searchPattern) {
        Assert.nonNull(searchPattern, new ModelMisuseError(Assert.ILLEGAL_NULL, "searchString", searchPattern));
        this.callback.tasks(0, -1);
        
        // load the package names of all RGs
        String[] rgs;
        RgInformationSet[] result;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (!downloadFile(SEARCH_FOR + searchPattern, baos)) {
                return new RgInformationSet[0];
            }
            rgs = baos.toString().split("\n");
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return new RgInformationSet[0];
            }
        }
        
        result = new RgInformationSet[rgs.length];
        
        // load all the XMLs
        for (int i = 0; i < rgs.length; i++) {
            this.callback.tasks(1 + i, 1 + rgs.length);
            
            // maybe this can be done less complicated, but i don't know for now
            ByteArrayOutputStream xmlbaos = new ByteArrayOutputStream();
            try {
                if (!downloadFile(rgs[i] + XML_STR, xmlbaos)) {
                    return new RgInformationSet[0];
                }
                
                ByteArrayInputStream bais = new ByteArrayInputStream(xmlbaos.toByteArray());
                try {
                    result[i] = RgInformationSetParser.createRgInformationSet(bais);
                    
                } finally {
                    try {
                        bais.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return new RgInformationSet[0];
                    }
                }
            } finally {
                try {
                    xmlbaos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return new RgInformationSet[0];
                }
            }
            
            this.callback.tasks(1 + i + 1, 1 + rgs.length);
        }
        
        return result;
    }
    
    
    @Override
    public File downloadResourceGroup(String rgPackage) {
        Assert.nonNull(rgPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "rgPackage", rgPackage));
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
        Assert.nonNull(callback, new ModelMisuseError(Assert.ILLEGAL_NULL, "callback", callback));
        if (callback == null) {
            this.callback = NullServerDownloadCallback.instance;
        } else {
            this.callback = callback;
        }
    }
    
}
