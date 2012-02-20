package de.unistuttgart.ipvs.pmp.jpmpps.server.controller;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPS;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.AbstractRequest;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.AbstractResponse;

public class ConnectionController {
    
    private Socket socket;
    
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    
    private boolean commnicationEnd;
    
    
    public ConnectionController(Socket socket) {
        this.socket = socket;
        
        /* Set a timeout for the socket to prevent DDOSing the server with blocking threads. */
        try {
            this.socket.setSoTimeout(10000);
        } catch (SocketException e) {
        }
        
        try {
            this.inputStream = new ObjectInputStream(this.socket.getInputStream());
            this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            log("Failed to create in-/outputstreams, closing connection.", e);
            endCommunication();
        }
        
    }
    
    
    public boolean isCommunicationEnd() {
        return commnicationEnd;
    }
    
    
    public void writeResponse(AbstractResponse response) {
        if (isCommunicationEnd()) {
            throw new IllegalAccessError("Communicaton channels are closed.");
        }
        
        try {
            outputStream.writeObject(response);
        } catch (IOException e) {
            log("Communication had a IOException, closing connection.", e);
            endCommunication();
        }
    }
    
    
    public AbstractRequest readRequest() {
        if (isCommunicationEnd()) {
            throw new IllegalAccessError("Communicaton channels are closed.");
        }
        
        try {
            Object request = this.inputStream.readObject();
            
            if (request instanceof AbstractRequest) {
                return (AbstractRequest) request;
            } else {
                log("The received request is not an instance of AbstractRequest.", null);
            }
        } catch (ClassNotFoundException e) {
            log("Received an unknown request (ClassNotFoundException).", e);
        } catch (InvalidClassException e) {
            log("Received an unknown request (InvalidClassException).", e);
        } catch (SocketTimeoutException e) {
            log("Communication timed out, closing connection.", e);
            endCommunication();
        } catch (IOException e) {
            log("Communication had a IOException, closing connection.", e);
            endCommunication();
        }
        
        return null;
    }
    
    
    public void endCommunication() {
        this.commnicationEnd = true;
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            socket.close();
        } catch (IOException e) {
        }
    }
    
    
    public void log(String message, Throwable t) {
        if (t == null) {
            System.out.println(this.socket.getInetAddress().toString() + " - " + message);
        } else {
            System.err.println(this.socket.getInetAddress().toString() + " - " + message);
            if (JPMPPS.DEBUG) {
                t.printStackTrace();
            }
        }
    }
}
