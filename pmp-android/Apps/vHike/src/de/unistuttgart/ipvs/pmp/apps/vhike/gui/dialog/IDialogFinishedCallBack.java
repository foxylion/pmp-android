/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
