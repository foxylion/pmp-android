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
package de.unistuttgart.ipvs.pmp.jpmpps.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPS;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.AbstractRequest;
import de.unistuttgart.ipvs.pmp.jpmpps.server.controller.ConnectionController;
import de.unistuttgart.ipvs.pmp.jpmpps.server.controller.HandlerFactory;

/**
 * The {@link TCPObjectServer} provides a TCP based connection interface for java clients.
 * All IO will be done by using {@link ObjectInputStream}s and {@link ObjectOutputStream}s.
 * 
 * @author Jakob Jarosch
 */
public class TCPObjectServer {
    
    /**
     * The {@link ServerSocket} where the server listens to.
     */
    private ServerSocket socket;
    
    /**
     * A Thread which is working in the background and accepts incoming connection.
     */
    private AcceptingConnectionThread connectionAcceptThread = null;
    
    
    /**
     * Creates and starts a new server on the specified port.
     * 
     * @param port
     *            Port which the server should listen on.
     */
    public TCPObjectServer(int port) {
        
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("[E] Creating ServerSocket on port " + port + " failed. [-> Exit]");
            if (JPMPPS.DEBUG) {
                e.printStackTrace();
            }
            System.exit(3);
        }
        
        /* Start the thread in background */
        this.connectionAcceptThread = new AcceptingConnectionThread();
        this.connectionAcceptThread.start();
    }
    
    
    /**
     * Stops the server.
     */
    public void stop() {
        try {
            this.connectionAcceptThread.interrupt();
            this.socket.close();
        } catch (IOException e) {
            System.out.println("[E] Failed to stop server. (Error: " + e.getMessage());
            if (JPMPPS.DEBUG) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Background thread for accepting incoming connections.
     * 
     * @author Jakob Jarosch
     */
    private class AcceptingConnectionThread extends Thread {
        
        @Override
        public void run() {
            while (!isInterrupted() && !TCPObjectServer.this.socket.isClosed()) {
                try {
                    // TODO use a thread pool to limit the number of threads at the same time, so DDOSing
                    //      will be a little bit more difficult.
                    
                    /* When a new connection is accepted another background thread will be started. */
                    Socket connection = TCPObjectServer.this.socket.accept();
                    new SocketProcessingThread(connection).start();
                } catch (Exception e) {
                    System.out.println("[E] Failed accepting incomming connection. (Error: " + e.getMessage());
                    if (JPMPPS.DEBUG) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    /**
     * {@link SocketProcessingThread} processes a client.
     * 
     * @author Jakob Jarosch
     */
    private class SocketProcessingThread extends Thread {
        
        /**
         * {@link Socket} which is connected to the client.
         */
        private Socket socket;
        
        /**
         * {@link ConnectionController} manages the connection to the client.
         */
        private ConnectionController controller;
        
        
        /**
         * Creates a new {@link SocketProcessingThread}.
         * 
         * @param socket
         *            The {@link Socket} which is connnected to the client.
         */
        public SocketProcessingThread(Socket socket) {
            this.socket = socket;
        }
        
        
        @Override
        public void run() {
            this.controller = new ConnectionController(this.socket);
            
            /*
             * Read on the socket until the communication is ended.
             */
            while (!this.controller.isCommunicationEnd()) {
                AbstractRequest request = this.controller.readRequest();
                if (!this.controller.isCommunicationEnd()) {
                    HandlerFactory.getInstance().handle(this.controller, request);
                }
            }
        }
    }
}
