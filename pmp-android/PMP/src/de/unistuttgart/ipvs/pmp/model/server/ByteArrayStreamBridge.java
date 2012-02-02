package de.unistuttgart.ipvs.pmp.model.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A class to make certain actions in the {@link ServerProvider} easier and faster because the briding functionality
 * from {@link OutputStream} to {@link InputStream} is required.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ByteArrayStreamBridge extends ByteArrayOutputStream {
    
    public ByteArrayStreamBridge(int size) {
        super(size);
    }
    
    
    /**
     * Exports the contents of this {@link ByteArrayStreamBridge} to a {@link ByteArrayInputStream} by using the
     * reference directly.
     * Will clean the current output buffer that was written to.
     * 
     * @return
     */
    public ByteArrayInputStream toInputStream() {
        byte[] result = this.buf;
        
        this.buf = new byte[result.length];
        this.count = 0;
        
        return new ByteArrayInputStream(result);
    }
    
}
