package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

public interface IDialogFinishedCallBack {
    
    public static int POSITIVE_BUTTON = -1;
    public static int NEGATIVE_BUTTON = -2;
    public static int NEUTRAL_BUTTON = -3;
    
    
    /**
     * Dialog call back function
     * 
     * @param dialogId
     *            The ID of the dialog
     * @param buttonId
     *            The ID of the chosen button
     */
    public void dialogFinished(int dialogId, int buttonId);
}
