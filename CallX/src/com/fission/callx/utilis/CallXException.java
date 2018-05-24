/**
 * 
 */
package com.fission.callx.utilis;

import java.util.GregorianCalendar;

/**
 * @author Mohan.Jadhav
 *
 */
public class CallXException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Custom exception
	 * 
	 * @param message
	 */
	public CallXException(String message) {
		super(message);
		GregorianCalendar gcalendar = new GregorianCalendar();
		System.out.println(message + "____" + gcalendar.getTimeInMillis());
	}
}
