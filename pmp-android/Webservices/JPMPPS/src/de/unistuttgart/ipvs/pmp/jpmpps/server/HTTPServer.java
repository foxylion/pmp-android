package de.unistuttgart.ipvs.pmp.jpmpps.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPS;
import de.unistuttgart.ipvs.pmp.jpmpps.server.controller.ConnectionController;
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;

/**
 * The {@link HTTPServer} provides the {@link IPresetSet}s over HTTP.
 * 
 * @author Jakob Jarosch
 */
public class HTTPServer {
    
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
    public HTTPServer(int port) {
        
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
     * Thread for connection accept.
     * 
     * @author Jakob Jarosch
     */
    class AcceptingConnectionThread extends Thread {
        
        @Override
        public void run() {
            while (!isInterrupted() && !HTTPServer.this.socket.isClosed()) {
                try {
                    // TODO use a thread pool to limit the number of threads at the same time, so DDOSing
                    //      will be a little bit more difficult.
                    
                    /* When a new connection is accepted another background thread will be started. */
                    Socket connection = HTTPServer.this.socket.accept();
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
     * Thread for processing a request from a client.
     * 
     * @author Jakob Jarosch
     */
    class SocketProcessingThread extends Thread {
        
        /**
         * Pattern is used to find the requested {@link IPresetSet} id.
         */
        private final Pattern HTTP_PRESET_SET_ID = Pattern.compile("GET /?([a-z0-9]+)");
        
        /**
         * {@link Socket} which provides the connection to client.
         */
        private Socket connection;
        
        /**
         * {@link BufferedReader} for reading the client input line by line.
         */
        private BufferedReader input;
        
        /**
         * {@link BufferedWriter} for writing output for the client line by line.
         */
        private BufferedWriter output;
        
        /**
         * {@link BufferedOutputStream} for writing output with byte arrays to the client.
         */
        private BufferedOutputStream outputSimple;
        
        
        /**
         * Creates a new processing thread.
         * 
         * @param connection
         *            {@link Socket} which should be handled.
         * @throws IOException
         *             Is thrown when it failed to create input or output streams.
         */
        public SocketProcessingThread(Socket connection) throws IOException {
            this.connection = connection;
            
            this.connection.setSoTimeout(ConnectionController.MAXIMUM_IDLE_TIME);
            
            this.input = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
            this.output = new BufferedWriter(new OutputStreamWriter(this.connection.getOutputStream()));
            this.outputSimple = new BufferedOutputStream(this.connection.getOutputStream());
        }
        
        
        @Override
        public void run() {
            try {
                
                while (!this.connection.isClosed()) {
                    
                    /*
                     * Read the header from the client.
                     */
                    List<String> header = new ArrayList<String>();
                    String line;
                    do {
                        line = this.input.readLine();
                        header.add(line);
                        
                    } while (!line.trim().equals(""));
                    
                    /* Find the "GET" */
                    String requestedPresetSet = null;
                    for (String headerLine : header) {
                        Matcher matcher = this.HTTP_PRESET_SET_ID.matcher(headerLine);
                        if (matcher.find()) {
                            requestedPresetSet = matcher.group(1);
                        }
                    }
                    
                    if (requestedPresetSet == null || requestedPresetSet.equals("")) {
                        this.output.write("HTTP/1.0 400 Bad Request");
                        this.output.write("Connection: close\r\n");
                        this.output.write("Server: JPMPPS\r\n");
                        this.output.write("Content-Type: text/plain\r\n");
                        this.output.write("\r\n");
                        this.output.write("Bad Request (No PresetSet ID defined)");
                    } else {
                        IPresetSet presetSet = JPMPPS.get().getPresetSet(requestedPresetSet);
                        if (presetSet == null) {
                            this.output.write("HTTP/1.0 400 Bad Request\r\n");
                            this.output.write("Connection: close\r\n");
                            this.output.write("Server: JPMPPS\r\n");
                            this.output.write("Content-Type: text/plain\r\n");
                            this.output.write("\r\n");
                            this.output.write("Bad Request (PresetSet not found)");
                        } else {
                            this.output.write("HTTP/1.0 200 Ok");
                            this.output.write("Connection: close\r\n");
                            this.output.write("Server: JPMPPS\r\n");
                            this.output.write("Content-Type: text/xml\r\n");
                            this.output.write("\r\n");
                            this.output.flush();
                            InputStream is = XMLUtilityProxy.getPresetUtil().compile(presetSet);
                            byte[] bytes = new byte[1024];
                            int length = 0;
                            while ((length = is.read(bytes)) != -1) {
                                this.outputSimple.write(bytes, 0, length);
                            }
                            this.outputSimple.flush();
                        }
                    }
                    this.output.close();
                    this.outputSimple.close();
                }
            } catch (SocketTimeoutException e) {
                // TODO make log entry
            } catch (IOException e) {
                // TODO make log entry
            } finally {
                try {
                    this.connection.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
