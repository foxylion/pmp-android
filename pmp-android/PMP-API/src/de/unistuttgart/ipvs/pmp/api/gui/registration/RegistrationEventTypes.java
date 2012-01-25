package de.unistuttgart.ipvs.pmp.api.gui.registration;

/**
 * The defined event types do have influences on the gui.
 * 
 * @author Jakob Jarosch
 * 
 */
public enum RegistrationEventTypes {
    /**
     * No Activity is defined error message.
     */
    NO_ACITIVTY_DEFINED,
    
    /**
     * PMP is not installed error message.
     */
    PMP_NOT_INSTALLED,
    
    /**
     * Registration process has been started.
     */
    START_REGISTRATION,
    
    /**
     * Registration has succeeded.
     */
    REGISTRATION_SUCCEED,
    
    /**
     * Registration has failed.
     */
    REGISTRATION_FAILED,
    
    /**
     * App is already registered at PMP.
     */
    ALREADY_REGISTERED,
    
    /**
     * The Service Features screen is going to be opened.
     */
    SF_SCREEN_OPENED,
    
    /**
     * The Service Features screen has been closed.
     */
    SF_SCREEN_CLOSED,
    
    /**
     * The Apps main Activity is going to be brought into the front.
     */
    OPEN_APP,
}
