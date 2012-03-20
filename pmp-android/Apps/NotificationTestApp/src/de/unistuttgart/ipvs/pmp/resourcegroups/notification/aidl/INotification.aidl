package de.unistuttgart.ipvs.pmp.resourcegroups.notification.aidl; 

interface INotification{
	void notify(String tickerText, String title ,String message);
}