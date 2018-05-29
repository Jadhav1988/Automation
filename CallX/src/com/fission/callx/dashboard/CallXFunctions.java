package com.fission.callx.dashboard;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.fission.callx.WithPublisher;
import com.fission.callx.utilis.CALLXConstants;
import com.fission.callx.utilis.CallXException;
import com.fission.callx.utilis.CommonSettings;

/**
 * 
 * @author Mohan Jadhav
 *
 */

public class CallXFunctions extends CommonSettings {

	DateClass date = new DateClass();

	/**
	 * Get the keys from the first JSONObject in the given JSONArrays
	 * 
	 * @param array
	 * @return
	 */
	public static List<String> getKeySetFromJSON(JSONArray array) {
		// if different json's are having keys then iterate the jsonObjects
		JSONObject object = array.getJSONObject(0);
		Set<String> keys = object.keySet();
		List<String> keyset = new ArrayList<String>(keys);

		return keyset;

	}

	/**
	 * Get the unique key and stores in a array
	 * 
	 * @param array
	 * @param uniqueKey
	 * @return
	 */
	public HashMap<WithPublisher, Integer> getUniqueKey(JSONArray array,
			String uniqueKey) {
		HashMap<WithPublisher, Integer> data = new HashMap<>();
		for (int i = 0; i < array.length(); i++) {
			WithPublisher uiPublisher = null;
			if (uniqueKey.contains(",")) {
				String[] values = uniqueKey.trim().split(",");
				String first = values[0];
				String second = values[1];
				uiPublisher = new WithPublisher(array.getJSONObject(i)
						.getString(first).replaceAll(",", ""), array
						.getJSONObject(i).getString(second).replaceAll(",", ""));
			} else {
				uiPublisher = new WithPublisher(array.getJSONObject(i)
						.getString(uniqueKey).replaceAll(",", ""), "");
			}
			data.put(uiPublisher, i);
		}
		return data;
	}

	/**
	 * Return index unique key index
	 * 
	 * @param uniqueKeyArrayList
	 * @param keyToMatch
	 * @return
	 */
	public int getUniqueKeyIndex(ArrayList<String> uniqueKeyArrayList,
			String keyToMatch) {
		for (int i = 0; i < uniqueKeyArrayList.size(); i++) {
			if (keyToMatch.equalsIgnoreCase(uniqueKeyArrayList.get(i))) {
				debugLogging(keyToMatch + " -> " + i + " Actual key"
						+ uniqueKeyArrayList.get(i), "Info");
				return i;
			}
		}
		return 0;
	}

	public String returnOneUniqueKey(String criteria) {
		if (criteria.contains(",")) {
			String[] values = criteria.trim().split(",");
			String second = values[1];
			return second;
		} else {
			return criteria;
		}

	}

	public void compareTwoJsonArrays(JSONArray uiData, JSONArray apiData,
			String sortCriteria) throws CallXException {

		List<String> keyFromObject = CallXFunctions.getKeySetFromJSON(uiData);
		HashMap<WithPublisher, Integer> names = getUniqueKey(apiData,
				sortCriteria);

		if (uiData.length() == apiData.length()) {
			debugLogging("sortCriteria : " + sortCriteria, "Info");
			debugLogging(uiData.length() + " = " + apiData.length(), "Info");
			WithPublisher uiPublisher = null;
			for (int i = 0; i < uiData.length(); i++) {
				if (sortCriteria.contains(",")) {
					String[] values = sortCriteria.trim().split(",");
					String first = values[0];
					String second = values[1];
					uiPublisher = new WithPublisher(uiData.getJSONObject(i)
							.getString(first), uiData.getJSONObject(i)
							.getString(second));
				} else {
					uiPublisher = new WithPublisher(uiData.getJSONObject(i)
							.getString(sortCriteria), "");
				}
				debugLogging("uiPublisher : " + uiPublisher, "Info");
				debugLogging("names : " + names, "Info");
				int index = names.get(uiPublisher);
				debugLogging("index : " + index, "info");

				JSONObject expectedJson = uiData.getJSONObject(i);
				JSONObject actualJson = apiData.getJSONObject(index);

				for (int j = 0; j < keyFromObject.size(); j++) {
					if (expectedJson.get(keyFromObject.get(j)).equals(
							actualJson.get(keyFromObject.get(j)))) {
						debugLogging(keyFromObject.get(j) + " UI "
								+ expectedJson.get(keyFromObject.get(j))
								+ " = " + actualJson.get(keyFromObject.get(j)),
								"Info");
					} else {
						reporterLog(expectedJson
								.get(returnOneUniqueKey(sortCriteria))
								+ " :  "
								+ keyFromObject.get(j)
								+ " UI "
								+ expectedJson.get(keyFromObject.get(j))
								+ " != " + actualJson.get(keyFromObject.get(j)));
						debugLogging(
								keyFromObject.get(j)
										+ " UI "
										+ expectedJson.get(keyFromObject.get(j))
										+ " != "
										+ actualJson.get(keyFromObject.get(j)),
								"Error");
					}

				}
				System.out.println("\n");

			}

		} else {
			debugLogging("Object count mismatched.." + uiData.length() + " !="
					+ apiData.length(), "Error");

			throw new CallXException("Object count mismatched..");
		}

	}

