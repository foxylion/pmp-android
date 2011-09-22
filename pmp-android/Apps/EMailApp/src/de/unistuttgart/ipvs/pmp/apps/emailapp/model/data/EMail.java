package de.unistuttgart.ipvs.pmp.apps.emailapp.model.data;

/**
 * This is an email with the attributes from, to, subject and content.
 * 
 * @author Marcus Vetter
 * 
 */
public class EMail {

	/**
	 * The email address of the sender
	 */
	private String from;

	/**
	 * The email address of the receiver
	 */
	private String to;

	/**
	 * The subject of the email
	 */
	private String subject;

	/**
	 * The content of the email
	 */
	private String content;
	
	/**
	 * Constructor
	 */
	public EMail(String from, String to, String subject, String content) {
		this.setFrom(from);
		this.setTo(to);
		this.setSubject(subject);
		this.setContent(content);
	}

	/**
	 * Get the email address of the sender
	 * 
	 * @return the email address of the sender
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Set the email address of the sender
	 * 
	 * @param the
	 *            email address of the sender
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Get the email address of the receiver
	 * 
	 * @return the email address of the receiver
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Set the email address of the receiver
	 * 
	 * @param to
	 *            the email address of the receiver
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * Get the subject of the email
	 * 
	 * @return the subject of the email
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Set the subject of the email
	 * 
	 * @param subject
	 *            the subject of the email
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Get the content of the email
	 * 
	 * @return the content of the email
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Set the content of the email
	 * 
	 * @param content
	 *            the content of the email
	 */
	public void setContent(String content) {
		this.content = content;
	}

}
