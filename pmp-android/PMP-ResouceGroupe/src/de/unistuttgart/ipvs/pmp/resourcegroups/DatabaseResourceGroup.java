/**
 * 
 */
package de.unistuttgart.ipvs.pmp.resourcegroups;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.SimplePrivacyLevel;
import de.unistuttgart.ipvs.pmp.service.PMPSignedService;

/**
 * @author Dang Huynh
 * 
 */
public class DatabaseResourceGroup extends ResourceGroup {

    public static final String DATABASE_RESOURCE_IDENTIFIER = 
	    "de.unistuttgart.ipvs.pmp.resourcegroups.DatabaseResource";
    public static final String PRIVACY_LEVEL_READ = "read";
    public static final String PRIVACY_LEVEL_WRITE = "write";
    public static final String PRIVACY_LEVEL_DELETE = "delete";
    // public static final String PRIVACY_LEVEL_CREATE = "create";

    private DatabaseResource dbr;
    private Class<? extends PMPSignedService> service;
    private Context context;

    // PL
    private SimplePrivacyLevel read, write, delete;

    public DatabaseResourceGroup(Context serviceContext,
	    Class<? extends PMPSignedService> service) {
	super(serviceContext, service);
	context = serviceContext;
	this.service = service;
	Log.d(context.getResources().getString(R.string.resource_group_name));

	// Register the privacy levels
	// TODO: Create the privacy levels
	registerPrivacyLevel(PRIVACY_LEVEL_READ, read);
	registerPrivacyLevel(PRIVACY_LEVEL_WRITE, write);
	registerPrivacyLevel(PRIVACY_LEVEL_DELETE, delete);

	// Register the resource
	// TODO: Where should the resource be created? Only when an authorized
	// application request, right?
	registerResource(DATABASE_RESOURCE_IDENTIFIER, dbr);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.unistuttgart.ipvs.pmp.resource.ResourceGroup#getName(java.lang.String)
     */
    @Override
    public String getName(String locale) {
	// TODO: Locale or not locale?
	return context.getResources().getString(R.string.resource_group_name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.unistuttgart.ipvs.pmp.resource.ResourceGroup#getDescription(java.lang
     * .String)
     */
    @Override
    public String getDescription(String locale) {
	// TODO: Locale or not locale?
	return context.getResources().getString(R.string.resource_group_name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.unistuttgart.ipvs.pmp.resource.ResourceGroup#getServiceAndroidName()
     */
    @Override
    protected String getServiceAndroidName() {
	// TODO Is this right?
	return service.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.unistuttgart.ipvs.pmp.resource.ResourceGroup#onRegistrationSuccess()
     */
    @Override
    public void onRegistrationSuccess() {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.unistuttgart.ipvs.pmp.resource.ResourceGroup#onRegistrationFailed(
     * java.lang.String)
     */
    @Override
    public void onRegistrationFailed(String message) {
	// TODO Auto-generated method stub

    }

}