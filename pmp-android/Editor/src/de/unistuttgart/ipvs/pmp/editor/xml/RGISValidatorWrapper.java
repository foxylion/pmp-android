package de.unistuttgart.ipvs.pmp.editor.xml;

import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.RGISValidator;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;

/**
 * Wrapper of the RGISValidator. The following issues are also attached to the
 * subobjects: 
 * - PS_IDENTIFIER_OCCURRED_TOO_OFTEN 
 * - CHANGE_DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN
 * 
 * @author Marcus Vetter
 * 
 */
public class RGISValidatorWrapper extends RGISValidator {

	/**
	 * Singleton stuff
	 */
	private static RGISValidatorWrapper instance = null;

	private RGISValidatorWrapper() {

	}

	public static RGISValidatorWrapper getInstance() {
		if (instance == null) {
			instance = new RGISValidatorWrapper();
		}
		return instance;
	}
	
	@Override
	public List<IIssue> validateRGIS(IRGIS rgis, boolean attachData) {
		// TODO Auto-generated method stub
		return super.validateRGIS(rgis, attachData);
	}
	
	@Override
	public List<IIssue> validateRGInformation(IRGIS rgis, boolean attachData) {
		// TODO Auto-generated method stub
		return super.validateRGInformation(rgis, attachData);
	}
	
	@Override
	public List<IIssue> validatePrivacySettings(IRGIS rgis, boolean attachData) {
		// TODO Auto-generated method stub
		return super.validatePrivacySettings(rgis, attachData);
	}
	
	@Override
	public List<IIssue> validatePrivacySetting(IRGISPrivacySetting ps,
			boolean attachData) {
		// TODO Auto-generated method stub
		return super.validatePrivacySetting(ps, attachData);
	}
	
}
