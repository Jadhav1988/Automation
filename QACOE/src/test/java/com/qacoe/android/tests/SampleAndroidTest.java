package com.qacoe.android.tests;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.QACOE.utils.AndroidUtils;

public class SampleAndroidTest extends AndroidUtils {

	String ipAddress = "127.0.0.1";
	String portNumber = "4723";
	String deviceName = "";
	String deviceId = "";
	String browserName = "Android";
	String deviceOsVersion = "";
	String deviceOsName = "Android";
	String appPackageName = "com.oneplus.calculator";
	String appActivityName = "com.oneplus.calculator.Calculator";

	@BeforeTest
	public void setup() throws Exception {
		startAppiumServer(ipAddress, portNumber);
		sleep(15000);
		openMobileApp(driver, deviceName, deviceId, browserName,
				deviceOsVersion, deviceOsName, appPackageName, appActivityName,
				ipAddress, portNumber);
	}

	@Test
	public void test() {

	}

	@AfterTest
	public void cleanUp() {
		stopAppiumServer();
		driver.quit();
	}

}
