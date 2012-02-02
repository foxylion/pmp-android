/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
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
package de.unistuttgart.ipvs.pmp.xmlutil.common.exception;

/**
 * This class is used to create exceptions within the information sets with a
 * type and message.
 * 
 * @author Marcus Vetter
 * 
 */
public class ISException extends AbstractException {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -4639871052954638618L;

	/**
	 * Type of the parser exception
	 */
	private Type type;

	/**
	 * Constructor
	 * 
	 * @param type
	 *            Type of the exception
	 * @param message
	 *            Message of the exception
	 * @param throwable
	 */
	public ISException(Type type, String message, Throwable throwable) {
		super(message, throwable);
		this.type = type;
	}

	/**
	 * Constructor
	 * 
	 * @param type
	 *            Type of the exception
	 * @param message
	 *            Message of the exception
	 */
	public ISException(Type type, String message) {
		this(type, message, null);
	}

	/**
	 * Get the type of the exception
	 * 
	 * @return type of the exception
	 */
	public Type getType() {
		return this.type;
	}

	public enum Type {

		/**
		 * A name with the same locale already exists
		 */
		NAME_WITH_SAME_LOCALE_ALREADY_EXISTS,

		/**
		 * A description with the same locale already exists
		 */
		DESCRIPTION_WITH_SAME_LOCALE_ALREADY_EXISTS,

		/**
		 * A privacy setting with the same identifier already exists within the
		 * same required resource group
		 */
		PRIVACY_SETTING_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,

		/**
		 * A service feature with the same identifier already exists
		 */
		SERVICE_FEATURE_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,

		/**
		 * A required Resourcegroup with the same identifier already exists
		 */
		REQUIRED_RESOUCEGROUP_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,

	}

}
