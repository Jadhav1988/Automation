package com.fission.callx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.fission.callx.dashboard.SalesDashboard;
import com.fission.callx.utilis.CALLXConstants;
import com.fission.callx.utilis.CommonSettings;
import com.fission.callx.utilis.DataConfig;
import com.fission.callx.utilis.JerseyClient;
import com.fission.callx.utilis.Log;

public class SalesCountDP extends CommonSettings {

	private Logger log = Logger.getLogger(this.getClass());

	AdminDahboard dash = new AdminDahboard(driver);
	SalesDashboard salesDash = new SalesDashboard();

	/** The client. */
	JerseyClient client = JerseyClient.getInstance();

	@DataProvider(name = "saleCount")
	public Object[][] getDataFromDataprovider(ITestContext c) throws Exception {

		DataConfig.openExcelFile("TestData", "Sales");
		Object[][] groupArray = null;

		List<Object[]> total = new ArrayList<>();
		int lastRowNum = DataConfig.getSheet().getLastRowNum();

		for (int i = 1; i <= lastRowNum; i++) {

			Object arr[] = new Object[9];
			for (int j = 0; j < 9; j++) {
				arr[j] = DataConfig.getCellData(i, j);
			}
			total.add(arr);

		}

		int size = total.size();
		groupArray = new Object[size][9];

		for (int i = 0; i < size; i++) {
			groupArray[i] = total.get(i);
		}

		return groupArray;
	}

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
		dash.adminLogin(driver, config.getProperty("UserName"),
				config.getProperty("Password"));

		waitUntilVisibility(driver, "dashboard.reports",
				CALLXConstants.GLOBAL_TIMEOUT);
		clickElement(driver, "dashboard.reports");

	}

	@Test(priority = 1, description = "Get API Data", dependsOnMethods = "adminLogin", dataProvider = "saleCount")
	public void compareUIAndAPISalesCount(String environment, String duration,
			String reportsCategory, String category, String fromDate,
			String toDate, String tableData, String tableHeader,
			String uniqueKey) throws Exception {

		waitUntilVisibility(driver, reportsCategory,
				CALLXConstants.GLOBAL_TIMEOUT);
		clickElement(driver, reportsCategory);

		waitUntilVisibility(driver, duration, CALLXConstants.GLOBAL_TIMEOUT);
		clickElement(driver, duration);

		waitUntilVisibility(driver, tableData, CALLXConstants.GLOBAL_TIMEOUT);

		String responce = client.getClient(salesDash.getSalestURL(environment,
				category, fromDate, toDate));

		JSONObject saleCountResponce = new JSONObject(responce);
		System.out.println("API Responce: " + saleCountResponce);

		JSONArray jarray = saleCountResponce.getJSONArray("data");

		salesDash.compareUiAndSaleCount(jarray, uniqueKey, tableData,
				tableHeader);

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
