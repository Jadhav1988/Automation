package com.fission.callx;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fission.callx.dashboard.AdminDahboard;
import com.fission.callx.dashboard.CallXFunctions;
import com.fission.callx.utilis.CALLXConstants;
import com.fission.callx.utilis.CommonSettings;
import com.fission.callx.utilis.DataConfig;
import com.fission.callx.utilis.JerseyClient;
import com.fission.callx.utilis.Log;

/**
 * @author Mohan Jadhav
 **/

public class ReportsDataValidationDP extends CommonSettings {

	private Logger log = Logger.getLogger(this.getClass());

	AdminDahboard dash = new AdminDahboard(driver);

	/** The client. */
	JerseyClient client = JerseyClient.getInstance();

	CallXFunctions callXDash = new CallXFunctions();

	JSONArray apiSortedArray;
	JSONArray uiSortedArray;

	@DataProvider(name = "reports")
	public Object[][] getDataFromDataprovider(ITestContext c) throws Exception {

		DataConfig.openExcelFile("TestData", "Reports");
		Object[][] groupArray = null;

		List<Object[]> total = new ArrayList<>();
		int lastRowNum = DataConfig.getSheet().getLastRowNum();

		for (int i = 1; i <= lastRowNum; i++) {

			Object arr[] = new Object[10];
			for (int j = 0; j < 10; j++) {
				arr[j] = DataConfig.getCellData(i, j);
			}
			total.add(arr);

		}

		int size = total.size();
		groupArray = new Object[size][10];

		for (int i = 0; i < size; i++) {
			groupArray[i] = total.get(i);
		}

		return groupArray;
	}

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

		waitUntilVisibility(driver, "dashboard.reports",
				CALLXConstants.GLOBAL_TIMEOUT);

		clickElement(driver, "dashboard.reports");
	}

	@Test(priority = 1, dataProvider = "reports", description = "Get Reports Data", dependsOnMethods = "adminLogin")
	public void validateUiAndApiReportsData(String environment,
			String categoryLocator, String durationLocator,
			String tableHeaderLocator, String tableDataLocator,
			String apiURLCategory, String fromDate, String toDate,
			String sortingCriteria, String noOfColoms) throws Exception {

		waitUntilVisibility(driver, categoryLocator,
				CALLXConstants.GLOBAL_TIMEOUT);

		clickElement(driver, categoryLocator);

		waitUntilVisibility(driver, durationLocator,
				CALLXConstants.GLOBAL_TIMEOUT);

		clickElement(driver, durationLocator);

		waitUntilVisibility(driver, tableHeaderLocator,
				CALLXConstants.GLOBAL_TIMEOUT);

		List<String> keySets = callXDash.getKeys(driver, tableHeaderLocator);

		waitUntilVisibility(driver, tableDataLocator,
				CALLXConstants.GLOBAL_TIMEOUT);

		List<HashMap<String, String>> values = dash.getTableData(driver,
				tableDataLocator, keySets, Integer.parseInt(noOfColoms));

		Map<String, List<HashMap<String, String>>> rowDataArray = new HashMap<String, List<HashMap<String, String>>>();
		rowDataArray.put("data", values);

		JSONObject uiJsonData = new JSONObject(rowDataArray);
		debugLogging("UI DATA: " + uiJsonData, "Info");

		// Format the jSon array in specific format to compare with the API JSON
		List<HashMap<String, Object>> formatedValue = callXDash
				.formatedArray(uiJsonData.getJSONArray("data"));
		Map<String, List<HashMap<String, Object>>> formatedArray = new HashMap<String, List<HashMap<String, Object>>>();
		formatedArray.put("data", formatedValue);
		JSONObject object = new JSONObject(formatedArray);

		debugLogging("Formated UI DATA : " + object, "Info");

		String URL = callXDash.getURL(environment, apiURLCategory, fromDate,
				toDate);

		String responce = client.getClient(URL);
		debugLogging("API URL : " + URL, "Info");

		JSONObject apiJsonData = new JSONObject(responce);

		debugLogging("Actual API responce : " + apiJsonData, "Info");

		apiSortedArray = callXDash.sortJSONArray(
				apiJsonData.getJSONArray("data"), sortingCriteria);
		uiSortedArray = callXDash.sortJSONArray(object.getJSONArray("data"),
				sortingCriteria);

		debugLogging("Sorted UI DATA: " + uiSortedArray, "Info");
		debugLogging("Sorted API DATA: " + apiSortedArray, "Info");

		callXDash.compareTwoJsonArrays(uiSortedArray, apiSortedArray,
				sortingCriteria);
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
