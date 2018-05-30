package com.QACOE.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CONSTANT {

	public final static String ANDROID_OBJECT_REPOSITORY_PATH = "inputs"
			+ File.separator + "AndroidOR.properties";

	public final static String CONFIG_FILE_PATH = "inputs" + File.separator
			+ "config.properties";

	public static final int GLOBAL_TIMEOUT = 20;

	public static final String LOG4J_PROPERTIES_PATH = "log4j.properties";

	public static final DateFormat GLOBAL_DATE_FORMAT = new SimpleDateFormat(
			"dd-MMM-yyyy");

	public static final String ANDROID_LOG_FILE_PATH = System
			.getProperty("user.home")
			+ File.separator
			+ "Desktop"
			+ File.separator
			+ "Logs"
			+ File.separator
			+ CONSTANT.GLOBAL_DATE_FORMAT.format(new java.util.Date())
			+ File.separator + "ANDROID.log";

	public static final String SCREENSHOTS_DIRECTORY = System
			.getProperty("user.home") + File.separator + "Desktop";

	public static final String CHROME_DRIVER_PATH = "lib" + File.separator
			+ "chromedriver.exe";

	public static final String FIREFOX_DRIVER_PATH = "lib" + File.separator
			+ "geckodriver.exe";

	public static final String WEB_LOG_FILE_PATH = System
			.getProperty("user.home")
			+ File.separator
			+ "Desktop"
			+ File.separator
			+ "Logs"
			+ File.separator
			+ CONSTANT.GLOBAL_DATE_FORMAT.format(new java.util.Date())
			+ File.separator + "WEB.log";

	public static final String WEB_OBJECT_REPOSITORY_PATH = "inputs"
			+ File.separator + "WebOR.properties";

}
