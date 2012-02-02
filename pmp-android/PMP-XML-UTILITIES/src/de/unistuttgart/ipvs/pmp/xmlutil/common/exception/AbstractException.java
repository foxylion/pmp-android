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
 * 
 * @author Marcus Vetter
 * 
 */
public abstract class AbstractException extends RuntimeException {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 6776578152738678734L;

	/**
	 * Message of the exception
	 */
	private String message;

	/**
	 * The throwable
	 */
	private Throwable throwable;

	/**
	 * Constructor
	 * 
	 * @param message
	 *            of the exception
	 */
	public AbstractException(String details, Throwable throwable) {
		this.message = details;
		this.throwable = throwable;
	}

	/**
	 * Get the message of the exception
	 * 
	 * @return message of the exception
	 */
	@Override
	public String getMessage() {
		return this.message;
	}

	/**
	 * Get the throwable
	 * 
	 * @return the throwable
	 */
	public Throwable getParent() {
		return this.throwable;
	}

}
