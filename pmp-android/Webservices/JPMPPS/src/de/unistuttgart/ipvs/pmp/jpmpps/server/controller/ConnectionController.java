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

/**
 * The {@link ConnectionController} controls the connection between server and client.
 * A connection to a client will be automatically closed after a idle time of
 * {@link ConnectionController#MAXIMUM_IDLE_TIME}.
 * 
 * @author Jakob Jarosch
 */
public class ConnectionController {
    
    /**
     * The socket which holds the connection to the client.
     */
    private Socket socket;
    
    /**
     * {@link ObjectInputStream} for communication between client to server.
     */
    private ObjectInputStream inputStream;
    /**
     * {@link ObjectOutputStream} for communication from server to client.
     */
    private ObjectOutputStream outputStream;
    
    /**
     * {@link Boolean} which indicates the current state of the connection.
     */
    private boolean commnicationEnd;
    
    /**
     * Maximum time of idle before the connection will be automatically closed.
     */
    public static final int MAXIMUM_IDLE_TIME = 10 * 10000;
    
    
    /**
     * Creates a new {@link ConnectionController}.
     * 
     * @param socket
     *            {@link Socket} which is connected to the client.
     */
    public ConnectionController(Socket socket) {
        this.socket = socket;
        
        /* Set a timeout for the socket to prevent DDOSing the server with blocking threads. */
        try {
            this.socket.setSoTimeout(MAXIMUM_IDLE_TIME);
        } catch (SocketException e) {
        }
        
        /* Try to open input and output streams. If it fails directly end the communication. */
        try {
            this.inputStream = new ObjectInputStream(this.socket.getInputStream());
            this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            log("Failed to create in-/outputstreams, closing connection.", e);
            endCommunication();
        }
        
    }
    
    
    /**
     * @return Returns True when the communication is ended.
     */
    public boolean isCommunicationEnd() {
        return this.commnicationEnd;
    }
    
    
    /**
     * Writes a response on the output stream.
     * 
     * @param response
     *            Response which should be written.
     */
    public void writeResponse(AbstractResponse response) {
        if (isCommunicationEnd()) {
            throw new IllegalAccessError("Communicaton channels are closed.");
        }
        
        try {
            this.outputStream.writeObject(response);
        } catch (IOException e) {
            log("Communication had a IOException, closing connection.", e);
            endCommunication();
        }
    }
    
    
    /**
     * @return Returns a read request from the input stream. Returns null if the request was not a valid one.
     */
    public AbstractRequest readRequest() {
        if (isCommunicationEnd()) {
            throw new IllegalAccessError("Communicaton channels are closed.");
        }
        
        /* Try to read the request */
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
            /* Timeout for maximum idle time reached, closing connection */
            log("Communication timed out, closing connection.", e);
            endCommunication();
        } catch (IOException e) {
            log("Communication had a IOException, closing connection.", e);
            endCommunication();
        }
        
        return null;
    }
    
    
    /**
     * Closes the connection to client.
     */
    public void endCommunication() {
        this.commnicationEnd = true;
        try {
            if (this.inputStream != null) {
                this.inputStream.close();
            }
            if (this.outputStream != null) {
                this.outputStream.close();
            }
            this.socket.close();
        } catch (IOException e) {
        }
    }
    
    
    /**
     * Logs a message.
     * 
     * @param message
     *            Message which should be logged.
     * @param t
     *            {@link Throwable} which might be the reason for logging. Can be null if not available.
     */
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
