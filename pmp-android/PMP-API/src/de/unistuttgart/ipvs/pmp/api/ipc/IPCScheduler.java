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
    public final BlockingQueue<IPCCommand> queue;
    
    /**
     * The connection to be used while executing commands.
     */
    public final IPCConnection connection;
    
    
    /**
     * Creates a new {@link IPCScheduler}.
     * 
     * @param context
     *            the context to use for the connections
     */
    public IPCScheduler(Context context) {
        this.queue = new LinkedBlockingQueue<IPCCommand>();
        
        this.connection = new IPCConnection(context);
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
                    IPCScheduler.this.connection.setDestinationService(command.getDestinationService());
                    
                    // handle timeout
                    if (command.getTimeout() < System.currentTimeMillis()) {
                        command.getHandler().onTimeout();
                        return;
                    }
                    
                    command.getHandler().onPrepare();
                    
                    // try connecting
                    IBinder binder = IPCScheduler.this.connection.getBinder();
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
