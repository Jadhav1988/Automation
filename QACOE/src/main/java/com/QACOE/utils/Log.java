package com.QACOE.utils;


import org.apache.log4j.Logger;

public class Log {

	private static Logger Log = Logger.getLogger(Logger.class.getName());//

	public static void startTestCase(String sTestCaseName){
		Log.info("\n\n#############################################################################");
		Log.info("#############################################################################");
		Log.info("###########      "+sTestCaseName+ "     ###########");
		Log.info("#############################################################################");
		Log.info("#############################################################################");
	}

	public static void endTestCase(String sTestCaseName){
		Log.info("#############################################################################");
		Log.info("########################## Completed test case ##############################");
		Log.info("###########      "+sTestCaseName+ "     ###########");
		Log.info("#############################################################################\n\n");
	}

	public static void info(String message) {
		Log.info(message);
	}

	public static void warn(String message) {
		Log.warn(message);
	}

	public static void error(String message) {
		Log.error(message);
	}

	public static void fatal(String message) {
		Log.fatal(message);
	}

	public static void debug(String message) {
		Log.debug(message);
	}

}