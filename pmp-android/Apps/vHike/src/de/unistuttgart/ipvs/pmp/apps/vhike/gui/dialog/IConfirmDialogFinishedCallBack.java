package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

public interface IConfirmDialogFinishedCallBack {
    
    /**
     * Dialog call back function for when the positive button was clicked
     * 
     * @param callbackFunctionID
     *            ID of the call back function, easier to handle more actions with the same dialog
     */
    public void confirmDialogPositive(int callbackFunctionID);
    
    
    /**
     * Dialog call back function for when the negative button was clicked
     * 
     * @param callbackFunctionID
     *            ID of the call back function, easier to handle more actions with the same dialog
     */
    public void confirmDialogNegative(int callbackFunctionID);
}