	/**
	 * Format Given JSON array as required
	 * 
	 * @param array
	 * @return
	 */
	public List<HashMap<String, Object>> formatJSONArray(JSONArray array) {

		List<String> keyFromObject = CallXFunctions.getKeySetFromJSON(array);
		List<HashMap<String, Object>> values = new ArrayList<HashMap<String, Object>>();

		for (int j = 0; j < array.length(); j++) {
			JSONObject data = array.getJSONObject(j);

			HashMap<String, Object> rowData = new HashMap<String, Object>();

			for (int i = 0; i < keyFromObject.size(); i++) {
				if (keyFromObject.get(i).equals("total_calls")
						|| keyFromObject.get(i).equals("unique_calls")
						|| keyFromObject.get(i).equals(
								"offers_not_available_count")
						|| keyFromObject.get(i).equals("paid_calls")) {

					rowData.put(keyFromObject.get(i), Integer.parseInt(data
							.get(keyFromObject.get(i)).toString()));

				} else if (keyFromObject.get(i).equals("repeat_calls")) {
					String repeat_calls = data.get("repeat_calls").toString();
					String repeat_callsArray[] = repeat_calls.split("\\s+");
					rowData.put("repeat_calls",
							Integer.parseInt(repeat_callsArray[0]));

				} else if (keyFromObject.get(i).equals("conv")
						|| keyFromObject.get(i).equals("unique_conv")) {
					int con = Integer.parseInt(data.get(keyFromObject.get(i))
							.toString());
					double d = (double) con / 100;
					rowData.put(keyFromObject.get(i), d);

				} else if (keyFromObject.get(i).equalsIgnoreCase("cost")
						|| keyFromObject.get(i).equals("avg_rpc")
						|| keyFromObject.get(i).equals("revenue")
						|| keyFromObject.get(i).equals("avg_cpc")
						|| keyFromObject.get(i).equals("profit")) {

					rowData.put(
							keyFromObject.get(i),
							Double.parseDouble(data.getString(
									keyFromObject.get(i)).toString()));

				} else if (keyFromObject.get(i).equals("avg_connect_duration")
						|| keyFromObject.get(i).equals(
								"avg_connect_duration_paid_calls")) {
					String avgCon = data.getString((keyFromObject.get(i))
							.toString());
					String[] avgArray = avgCon.split(":");

					final NumberFormat nf = NumberFormat.getInstance();
					nf.setMinimumFractionDigits(2);
					nf.setMaximumFractionDigits(2);
					nf.setGroupingUsed(false);

					if (avgArray.length > 2) {
						rowData.put(
								keyFromObject.get(i),
								nf.format(
										Integer.parseInt(avgArray[2])
												+ (60 * Integer
														.parseInt(avgArray[1]))
												+ (3600 * Integer
														.parseInt(avgArray[0])))
										.toString());
					} else {
						rowData.put(
								keyFromObject.get(i),
								nf.format(
										Integer.parseInt(avgArray[1])
												+ (60 * Integer
														.parseInt(avgArray[0])))
										.toString());
					}
				} else if (keyFromObject.get(i).equals("show_details")
						|| keyFromObject.get(i).equals("sale")) {
					// debugLogging("Ignoring :-> " + keyFromObject.get(i),
					// "Info");

				} else if (keyFromObject.get(i).equals("promo_number")) {
					rowData.put(keyFromObject.get(i),
							"1"
									+ data.getString(keyFromObject.get(i))
											.replaceAll("-", "").trim()
											.toString());
				} else {
					rowData.put(keyFromObject.get(i),
							data.get(keyFromObject.get(i)).toString());
				}

			}
			values.add(rowData);
		}
		return values;

	}

	/**
	 * Create a API URL based upon the parameter
	 * 
	 * @param environment
	 * @param category
	 * @param fromDuration
	 * @param toDuration
	 * @return
	 */
	public String getURL(String environment, String category,
			String fromDuration, String toDuration) {
		String URL = config.getProperty(environment)
				+ config.getProperty(category) + "?is_export=" + false
				+ "&ref=" + fromDuration + "&ref_from="
				+ date.getMeTheDate(fromDuration) + "&ref_to="
				+ date.getMeTheDate(toDuration);

		return URL;

	}

	/**
	 * Generate Key set based upon the header and do the necessary changes if
	 * Required
	 * 
	 * @param tableHeder
	 * @return
	 * @throws Exception
	 */
	public List<String> keysetFromTableHeader(WebDriver driver,
			String tableHeder) throws Exception {

		waitUntilVisibility(driver, tableHeder, CALLXConstants.GLOBAL_TIMEOUT);
		List<WebElement> header = driver.findElements(getLocator(tableHeder));

		List<String> keyset = new ArrayList<String>();
		List<String> key = new ArrayList<String>();

		for (int i = 0; i < header.size(); i++) {
			String keys = header.get(i).getText().replaceAll("[%.()]", "")
					.trim().replace(" ", "_").toLowerCase().trim();
			keyset.add(keys);
			if (keyset.get(i).equals("campaign")
					|| keyset.get(i).equals("offer")
					|| keyset.get(i).equals("publisher")
					|| keyset.get(i).equals("advertiser")) {
				key.add(keyset.get(i).replace(keyset.get(i),
						keyset.get(i) + "_name"));
			} else if (keyset.get(i).equals("calls")) {
				key.add(keyset.get(i).replace(keyset.get(i), "total_calls"));
			} else if (keyset.get(i).equals("conv_rate")
					|| keyset.get(i).equals("unique_conv_rate")) {
				key.add(keyset.get(i).replace("_rate", "").trim());
			} else if (keyset.get(i).equals("offers_not_available")) {
				key.add(keyset.get(i).replace(keyset.get(i),
						keyset.get(i) + "_count"));
			} else if (keyset.get(i).equals("description")) {
				key.add(keyset.get(i).replace(keyset.get(i),
						"promo_number_description"));
			} else {
				key.add(keyset.get(i));
			}
		}
		return key;
	}
}
