package de.unistuttgart.ipvs.pmp.infoapp.webservice;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * This trust manager uses the default trust managers provided by the runtime environmen.
 * However, it allows the user to add their own certificates.
 * Based on http://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https
 * 
 * @author Patrick Strobel
 */
public class ExtensibleTrustManager implements X509TrustManager {
    
    private ArrayList<X509TrustManager> x509TrustManagers = new ArrayList<X509TrustManager>();
    
    
    public ExtensibleTrustManager() throws NoSuchAlgorithmException, KeyStoreException {
        
        // Get the default X509 trust manager and add it to the managers-list
        TrustManagerFactory defaultTMF = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        defaultTMF.init((KeyStore) null);
        this.addX509TrustManagers(defaultTMF);
        
        // Stop, if no default trust manager was found as we require this one
        if (this.x509TrustManagers.isEmpty()) {
            throw new RuntimeException("No default X509 trust manager found");
        }
    }
    
    
    /**
     * Adds an additional key store to the trust manager
     * 
     * @param store
     *            Key store that will be added to the trust manager
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     */
    public void addKeyStore(KeyStore store) throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory additionalTMF = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        additionalTMF.init(store);
        this.addX509TrustManagers(additionalTMF);
    }
    
    
    private void addX509TrustManagers(TrustManagerFactory factory) {
        for (TrustManager tm : factory.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                this.x509TrustManagers.add((X509TrustManager) tm);
            }
        }
        System.out.println("Added " + this.x509TrustManagers.size() + " X509 trust managers");
    }
    
    
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // Check client with the default trust manager
        this.x509TrustManagers.get(0).checkClientTrusted(chain, authType);
        
    }
    
    
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // Loop through every trust manager at let them check the certificate
        for (X509TrustManager tm : this.x509TrustManagers) {
            try {
                tm.checkServerTrusted(chain, authType);
                return;
            } catch (CertificateException ce) {
                // Do nothing (another trust manager may accept the certificate)
            }
        }
        
        // If we end here, no trust manager accepted the certficate
        throw new CertificateException();
        
    }
    
    
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // Get the accepted issuers from all trust managers and return them in one list
        List<X509Certificate> certs = new ArrayList<X509Certificate>();
        
        for (X509TrustManager tm : this.x509TrustManagers) {
            certs.addAll(Arrays.asList(tm.getAcceptedIssuers()));
        }
        
        return certs.toArray(new X509Certificate[certs.size()]);
    }
    
}
