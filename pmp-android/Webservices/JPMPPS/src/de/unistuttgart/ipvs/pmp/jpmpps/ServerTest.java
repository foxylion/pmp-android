package de.unistuttgart.ipvs.pmp.jpmpps;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestCommunicationEnd;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroups;


public class ServerTest {
    
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", JPMPPS.LISTEN_PORT);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            
            output.writeObject(new RequestResourceGroups("en"));
            Object result = input.readObject();
            
            System.out.println(result.getClass());
            
            output.writeObject(new RequestCommunicationEnd());
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
    }
    
}
