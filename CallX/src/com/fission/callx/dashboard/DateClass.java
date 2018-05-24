package com.fission.callx.dashboard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author Mohan Jadhav
 *
 */
public class DateClass {

	SimpleDateFormat date = new SimpleDateFormat("MMM+dd,+yyyy");
	Date d = new Date();

	/**
	 * Subtract the no of days from the current date based upon the parameter
	 * value
	 * 
	 * @param days
	 * @return
	 */
	public Date daysMinues(int days) {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -days);
		return cal.getTime();
	}

	/**
	 * Get the month First date
	 * 
	 * @param month
	 *            subtract the month
	 * @return
	 */
	public Date getMonthsFirstDate(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * Get the month last date
	 * 
	 * @return
	 */
	public Date getLastMonthLastDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);

		int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, max);

		return calendar.getTime();
	}

	/**
	 * Get todays date
	 * 
	 * @return
	 */
	public String getMeToday() {
		return date.format(d);
	}

	/**
	 * Returns the required date based upon the parameter
	 * 
	 * @param duration
	 * @return
	 */
	public String getMeTheDate(String duration) {
		if (duration.equalsIgnoreCase("today")) {
			return getMeToday();
		} else if (duration.equalsIgnoreCase("yesterday")) {
			return date.format(daysMinues(1));
		} else if (duration.equalsIgnoreCase("seven-days")) {
			return date.format(daysMinues(7));
		} else if (duration.equalsIgnoreCase("thirty-days")) {
			return date.format(daysMinues(30));
		} else if (duration.equalsIgnoreCase("this-year")) {
			return "Jan+1,+2018";
		} else if (duration.equalsIgnoreCase("last-month")) {
			return date.format(getMonthsFirstDate(-1));
		} else if (duration.equalsIgnoreCase("this-month")) {
			return date.format(getMonthsFirstDate(0));
		} else if (duration.equalsIgnoreCase("last-month_last")) {
			return date.format(getLastMonthLastDate());
		} else
			return "Invalid duration";
	}

}