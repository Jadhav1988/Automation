package com.fission.callx;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fission.callx.dashboard.AdminDahboard;
import com.fission.callx.dashboard.CallXFunctions;
import com.fission.callx.dashboard.SalesDashboard;
import com.fission.callx.utilis.CALLXConstants;
import com.fission.callx.utilis.CommonSettings;
import com.fission.callx.utilis.JerseyClient;
import com.fission.callx.utilis.Log;

public class SaleCount extends CommonSettings {

	private Logger log = Logger.getLogger(this.getClass());

	AdminDahboard dash = new AdminDahboard(driver);
	SalesDashboard salesDash = new SalesDashboard();
	CallXFunctions callXDash = new CallXFunctions();

	/** The client. */
	JerseyClient client = JerseyClient.getInstance();

	String environment = "STAGE";
	String category = "campaign";
	String fromDuration = "thirty-days";
	String toDuration = "yesterday";

	@BeforeTest
	public void setup() throws Exception {
		Log.startTestCase(Thread.currentThread().getName() + "-> "
				+ this.getClass().getSimpleName() + " >> "
				+ (new java.util.Date()));
		openWebbrowser(getBrowserName());
		openApplicationWebPage(driver, getTestEnvironment());
	}

	@Test(priority = 0, description = "Login as Admin")
	public void adminLogin() throws Exception {
		waitUntilInVisibility(driver, "admin.page.loader",
				CALLXConstants.GLOBAL_TIMEOUT);
		dash.adminLogin(driver, "admin@apl.com", "Redf0x32!!");

		waitUntilVisibility(driver, "dashboard.reports",
				CALLXConstants.GLOBAL_TIMEOUT);
		clickElement(driver, "dashboard.reports");

	}

	@Test(priority = 1, description = "Get API Data", dependsOnMethods = "adminLogin")
	public void compareUIAndAPISalesCount() throws Exception {

		waitUntilVisibility(driver, "Campaigns", CALLXConstants.GLOBAL_TIMEOUT);
		clickElement(driver, "Campaigns");

		waitUntilVisibility(driver, "Last_Thirty_Days",
				CALLXConstants.GLOBAL_TIMEOUT);
		clickElement(driver, "Last_Thirty_Days");

		waitUntilVisibility(driver, "Campaigns_Table_Data",
				CALLXConstants.GLOBAL_TIMEOUT);

		String responce = client.getClient(salesDash.getSalestURL(environment,
				category, fromDuration, toDuration));

		debugLogging(
				"Sales API URL: "
						+ salesDash.getSalestURL(environment, category,
								fromDuration, toDuration), "Info");

		JSONObject saleCountResponce = new JSONObject(responce);
		debugLogging("Actual Sales API Responce. " + saleCountResponce, "Info");

		JSONArray jarray = saleCountResponce.getJSONArray("data");

		debugLogging("sales JSONArray Data: " + jarray, "Info");

		salesDash.compareUiAndSaleCount(jarray, "campaignName",
				"Campaigns_Table_Data", "Campaigns_Table_Header");

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
		if (testResult.getStatus() == ITestResult.SKIP) {
			if (testResult.getThrowable().toString()
					.contains("No record Found")) {
				testResult.setThrowable(null);
			}
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
