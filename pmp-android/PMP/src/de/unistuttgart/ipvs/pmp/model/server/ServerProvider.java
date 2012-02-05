package de.unistuttgart.ipvs.pmp.model.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.IRequest;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestCommunicationEnd;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroupPackage;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroups;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.AbstractResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.CachedRequestResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.ResourceGroupPackageResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.ResourceGroupsResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.model.LocalizedResourceGroup;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;

/**
 * @see IServerProvider
 * @author Tobias Kuhn
 * 
 */
public class ServerProvider implements IServerProvider {
    
    /*
     * constants
     */
    private static final String SERVER_URL = "pmp-android.no-ip.org";
    private static final int SERVER_PORT = 3133;
    
    private static final int BUFFER_SIZE = 32 * 1024;
    
    /**
     * Amount of time where the cache is that fresh that the server is not even contacted in milliseconds.
     */
    private static final long LOCAL_CACHE_ONLY_TIME = 60000L;
    
    private static final String APK_STR = ".apk";
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
    
    
    public AbstractResponse handleRequest(IRequest request) throws IOException, ClassNotFoundException {
        Socket tcpSocket = new Socket(SERVER_URL, SERVER_PORT);
        
        Object result = null;
        
        ObjectOutputStream oos = new ObjectOutputStream(tcpSocket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(tcpSocket.getInputStream());
        try {
            
            oos.writeObject(request);
            result = ois.readObject();
            if (!(result instanceof AbstractResponse)) {
                throw new ClassNotFoundException();
            }
            
            oos.writeObject(new RequestCommunicationEnd());
            
        } finally {
            ois.close();
            oos.close();
        }
        
        return (AbstractResponse) result;
    }
    
    
    /**
     * Retrieves an {@link AbstractResponse} that fits the request.
     * 
     * @param requestString
     *            isRG ? rgPackage : search string
     * @param isRG
     *            whether this requestString is a rgPackage or a search string
     * @return the corresponding {@link AbstractResponse}, or null if an error occurred
     */
    public AbstractResponse getResponseFor(String requestString, boolean isRG) {
        String cacheHash;
        if (isRG) {
            cacheHash = "r" + String.valueOf(requestString.hashCode());
        } else {
            cacheHash = String.valueOf(requestString.hashCode());
        }
        
        // check whether cache would be available
        AbstractResponse cachedResponse = null;
        File cacheFile = new File(TEMPORARY_PATH + cacheHash);
        if (cacheFile.exists()) {
            try {
                
                FileInputStream fis = new FileInputStream(cacheFile);
                try {
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    try {
                        
                        Object o = ois.readObject();
                        if (!(o instanceof AbstractResponse)) {
                            cachedResponse = null;
                        }
                        cachedResponse = (AbstractResponse) o;
                        
                    } finally {
                        ois.close();
                    }
                } finally {
                    fis.close();
                }
                
            } catch (IOException e) {
                Log.e("IOException during loading cache", e);
                cachedResponse = null;
                
            } catch (ClassNotFoundException e) {
                Log.e("ClassNotFoundException during loading cache", e);
                cachedResponse = null;
                
            }
        }
        
        // if the cache is that new it is extremely unlikely that something has changed
        // e.g. we're installing several RGs
        if ((cachedResponse != null) && (cacheFile.lastModified() + LOCAL_CACHE_ONLY_TIME > System.currentTimeMillis())) {
            return cachedResponse;
        }
        
        // send request
        IRequest request;
        if (isRG) {
            if ((cachedResponse != null) && (cachedResponse instanceof ResourceGroupPackageResponse)) {
                ResourceGroupPackageResponse rgpr = (ResourceGroupPackageResponse) cachedResponse;
                // we are missing the caching
                request = new RequestResourceGroupPackage(requestString);
            } else {
                request = new RequestResourceGroupPackage(requestString);
            }
            
        } else {
            if ((cachedResponse != null) && (cachedResponse instanceof ResourceGroupsResponse)) {
                ResourceGroupsResponse rgr = (ResourceGroupsResponse) cachedResponse;
                request = new RequestResourceGroups(Locale.getDefault().toString(), requestString, rgr.getHash());
            } else {
                request = new RequestResourceGroups(Locale.getDefault().toString(), requestString);
            }
        }
        
        // handle request, fetch response
        AbstractResponse response;
        try {
            response = handleRequest(request);
        } catch (IOException e) {
            Log.e("IOException during " + request.getClass().getSimpleName(), e);
            return null;
        } catch (ClassNotFoundException e) {
            Log.e("ClassNotFoundException during " + request.getClass().getSimpleName(), e);
            return null;
        }
        
        if (response instanceof CachedRequestResponse) {
            // okay to use cache                
            return cachedResponse;
            
        } else {
            // overwrite cache
            
            try {
                FileOutputStream fos = new FileOutputStream(cacheFile);
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    try {
                        oos.writeObject(response);
                    } finally {
                        oos.close();
                    }
                } finally {
                    fos.close();
                }
            } catch (IOException e) {
                Log.e("IOException during " + response.getClass().getSimpleName(), e);
            }
            
            return response;
        }
        
    }
    
    
    @Override
    public LocalizedResourceGroup[] findResourceGroups(String searchPattern) {
        Assert.nonNull(searchPattern, ModelMisuseError.class, Assert.ILLEGAL_NULL, "searchPattern", searchPattern);
        this.callback.tasks(0, 1);
        
        // get response
        AbstractResponse response = getResponseFor(searchPattern, false);
        if ((response == null) || !(response instanceof ResourceGroupsResponse)) {
            return new LocalizedResourceGroup[0];
        }
        
        ResourceGroupsResponse rgr = (ResourceGroupsResponse) response;
        this.callback.tasks(1, 1);
        
        return rgr.getResourceGroups();
    }
    
    
    @Override
    public File downloadResourceGroup(String rgPackage) {
        Assert.nonNull(rgPackage, ModelMisuseError.class, Assert.ILLEGAL_NULL, "rgPackage", rgPackage);
        try {
            this.callback.tasks(0, 1);
            
            File tmp = new File(TEMPORARY_PATH + rgPackage + APK_STR);
            FileOutputStream fos = new FileOutputStream(tmp);
            try {
                // get response
                AbstractResponse response = getResponseFor(rgPackage, true);
                if ((response == null) || !(response instanceof ResourceGroupPackageResponse)) {
                    return null;
                }
                
                // copy file
                ResourceGroupPackageResponse rgpr = (ResourceGroupPackageResponse) response;
                InputStream is = rgpr.getResourceGroupInputStream();
                try {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    
                    int read = -1;
                    do {
                        read = is.read(buffer, 0, BUFFER_SIZE);
                        if (read > -1) {
                            fos.write(buffer, 0, read);
                        }
                    } while (read > -1);
                    
                } finally {
                    is.close();
                }
            } finally {
                fos.close();
            }
            
            this.callback.tasks(1, 1);
            return tmp;
            
        } catch (IOException ioe) {
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
