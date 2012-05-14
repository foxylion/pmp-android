package de.unistuttgart.ipvs.pmp.infoapp.webservice;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * A fake trust manager that accepts all types of certificates (including self-signed and expired ones)
 * 
 * @author Patrick Strobel
 */
public class FakeTrustManager implements X509TrustManager {
    
    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        //System.out.println("checkClientTrusted: " + arg0[0].getSigAlgName() + ", arg1: " + arg1);
    }
    
    
    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        //System.out.println("checkServerTrusted: " + arg0[0].getSigAlgName() + ", arg1: " + arg1);
    }
    
    
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        //System.out.println("getAcceptedIssuers");
        return null;
    }
    
}
