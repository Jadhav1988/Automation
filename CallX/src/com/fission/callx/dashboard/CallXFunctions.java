package com.fission.callx.dashboard;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fission.callx.utilis.CALLXConstants;
import com.fission.callx.utilis.CallXException;
import com.fission.callx.utilis.CommonSettings;
import com.flipkart.zjsonpatch.JsonDiff;

/**
 * 
 * @author Mohan Jadhav
 *
 */

public class CallXFunctions extends CommonSettings {

	DateClass date = new DateClass();

	/**
	 * Sort the given JSONArray based upon the unique key value
	 * 
	 * @param arrayToSort
	 * @param key
	 * @return
	 */
	public JSONArray sortJSONArray(JSONArray arrayToSort, final String key) {

		JSONArray sortedJsonArray = new JSONArray();

		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < arrayToSort.length(); i++) {
			jsonValues.add(arrayToSort.getJSONObject(i));
		}
		Collections.sort(jsonValues, new Comparator<JSONObject>() {

			@Override
			public int compare(JSONObject a, JSONObject b) {
				String valA = new String();
				String valB = new String();

				try {
					valA = (String) a.get(key);
					valB = (String) b.get(key);
				} catch (JSONException e) {
					// do something
				}

				return valA.compareTo(valB);
				// if you want to change the sort order, simply use the
				// following:
				// return -valA.compareTo(valB);
			}
		});

		for (int i = 0; i < arrayToSort.length(); i++) {
			sortedJsonArray.put(jsonValues.get(i));
		}

		return sortedJsonArray;

	}

	/**
	 * Compare the two JSONArray and return the parameter difference
	 * 
	 * @param expectedJsonObject
	 * @param actualJsonObject
	 */
	public void jsonDiff(JSONArray expectedJsonObject,
			JSONArray actualJsonObject) {
		ObjectMapper mapper = new ObjectMapper();

		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		JsonNode expectedJson = mapper.convertValue(expectedJsonObject,
				JsonNode.class);
		JsonNode actualJson = mapper.convertValue(actualJsonObject,
				JsonNode.class);

		JsonNode jsonDiff = JsonDiff.asJson(expectedJson, actualJson);

		for (JsonNode d : jsonDiff) {
			if (d.get("op").textValue().equalsIgnoreCase("ADD")) {
				if (d.get("path").textValue().replace("/map/", "").trim()
						.contains("publisher_id")
						|| d.get("path").textValue().replace("/map/", "")
						.trim().contains("publisher_name")
						|| d.get("path").textValue().replace("/map/", "")
						.trim().contains("state")
						|| d.get("path").textValue().replace("/map/", "")
						.trim().contains("daypart")
						|| d.get("path").textValue().replace("/map/", "")
						.trim().contains("campaign_id")) {

				} else {
					System.err.println("ADD: -> "
							+ d.get("path").textValue().replace("/map/", "")
							.trim());
				}

			} else if (d.get("op").textValue().equalsIgnoreCase("REMOVE")) {
				if (d.get("path").textValue().replace("/map/", "").trim()
						.contains("show_details")
						|| d.get("path").textValue().replace("/map/", "")
						.trim().contains("sales")) {

				} else {
					System.err.println("REMOVE: -> "
							+ d.get("path").textValue().replace("/map/", "")
							.trim());

				}
			}
		}
		debugLogging(
				"Ignoring these API KEYS: "
						+ " publisher_id,  publisher_name, state, daypart, campaign_id ",
				"Info");

		debugLogging("Ignoring these UI KEYS: " + " sales, show_details ",
				"Info");
	}

	/**
	 * Get the list of keys from the JSON
	 * 
	 * @param array
	 * @return
	 */
	public static List<String> getKeySet(JSONArray array) {
		// if different json's are having keys then iterate the jsonObjects
		JSONObject object = array.getJSONObject(0);
		Set<String> keys = object.keySet();
		List<String> keyset = new ArrayList<String>(keys);

		return keyset;

	}

	/**
	 * Compare two JSONArray values and return the difference
	 * 
	 * @param uiData
	 * @param apiData
	 * @throws CallXException
	 */
	public void compareTwoJsonArrays(JSONArray uiData, JSONArray apiData,
			String sortCriteria) throws CallXException {

		List<String> keyFromObject = CallXFunctions.getKeySet(uiData);

		if (uiData.length() == apiData.length()) {

			debugLogging(uiData.length() + " = " + apiData.length(), "Info");

			for (int i = 0; i < uiData.length(); i++) {
				JSONObject expectedJson = uiData.getJSONObject(i);
				JSONObject actualJson = apiData.getJSONObject(i);

				for (int j = 0; j < keyFromObject.size(); j++) {
					if (expectedJson.get(keyFromObject.get(j)).equals(
							actualJson.get(keyFromObject.get(j)))) {
						debugLogging(keyFromObject.get(j) + " UI "
								+ expectedJson.get(keyFromObject.get(j))
								+ " = " + actualJson.get(keyFromObject.get(j)),
								"Info");
					} else {
						reporterLog(actualJson.get(sortCriteria) + " :  "
								+ keyFromObject.get(j) + " UI "
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
	public List<HashMap<String, Object>> formatedArray(JSONArray array) {

		List<String> keyFromObject = CallXFunctions.getKeySet(array);
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
					String str = data
							.getString(keyFromObject.get(i).toString());
					if (keyFromObject.get(i).equals(
							"avg_connect_duration_paid_calls")
							&& str.equals("00:00")) {
						rowData.put("avg_connect_duration_paid_calls",
								"0".toString());

					} else {

						String avgCon = data.getString((keyFromObject.get(i))
								.toString());
						String avgConArray[] = avgCon.split(":");
						int firstPart = Integer.parseInt(avgConArray[0]);
						int secondPart = Integer.parseInt(avgConArray[1]);
						int finalPart = (firstPart * 60) + secondPart;
						Double d = new Double(finalPart);

						final NumberFormat nf = NumberFormat.getInstance();
						nf.setMinimumFractionDigits(2);
						nf.setMaximumFractionDigits(2);
						nf.setGroupingUsed(false);

						rowData.put(keyFromObject.get(i), nf.format(d)
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
	public List<String> getKeys(WebDriver driver, String tableHeder)
			throws Exception {

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
