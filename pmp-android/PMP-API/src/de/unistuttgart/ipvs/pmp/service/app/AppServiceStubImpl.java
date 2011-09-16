package de.unistuttgart.ipvs.pmp.service.app;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.app.AppInformationSetParcelable;
import de.unistuttgart.ipvs.pmp.service.RegistrationState;

/**
 * Implementation of the {@link IAppService.Stub} stub.
 * 
 * @author Thorsten Berberich
 */
public class AppServiceStubImpl extends IAppService.Stub {
    
    /**
     * The {@link App} referenced.
     */
    private App app;
    
    public void setApp(App app) {
	this.app = app;
    }

    @Override
    public AppInformationSetParcelable getAppInformationSet()
	    throws RemoteException {
	// Convert the AppInformationset into an AppInformationSetParcelable
	AppInformationSetParcelable appInfoSetParcelable = new AppInformationSetParcelable(
		app.getInfoSet());
	return appInfoSetParcelable;
    }

    @Override
    public void setActiveServiceLevel(int level) throws RemoteException {
	app.setActiveServiceLevel(level);
    }

    @Override
    public void setRegistrationState(RegistrationState state)
	    throws RemoteException {
	if (state.getState()) {
	    app.onRegistrationSuccess();
	} else {
	    app.onRegistrationFailed(state.getMessage());
	}
    }

}
