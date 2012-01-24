package de.unistuttgart.ipvs.pmp.api.gui.registration;

import java.util.concurrent.Semaphore;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRegistrationHandler;

@Deprecated
final class DirtyChecks {
    
    @Deprecated
    protected final boolean isPMPInstalled(Context c2) {
        ServiceConnection d = new ServiceConnection() {
            
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
            
            
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
            }
        };
        boolean c = c2.bindService(new Intent(Constants.PMP_IDENTIFIER), d, Context.BIND_AUTO_CREATE);
        if (c) {
            c2.unbindService(d);
        }
        return c;
    }
    
    private final class C {
        
        public boolean b = false;
    }
    
    
    @Deprecated
    protected final boolean isAppRegistered() throws InterruptedException {
        if (Thread.currentThread().getName().equalsIgnoreCase("main")) {
            throw new IllegalThreadStateException();
        }
        final Semaphore b = new Semaphore(0);
        final C d = new C();
        PMP.get().register(new PMPRegistrationHandler() {
            
            @Override
            public void onAlreadyRegistered() {
                d.b = true;
            }
            
            
            @Override
            public void onFinalize() {
                b.release();
            }
        });
        b.acquire();
        return d.b;
        
    }
}
