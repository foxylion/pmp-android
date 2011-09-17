/**
 * 
 */
package de.unistuttgart.ipvs.pmp.resourcegroups.database;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.BooleanPrivacyLevel;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;

/**
 * @author Dang Huynh
 * 
 */
public class DatabaseResourceGroup extends ResourceGroup {

    public static final String DATABASE_RESOURCE_IDENTIFIER = 
	    "de.unistuttgart.ipvs.pmp.resourcegroups.database";

    private Context context;

    public static final String PRIVACY_LEVEL_READ = "read";
    public static final String PRIVACY_LEVEL_MODIFY = "modify";
    public static final String PRIVACY_LEVEL_CREATE = "create";

    private DatabaseResource dbr;

    // PL
    private BooleanPrivacyLevel read, modify, create;

    public DatabaseResourceGroup(Context context) {
	super(context);
	this.context = context;

	// TODO Remove Log
	Log.d(context.getResources().getString(R.string.resource_group_name) + getDescription("en"));
	
	// Prepare the privacy levels and resource
	read = new BooleanPrivacyLevel(context.getResources().getString(
		R.string.privacy_level_read_name), context.getResources()
		.getString(R.string.privacy_level_read_description));
	modify = new BooleanPrivacyLevel(context.getResources().getString(
		R.string.privacy_level_modify_name), context.getResources()
		.getString(R.string.privacy_level_modify_description));
	create = new BooleanPrivacyLevel(context.getResources().getString(
		R.string.privacy_level_create_name), context.getResources()
		.getString(R.string.privacy_level_create_description));
	dbr = new DatabaseResource(context);

	// Register the privacy levels
	registerPrivacyLevel(PRIVACY_LEVEL_READ, read);
	registerPrivacyLevel(PRIVACY_LEVEL_MODIFY, modify);
	registerPrivacyLevel(PRIVACY_LEVEL_CREATE, create);

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
	return context.getResources().getString(
		R.string.resource_group_description);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.unistuttgart.ipvs.pmp.resource.ResourceGroup#getServiceAndroidName()
     */
    @Override
    protected String getServiceAndroidName() {
	return DATABASE_RESOURCE_IDENTIFIER;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.unistuttgart.ipvs.pmp.resource.ResourceGroup#onRegistrationSuccess()
     */
    @Override
    public void onRegistrationSuccess() {
	Log.d("Registration with the PMP Service successed");
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
	// TODO Retry?
	Log.e("Registration with the PMP Service failed: " + message + "");
    }
}