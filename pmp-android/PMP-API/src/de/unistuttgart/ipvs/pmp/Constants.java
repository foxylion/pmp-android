package de.unistuttgart.ipvs.pmp;

import android.content.Intent;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnector;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

/**
 * Constants used by PMP, PMP-API and their implementing Apps and
 * ResourceGroups.
 * 
 * @author Jakob Jarosch
 */
public class Constants {

    /**
     * The Log-Name used in DDMS for debugging.
     */
    public static final String LOG_NAME = "PMP";

    /**
     * The key for the type of the connection inside the {@link Intent}, used by
     * {@link AbstractConnector}.
     */
    public static final String INTENT_TYPE = "connection.type";

    /**
     * The key for the App/RG identifier inside the {@link Intent}, used by
     * {@link AbstractConnector}.
     */
    public static final String INTENT_IDENTIFIER = "connection.identifier";

    /**
     * The key for the signature inside the {@link Intent}, used by
     * {@link AbstractConnector}.
     */
    public static final String INTENT_SIGNATURE = "connection.signature";

    /**
     * The Android-wide identifier for the PMP (also used to connect to the
     * Service inside the {@link PMPServiceConnector}.
     */
    public static final String PMP_IDENTIFIER = "de.unistuttgart.ipvs.pmp.service.PMPService";

    public static final String PMP_LOG_SUFIX = "PMP";
    
    /**
     * The default locale required to be present at all times
     */
    public static final String DEFAULT_LOCALE = "en";

}
