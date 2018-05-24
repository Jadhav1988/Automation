/**
 * 
 */
package com.fission.callx.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;

import com.fission.callx.utilis.CALLXConstants;
import com.fission.callx.utilis.CommonSettings;

/**
 * @author mohan.jadhav
 *
 */
public class AdminDahboard extends CommonSettings {

	WebDriver driver;

	public AdminDahboard(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Login to the callX application
	 * 
	 * @param userName
	 * @param password
	 * @throws Exception
	 */
	public void adminLogin(WebDriver driver, String userName, String password)
			throws Exception {
		waitUntilVisibility(driver, "login.email.address",
				CALLXConstants.GLOBAL_TIMEOUT);
		fillValue(driver, "login.email.address", userName);

		waitUntilVisibility(driver, "login.password",
				CALLXConstants.GLOBAL_TIMEOUT);
		fillValue(driver, "login.password", password);

		waitUntilVisibility(driver, "login.submit",
				CALLXConstants.GLOBAL_TIMEOUT);
		clickElement(driver, "login.submit");
	}

	/**
	 * Get Table data from the UI
	 * 
	 * @param driver
	 * @param tableDataLocator
	 *            - table data locator
	 * @param keySet
	 *            - set of keys
	 * @param noOfColumns
	 *            - no of columns
	 * @return List<HashMap<String, String>>
	 * @throws Exception
	 */
	public List<HashMap<String, String>> getTableData(WebDriver driver,
			String tableDataLocator, List<String> keySet, int noOfColumns)
					throws Exception {

		List<WebElement> data = driver
				.findElements(getLocator(tableDataLocator));

		List<HashMap<String, String>> values = new ArrayList<HashMap<String, String>>();

		// -noOfColoumns is to exclude the last row because the last row is
		// total
		if (data.size() > 3) {
			for (int i = 0; i < data.size() - noOfColumns - 1;) {
				HashMap<String, String> rowData = new HashMap<String, String>();
				int size = 0;
				while (size <= noOfColumns) {
					rowData.put(keySet.get(size), data.get(i).getText()
							.replaceAll("[,$%]", ""));
					i++;
					size++;
				}

				values.add(rowData);
			}
		} else {
			reporterLog("No Record Found");
			throw new SkipException("No record Found");
		}

		return values;
	}
}
