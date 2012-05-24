/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp-CommunicationLib
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
