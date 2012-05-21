package de.unistuttgart.ipvs.pmp.jpmpps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestCommunicationEnd;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroupPackage;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroups;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.ResourceGroupPackageResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.ResourceGroupsResponse;

/**
 * File for testing the JPMPPS.
 * 
 * @author Jakob Jarosch, Tobias Kuhn
 */
public class ServerTest {
    
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", JPMPPSConstants.PORT);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            
            output.writeObject(new RequestResourceGroups("en"));
            Object result = input.readObject();
            
            System.out.println(result.getClass());
            
            String downloadPackage = null;
            if (result instanceof ResourceGroupsResponse) {
                ResourceGroupsResponse rgr = (ResourceGroupsResponse) result;
                downloadPackage = rgr.getResourceGroups()[0].getIdentifier();
                System.out.println(rgr.getResourceGroups()[0].getIdentifier());
                System.out.println("Name:        " + rgr.getResourceGroups()[0].getName());
                System.out.println("Revision:    " + rgr.getResourceGroups()[0].getRevision());
                System.out.println("Description: " + rgr.getResourceGroups()[0].getDescription());
            }
            
            if (downloadPackage != null) {
                System.out.println("Download");
                
                output.writeObject(new RequestResourceGroupPackage(downloadPackage));
                Object result2 = input.readObject();
                
                System.out.println(result2.getClass());
                
                if (result2 instanceof ResourceGroupPackageResponse) {
                    ResourceGroupPackageResponse rgpr = (ResourceGroupPackageResponse) result2;
                    download(rgpr);
                }
                
            }
            
            output.writeObject(new RequestCommunicationEnd());
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Unpacks a package from a {@link ResourceGroupPackageResponse}.
     * 
     * @param rgpr
     *            {@link ResourceGroupPackageResponse} which should be unpacked.
     * 
     * @throws IOException
     *             Throws an {@link IOException} on an IO failure.
     */
    private static void download(ResourceGroupPackageResponse rgpr) throws IOException {
        final int BUFFER_SIZE = 32 * 1024;
        File tmp = new File("Test.apk");
        FileOutputStream fos = new FileOutputStream(tmp);
        try {
            
            // Copy file to a local one.
            InputStream is = rgpr.getResourceGroupInputStream();
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                
                int read = -1;
                do {
                    read = is.read(buffer, 0, BUFFER_SIZE);
                    if (read > -1) {
                        fos.write(buffer, 0, read);
                    }
                } while (read > -1);
                
            } finally {
                is.close();
            }
        } finally {
            fos.close();
        }
        
        // Print out the file size of the package.
        System.out.println(tmp.getAbsolutePath() + " is " + tmp.length() + " Bytes");
    }
}
