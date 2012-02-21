/*
 * Copyright 2012 pmp-android development team
 * Project: Editor
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
package de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser;

/**
 * Thrown whenever the user wants to add the service, where PMP connects to, to
 * the AndroidManifest.xml but the service already exists
 * 
 * @author Thorsten Berberich
 * 
 */
public class PMPServiceAlreadyExists extends Exception {

	/**
	 * Auto generated serial
	 */
	private static final long serialVersionUID = -8944727079675898809L;

	/**
	 * Constructor to send a message
	 * 
	 * @param msg
	 *            message to display
	 */
	public PMPServiceAlreadyExists(String msg) {
		super(msg);
	}

}
