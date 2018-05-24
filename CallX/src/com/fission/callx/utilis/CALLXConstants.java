/**
 * 
 */
package com.fission.callx.utilis;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Mohan.Jadhav
 * @CreatedOn 11-May-2017
 *
 */
public class CALLXConstants {

	/**
	 * Required Static constants
	 */

	public static final String SCREENSHOTS_DIRECTORY = System
			.getProperty("user.home") + File.separator + "Desktop";
	public static final DateFormat GLOBAL_DATE_FORMAT = new SimpleDateFormat(
			"dd-MMM-yyyy");
	public static final int GLOBAL_TIMEOUT = 20;
	public static final String ORDER_DETAILS = "inputs" + File.separator
			+ "OrderDetail.properties";

	public static final String CHROME_DRIVER_PATH = "lib" + File.separator
			+ "chromedriver.exe";
	public static final String FIREFOX_DRIVER_PATH = "lib" + File.separator
			+ "geckodriver.exe";
	public static final String LOG4J_PROPERTIES_PATH = "log4j.properties";
	public static final String LOG_FILE_PATH = System.getProperty("user.home")
			+ File.separator + "Desktop" + File.separator + "Logs"
			+ File.separator
			+ CALLXConstants.GLOBAL_DATE_FORMAT.format(new java.util.Date())
			+ File.separator + "logfile_CallX.log";
	public static final String OBJECT_REPOSITORY_PATH = "inputs"
			+ File.separator + "OR.properties";

	public static final String EMAIL_MESSAGE_BODY_REQUEST = "Waiting for customer call to execute automation,\n\nPlease find the calling details : ";
	public static final String EMAIL_MESSAGE_BODY_ANSWERED = "Call answered by the CCO, continuing automation execution ...";
	public static final String EMAIL_MESSAGE_BODY_REGARDS = "\n\n\nRegards\nAutomation team";

	public static final String MESSAGE_BODY_STYLE = "style=\"color: black; font-family: monospace; font-size: 11pt; white-space: pre;\"";
	public static final String TABLE_COLUMN_TAG_STYLE = "<td style=\"font-family: monospace; border: 1px solid #000000; text-align: left; padding: 8px; color: black;\">";
	public static final String TABLE_COLUMN_TAG_CLOSE = "</td>";
	public static final String TABLE_ROW_TAG_OPEN = "<tr>";
	public static final String TABLE_ROW_TAG_CLOSE = "</tr>";

	public static final String CONFIG_FILE_PATH = "inputs" + File.separator
			+ "config.properties";

}
