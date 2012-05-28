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

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.params.HttpParams;

/**
 * This "wrapper"-class is required since Android's SSLSocketFactory-class does not accept a SSLContext
 * 
 * @author Patrick Strobel
 */
public class ExtendedSSLSocketFactory extends SSLSocketFactory {
    
    private final SSLContext context;
    
    
    public ExtendedSSLSocketFactory(SSLContext context) throws KeyManagementException, UnrecoverableKeyException,
            NoSuchAlgorithmException, KeyStoreException {
        super((KeyStore) null);
        this.context = context;
    }
    
    
    @Override
    public Socket createSocket(HttpParams params) throws IOException {
        return this.context.getSocketFactory().createSocket();
    }
    
    
    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        SSLSocket sslSocket = (SSLSocket) this.context.getSocketFactory().createSocket(socket, host, port, autoClose);
        
        X509HostnameVerifier hostnameVerifier = super.getHostnameVerifier();
        if (hostnameVerifier != null) {
            hostnameVerifier.verify(host, sslSocket);
        }
        // verifyHostName() didn't blowup - good!
        return sslSocket;
    }
    
    
    @Override
    public Socket createSocket() throws IOException {
        return this.context.getSocketFactory().createSocket();
    }
    
}
