package de.unistuttgart.ipvs.pmp.resourcegroups.contact.aidl; 

interface IContact{
	void call(String tel);
	void sms(String tel, String message);
	void email(String recipient, String subject, String message);
}