package de.unistuttgart.ipvs.pmp.xmlutil.common.exception;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public abstract class AbstractException extends RuntimeException {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 6776578152738678734L;

	/**
	 * Message of the exception
	 */
	private String message;

	/**
	 * The throwable
	 */
	private Throwable throwable;

	/**
	 * Constructor
	 * 
	 * @param message
	 *            of the exception
	 */
	public AbstractException(String details, Throwable throwable) {
		this.message = details;
		this.throwable = throwable;
	}

	/**
	 * Get the message of the exception
	 * 
	 * @return message of the exception
	 */
	@Override
	public String getMessage() {
		return this.message;
	}

	/**
	 * Get the throwable
	 * 
	 * @return the throwable
	 */
	public Throwable getParent() {
		return this.throwable;
	}

}
