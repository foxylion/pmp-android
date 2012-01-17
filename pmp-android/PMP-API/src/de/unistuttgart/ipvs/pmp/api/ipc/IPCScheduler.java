package de.unistuttgart.ipvs.pmp.api.ipc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.content.Context;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.api.ipc.command.IPCCommand;

/**
 * The API IPC Scheduler to queue IPC commands and execute them.
 * 
 * @author Tobias Kuhn
 * 
 */
public class IPCScheduler extends Thread {
    
    /**
     * The queue that contains all the commands to be executed
     */
    public BlockingQueue<IPCCommand> queue;
    
    /**
     * The connection to be used while executing commands.
     */
    public IPCConnection connection;
    
    
    /**
     * Creates a new {@link IPCScheduler} to connect to PMP.
     * 
     * @param context
     *            the context to use for the connection
     */
    public IPCScheduler(Context context) {
        this(context, "de.unistuttgart.ipvs.pmp.service.PMPService");
    }
    
    
    /**
     * Creates a new {@link IPCScheduler} to connect to an arbitrary destination service
     * 
     * @param context
     *            the context to use for the connection
     * @param destinationService
     *            the Android identifier of the destination service
     */
    public IPCScheduler(Context context, String destinationService) {
        this.queue = new LinkedBlockingQueue<IPCCommand>();
        
        this.connection = new IPCConnection(context);
        this.connection.setDestinationService(destinationService);
    }
    
    
    /**
     * Queues a new {@link IPCCommand} to be executed.
     * 
     * @param command
     *            the IPC command to be executed at an arbitrary point in the future
     */
    protected void queue(IPCCommand command) {
        if (!this.queue.offer(command)) {
            Log.e("BlockingQueue ran out of capacity!");
        }
    }
    
    
    @Override
    public void run() {
        while (!isInterrupted()) {
            
            final IPCCommand command = this.queue.poll();
            
            new Thread() {
                
                public void run() {
                    // handle timeout
                    if (command.getTimeout() < System.currentTimeMillis()) {
                        command.getHandler().onTimeout();
                        return;
                    }
                    
                    command.getHandler().onPrepare();
                    
                    // try connecting
                    IBinder binder = connection.getBinder();
                    if (binder != null) {
                        command.execute(binder);
                    } else {
                        command.getHandler().onBindingFailed();
                    }
                    
                    command.getHandler().onFinalize();
                    
                };
            }.start();
            
        }
    }
}
