package de.unistuttgart.ipvs.pmp.apps.emailapp.model.data;

import java.util.List;

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
	 * The email addresses of the recipients
	 */
	private List<String> recipients;

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
	public EMail(String from, List<String> recipients, String subject, String content) {
		this.setFrom(from);
		this.setRecipients(recipients);
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

	/**
	 * Get the list of recipients
	 * @return the list of recipients
	 */
	public List<String> getRecipients() {
		return recipients;
	}

	/**
	 * Set the list of recipients
	 * @param recipients list of recipients
	 */
	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}
	
	@Override
	public String toString(){
	    String recipient = "";
	    for (String rec : recipients){
		recipient = recipient + rec + " ";
	    }
	    return "From: " + from + "\nTo: " + recipient + "\n" + "Subject: " + subject;
	}

}
