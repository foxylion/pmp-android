package de.unistuttgart.ipvs.pmp.service.app;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.AppApplication;
import de.unistuttgart.ipvs.pmp.app.AppInformationSetParcelable;
import de.unistuttgart.ipvs.pmp.app.appUtil.AppUtility;
import de.unistuttgart.ipvs.pmp.app.appUtil.xmlParser.AppInformationSet;
import de.unistuttgart.ipvs.pmp.service.RegistrationState;

/**
 * Implementation of the {@link IAppService.Stub} stub.
 * 
 * @author Thorsten Berberich
 */
public class IAppServiceStubImpl extends IAppService.Stub {

    @Override
    public AppInformationSetParcelable getAppInformationSet()
	    throws RemoteException {
	// Parse the xml
	AppInformationSet appInfoSet = AppUtility.createAppInformationSet(this
		.getClass().getResourceAsStream("/assets/AppInformation.xml"));

	// Convert the AppInformationset into an AppInformationSetParcelable
	AppInformationSetParcelable appInfoSetParcelable = new AppInformationSetParcelable(
		appInfoSet);
	return appInfoSetParcelable;
    }

    @Override
    public void setActiveServiceLevel(int level) throws RemoteException {
	// TODO Auto-generated method stub

    }

    @Override
    public void setRegistrationSuccessful(RegistrationState state)
	    throws RemoteException {
	Log.v(AppApplication.getInstance().getContext().getPackageName() + " registred successful");
    }

}
