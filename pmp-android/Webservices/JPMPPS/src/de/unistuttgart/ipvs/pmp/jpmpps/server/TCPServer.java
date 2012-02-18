package de.unistuttgart.ipvs.pmp.jpmpps.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPS;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestCommunicationEnd;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestRGIS;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroupPackage;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroups;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.CachedRequestResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.InvalidRequestResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.NoSuchPackageResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.RGISResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.ResourceGroupPackageResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.ResourceGroupsResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.model.LocalizedResourceGroup;
import de.unistuttgart.ipvs.pmp.jpmpps.model.Model;
import de.unistuttgart.ipvs.pmp.jpmpps.model.ResourceGroup;

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
        
        private ObjectInputStream input;
        private ObjectOutputStream output;
        
        
        public SocketProcessingThread(Socket socket) {
            this.socket = socket;
        }
        
        
        @Override
        public void run() {
            try {
                input = new ObjectInputStream(this.socket.getInputStream());
                output = new ObjectOutputStream(this.socket.getOutputStream());
                
                processing();
            } catch (IOException e) {
                System.out.println("[E] Failed to create in-/outputstreams for connection with "
                        + this.socket.getInetAddress());
                if (JPMPPS.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        
        private void processing() {
            try {
                boolean end = false;
                while (!end) {
                    try {
                        Object request = input.readObject();
                        
                        if (request instanceof RequestResourceGroups) {
                            /* REQUEST: ResourecGroups-List */
                            RequestResourceGroups req = (RequestResourceGroups) request;
                            LocalizedResourceGroup[] rgs = JPMPPS.get().findResourceGroups(req.getLocale(),
                                    req.getFilter(), JPMPPS.LIMIT);
                            
                            if (ResponseHasher.checkHash(req.getLocale(), rgs, req.getCacheHash())) {
                                output.writeObject(new CachedRequestResponse());
                            } else {
                                output.writeObject(new ResourceGroupsResponse(rgs, ResponseHasher.hash(req.getLocale(),
                                        rgs)));
                            }
                            
                        } else if (request instanceof RequestResourceGroupPackage) {
                            /* REQUEST: ResourceGroup-Package */
                            ResourceGroup rg = Model.get().getResourceGroups()
                                    .get(((RequestResourceGroupPackage) request).getPackageName());
                            if (rg == null) {
                                output.writeObject(new NoSuchPackageResponse());
                            } else if (ResponseHasher.checkHash(rg,
                                    ((RequestResourceGroupPackage) request).getCacheHash())) {
                                output.writeObject(new CachedRequestResponse());
                            } else {
                                output.writeObject(new ResourceGroupPackageResponse(rg.getInputStream(), ResponseHasher
                                        .hash(rg.getRevision())));
                            }
                            
                        } else if (request instanceof RequestRGIS) {
                            /* REQUEST: ResourceGroup-RGIS */
                            ResourceGroup rg = Model.get().getResourceGroups()
                                    .get(((RequestRGIS) request).getPackageName());
                            
                            if (rg == null) {
                                output.writeObject(new NoSuchPackageResponse());
                            } else if (ResponseHasher.checkHash(rg, ((RequestRGIS) request).getCacheHash())) {
                                output.writeObject(new CachedRequestResponse());
                            } else {
                                output.writeObject(new RGISResponse(rg.getRGIS(), ResponseHasher.hash(rg.getRevision())));
                            }
                            
                        } else if (request instanceof RequestCommunicationEnd) {
                            /* REQUEST: Communication END */
                            end = true;
                            
                        } else {
                            /* REQUEST: UNKNOWN */
                            output.writeObject(new InvalidRequestResponse());
                        }
                        
                        continue;
                        /* REQUEST: UNKNOWN */
                    } catch (ClassNotFoundException e) {
                        System.out.println("[E] Received an unknown object from " + this.socket.getInetAddress());
                        
                        if (JPMPPS.DEBUG) {
                            e.printStackTrace();
                        }
                        
                        // No correct request.
                        output.writeObject(new InvalidRequestResponse());
                    }
                    
                }
            } catch (IOException e) {
                System.out.println("[E] Communication with " + this.socket.getInetAddress() + " failed + (Error: "
                        + e.getMessage() + ")");
                if (JPMPPS.DEBUG) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
}
