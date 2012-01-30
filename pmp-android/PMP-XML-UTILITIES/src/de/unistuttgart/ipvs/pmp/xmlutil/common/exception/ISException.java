package de.unistuttgart.ipvs.pmp.xmlutil.common.exception;

/**
 * This class is used to create exceptions within the information sets with a
 * type and message.
 * 
 * @author Marcus Vetter
 * 
 */
public class ISException extends AbstractException {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -4639871052954638618L;

	/**
	 * Type of the parser exception
	 */
	private Type type;

	/**
	 * Constructor
	 * 
	 * @param type
	 *            Type of the exception
	 * @param message
	 *            Message of the exception
	 * @param throwable
	 */
	public ISException(Type type, String message, Throwable throwable) {
		super(message, throwable);
		this.type = type;
	}

	/**
	 * Constructor
	 * 
	 * @param type
	 *            Type of the exception
	 * @param message
	 *            Message of the exception
	 */
	public ISException(Type type, String message) {
		this(type, message, null);
	}

	/**
	 * Get the type of the exception
	 * 
	 * @return type of the exception
	 */
	public Type getType() {
		return this.type;
	}

	public enum Type {

		/**
		 * A name with the same locale already exists
		 */
		NAME_WITH_SAME_LOCALE_ALREADY_EXISTS,

		/**
		 * A description with the same locale already exists
		 */
		DESCRIPTION_WITH_SAME_LOCALE_ALREADY_EXISTS,

		/**
		 * A privacy setting with the same identifier already exists within the
		 * same required resource group
		 */
		PRIVACY_SETTING_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,

		/**
		 * A service feature with the same identifier already exists
		 */
		SERVICE_FEATURE_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,

		/**
		 * A required Resourcegroup with the same identifier already exists
		 */
		REQUIRED_RESOUCEGROUP_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,

	}

}
