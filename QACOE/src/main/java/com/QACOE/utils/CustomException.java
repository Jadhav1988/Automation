/**
 * 
 */
package com.QACOE.utils;

import java.util.GregorianCalendar;

/**
 * @author Mohan.Jadhav
 *
 */
public class CustomException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Custom exception
	 * 
	 * @param message
	 */
	public CustomException(String message) {
		super(message);
		GregorianCalendar gcalendar = new GregorianCalendar();
		System.out.println(message + "____" + gcalendar.getTimeInMillis());
	}
}
