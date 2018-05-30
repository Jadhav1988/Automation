package com.qacoe.web.tests;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.QACOE.utils.CONSTANT;
import com.QACOE.utils.CommonSettings;
import com.QACOE.utils.Log;
import com.web.dashboard.AdminDahboard;

public class SampleTest extends CommonSettings {

	private Logger log = Logger.getLogger(this.getClass());

	AdminDahboard dash = new AdminDahboard(driver);

	@BeforeTest
	public void setup() throws FileNotFoundException, IOException {
		Log.startTestCase(Thread.currentThread().getName() + "-> "
				+ this.getClass().getSimpleName() + " >> "
				+ (new java.util.Date()));
		openWebbrowser(getBrowserName());
		openApplicationWebPage(driver, getTestEnvironment());
	}

	@Test(priority = 0, description = "Login as Admin")
	public void adminLogin() throws Exception {
		waitUntilInVisibility(driver, "admin.page.loader",
				CONSTANT.GLOBAL_TIMEOUT);
		dash.adminLogin(driver, "admin@apl.com", "Redf0x32!!");
	}

	@AfterMethod(alwaysRun = true)
	public void failureCatcher(ITestResult testResult) throws IOException {
		if (testResult.getStatus() == ITestResult.FAILURE) {
			log.error("Method : " + testResult.getName() + " status: "
					+ testResult.getStatus());
			captureScreenShot(driver, this.getClass().getSimpleName() + "_"
					+ testResult.getName() + "_failed");

			log.info("*****************************************");
			log.error("******************  Message  *******************\n"
					+ testResult.getThrowable().getMessage() + "\n");
		}

	}

	@AfterTest()
	public void browserCleanUp() {
		Log.endTestCase(Thread.currentThread().getName() + "-> "
				+ this.getClass().getSimpleName() + " >> "
				+ (new java.util.Date()));

		driver.close();
		driver.quit();
	}

}
