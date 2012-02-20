package de.unistuttgart.ipvs.pmp.jpmpps.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.TimerTask;

import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPS;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.AbstractRequest;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestCommunicationEnd;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestPresetSetLoad;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestPresetSetSave;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestRGIS;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroupPackage;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroups;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.AbstractResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.CachedRequestResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.InvalidRequestResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.NoSuchPackageResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.RGISResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.ResourceGroupPackageResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.ResourceGroupsResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.model.LocalizedResourceGroup;
import de.unistuttgart.ipvs.pmp.jpmpps.model.Model;
import de.unistuttgart.ipvs.pmp.jpmpps.model.ResourceGroup;
import de.unistuttgart.ipvs.pmp.jpmpps.server.controller.ConnectionController;
import de.unistuttgart.ipvs.pmp.jpmpps.server.controller.HandlerFactory;

public class TCPServer {
    
    private ServerSocket socket;
    
    private AcceptingConnectionThread connectionAcceptThread = null;
    
    
    /**
     * Creates and starts a new server on the specified port.
     * 
     * @param port
     *            Port which the server should listen on.
     */
    public TCPServer(int port) {
        
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("[E] Creating ServerSocket on port " + port + " failed. [-> Exit]");
            if (JPMPPS.DEBUG) {
                e.printStackTrace();
            }
            System.exit(3);
        }
        
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
    
    private class AcceptingConnectionThread extends Thread {
        
        public void run() {
            while (!isInterrupted()) {
                try {
                    Socket connection = socket.accept();
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
    
    private class SocketProcessingThread extends Thread {
        
        private Socket socket;
        
        private ConnectionController controller;
        
        
        public SocketProcessingThread(Socket socket) {
            this.socket = socket;
        }
        
        
        @Override
        public void run() {
            this.controller = new ConnectionController(this.socket);
            
            while (!controller.isCommunicationEnd()) {
                AbstractRequest request = controller.readRequest();
                if (!controller.isCommunicationEnd()) {
                    HandlerFactory.getInstance().handle(controller, request);
                }
            }
        }
    }
}
