package de.unistuttgart.ipvs.pmp.service.app;

import android.content.Intent;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.service.NullServiceStubImpl;
import de.unistuttgart.ipvs.pmp.service.PMPSignedService;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * The {@link AppService} is used to allow PMPService a connection where informations about the App are served.
 * 
 * @author Jakob Jarosch
 */
public class AppService extends PMPSignedService {
    
    @Override
    protected PMPSignee createSignee() {
        return findContextApp().getSignee();
    }
    
    
    @Override
    public IBinder onSignedBind(Intent intent) {
        PMPComponentType type = (PMPComponentType) intent.getSerializableExtra(Constants.INTENT_TYPE);
        
        if (type == PMPComponentType.PMP) {
            AppServicePMPStubImpl assi = new AppServicePMPStubImpl();
            assi.setApp(findContextApp());
            return assi;
        } else {
            return new NullServiceStubImpl("The bound Type is does not allow any Service at the AppService");
        }
    }
    
    
    @Override
    public IBinder onUnsignedBind(Intent intent) {
        return new NullServiceStubImpl("The AppService does not allow any unsigned connection");
    }
    
    
    private App findContextApp() {
        if (!(getApplication() instanceof App)) {
            return null;
        } else {
            return (App) getApplication();
        }
    }
}
