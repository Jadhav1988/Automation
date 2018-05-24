package com.fission.callx.dashboard;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 
 * @author Mohan Jadhav
 *
 */

import com.fission.callx.utilis.CommonSettings;

public class SalesDashboard extends CommonSettings {

	DateClass date = new DateClass();

	/**
	 * Construct the sales API URL based upon the parameter value
	 * 
	 * @param environment
	 * @param category
	 * @param fromDuration
	 * @param toDuration
	 * @return
	 */
	public String getSalestURL(String environment, String category,
			String fromDuration, String toDuration) {
		String URL = config.getProperty(environment) + "/etl/reports/"
				+ category + "?ref=" + fromDuration + "&ref_from="
				+ date.getMeTheDate(fromDuration) + "&ref_to="
				+ date.getMeTheDate(toDuration);

		return URL;

	}

	/**
	 * Get the sales column position from the table
	 * 
	 * @param header
	 * @return
	 * @throws Exception
	 */
	public int salesPosition(String header) throws Exception {
		int i = 0;
		List<WebElement> pos = driver.findElements(getLocator(header));
		for (; i < pos.size(); i++) {
			if (pos.get(i).getText().equals("Sale"))
				return i + 1;
		}
		return i + 1;
	}

	/**
	 * Compare API and UI Sales count
	 * 
	 * @param jArray
	 * @param uniqueKey
	 * @param tableData
	 * @param tableHeader
	 * @throws Exception
	 * @throws Exception
	 */
	public void compareUiAndSaleCount(JSONArray jArray, String uniqueKey,
			String tableData, String tableHeader) throws Exception, Exception {

		if (jArray.length() > 0) {

			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json = jArray.getJSONObject(i);
				fillValue(driver, "search", json.get(uniqueKey).toString());
				sleep(850);

				String locator = getLocator(tableData) + "["
						+ Integer.toString(salesPosition(tableHeader)) + "]";

				List<WebElement> salesCount = driver.findElements(By
						.xpath(locator.replace("By.xpath:", "").trim()));
				if (salesCount.size() > 0) {

					for (int j = 0; j < salesCount.size(); j++) {

						if (salesCount.get(j).getText().toString()
								.equals(json.get("saleCount").toString())) {
							debugLogging(json.get(uniqueKey)
									+ " -> SaleCount UI: "
									+ salesCount.get(j).getText() + " = "
									+ json.get("saleCount"), "Info");
						} else {
							reporterLog(json.get(uniqueKey)
									+ " -> SaleCount UI: "
									+ salesCount.get(j).getText() + " != "
									+ json.get("saleCount"));
							captureScreenShot(driver);
						}

					}

				} else {
					reporterLog("No record Found with the search :  "
							+ json.get(uniqueKey) + " ->   SaleCount  : "
							+ json.get("saleCount"));

					debugLogging(
							"No record Found with the search "
									+ json.get(uniqueKey) + " SaleCount :"
									+ json.get("saleCount"), "Error");
					captureScreenShot(driver);
				}
			}
		} else {
			reporterLog("Responce aaray is empty.. " + jArray);
		}

	}

}
