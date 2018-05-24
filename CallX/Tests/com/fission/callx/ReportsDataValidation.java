package com.fission.callx;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.fission.callx.utilis.CALLXConstants;
import com.fission.callx.utilis.CallXException;
import com.fission.callx.utilis.CommonSettings;
import com.fission.callx.utilis.JerseyClient;
import com.fission.callx.utilis.Log;

/**
 * 
 * @author Mohan Jadhav
 *
 */

public class ReportsDataValidation extends CommonSettings {

	private Logger log = Logger.getLogger(this.getClass());

	AdminDahboard dash = new AdminDahboard(driver);

	/** The client. */
	JerseyClient client = JerseyClient.getInstance();

	CallXFunctions callXDash = new CallXFunctions();

	JSONArray apiSortedArray;
	JSONArray uiSortedArray;

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
				CALLXConstants.GLOBAL_TIMEOUT);
		dash.adminLogin(driver, "admin@apl.com", "Redf0x32!!");
	}

	@Test(priority = 1, description = "Get Table data Reports -> Campaigns", dependsOnMethods = "adminLogin")
	public void getReportData() throws Exception {
		waitUntilVisibility(driver, "dashboard.reports",
				CALLXConstants.GLOBAL_TIMEOUT);
		clickElement(driver, "dashboard.reports");

		waitUntilVisibility(driver, "Campaigns", CALLXConstants.GLOBAL_TIMEOUT);

		clickElement(driver, "Campaigns");

		waitUntilVisibility(driver, "Last_Month",
				CALLXConstants.GLOBAL_TIMEOUT);

		clickElement(driver, "Last_Month");

		waitUntilVisibility(driver, "Campaigns_Table_Data",
				CALLXConstants.GLOBAL_TIMEOUT);

		List<String> keySets = callXDash.getKeys(driver,
				"Campaigns_Table_Header");

		List<HashMap<String, String>> values = dash.getTableData(driver,
				"Campaigns_Table_Data", keySets, 15);

		Map<String, List<HashMap<String, String>>> rowDataArray = new HashMap<String, List<HashMap<String, String>>>();
		rowDataArray.put("data", values);

		JSONObject uiJsonData = new JSONObject(rowDataArray);

		// Format the jSon array in specific format to compare with the API JSON
		List<HashMap<String, Object>> formatedValue = callXDash
				.formatedArray(uiJsonData.getJSONArray("data"));
		Map<String, List<HashMap<String, Object>>> formatedArray = new HashMap<String, List<HashMap<String, Object>>>();
		formatedArray.put("data", formatedValue);
		JSONObject object = new JSONObject(formatedArray);

		debugLogging("UI DATA: " + uiJsonData, "Info");
		debugLogging("Formated UI DATA : " + object, "Info");

		String responce = client
				.getClient("http://52.43.226.123/reports-v2/campaigns/report?is_export=false&ref=last-month&ref_from=Apr+1,+2018&ref_to=Apr+30,+2018");

		JSONObject apiJsonData = new JSONObject(responce);

		debugLogging("Actual API responce : " + apiJsonData, "Info");

		apiSortedArray = callXDash.sortJSONArray(
				apiJsonData.getJSONArray("data"), "campaign_name");
		uiSortedArray = callXDash.sortJSONArray(object.getJSONArray("data"),
				"campaign_name");

		debugLogging("Sorted UI DATA: " + uiSortedArray, "Info");
		debugLogging("Sorted API DATA: " + apiSortedArray, "Info");

	}

	@Test(priority = 2, description = "Comapre UI data and API responce", dependsOnMethods = "getReportData")
	public void validateUIAnaAPIData() throws CallXException {
		callXDash.compareTwoJsonArrays(uiSortedArray, apiSortedArray,
				"campaign_name");
		callXDash.jsonDiff(uiSortedArray, apiSortedArray);
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
