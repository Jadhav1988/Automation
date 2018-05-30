/**
 * 
 */
package com.QACOE.utils;

import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.github.genium_framework.appium.support.server.AppiumServer;
import com.github.genium_framework.server.ServerArguments;

/**
 * @author Mohan Jadhav
 *
 */
public class AndroidUtils {

	public AndroidDriver<WebElement> driver;
	public Properties orProperties;
	public Properties orConfig;

	public File NODE_EXECUTABLE_FILE_PATH = new File("C:" + File.separator
			+ "Program Files" + File.separator + "nodejs" + File.separator
			+ "node.exe");
	public File APPIUM_JAVASCRIPT_FILE_PATH = new File("C:" + File.separator
			+ "Users" + File.separator + "Fission Labs" + File.separator
			+ "AppData" + File.separator + "Roaming" + File.separator + "npm"
			+ File.separator + "node_modules" + File.separator + "appium"
			+ File.separator + "build" + File.separator + "lib"
			+ File.separator + "main.js");

	static List<File> screenshots = new ArrayList<File>();

	/**
	 * Constructor
	 * 
	 * @param driver
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public AndroidUtils() {
		try {
			configureLog4jPropertiesFile();
			orProperties = loadProperties();
			orConfig = loadConfigFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param ipAddress
	 * @param portNumber
	 * @throws Exception
	 *             @ Start AppiumServer
	 */
	public void startAppiumServer(String ipAddress, String portNumber)
			throws Exception {
		ServerArguments serverArguments = new ServerArguments();
		serverArguments.setArgument("--address", ipAddress);
		serverArguments.setArgument("--chromedriver-port", 9516);
		serverArguments.setArgument("--bootstrap-port", portNumber);
		serverArguments.setArgument("--no-reset", true);
		serverArguments.setArgument("--local-timezone", true);
		AppiumServer appiumServer = new AppiumServer(NODE_EXECUTABLE_FILE_PATH,
				APPIUM_JAVASCRIPT_FILE_PATH, serverArguments);
		appiumServer.startServer();
	}

	/**
	 * @Stop appiumServer
	 */
	public void stopAppiumServer() {
		ServerArguments serverArguments = new ServerArguments();
		AppiumServer appiumServer = new AppiumServer(serverArguments);
		appiumServer.stopServer();
	}

	/**
	 * @param deviceName
	 * @param browserName
	 * @param osVersion
	 * @param osName
	 * @param appPakageName
	 * @param appActivityName
	 * @throws IOException
	 * 
	 *             Open the particular mobile application
	 */
	public void openMobileApp(String deviceName, String browserName,
			String osVersion, String osName, String appPakageName,
			String appActivityName, String nextAndCurrentActivity,
			String ipAddressAndPortNo) throws IOException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", deviceName);
		capabilities.setCapability(CapabilityType.BROWSER_NAME, browserName);
		capabilities.setCapability(CapabilityType.VERSION, osVersion);
		capabilities.setCapability("platformName", osName);
		capabilities.setCapability("appPackage", appPakageName);
		capabilities.setCapability("appActivity", appActivityName);
		capabilities.setCapability("appWaitActivity", nextAndCurrentActivity);
		driver = new AndroidDriver<WebElement>(new URL(ipAddressAndPortNo),
				capabilities);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public AndroidDriver<WebElement> openMobileApp(
			AndroidDriver<WebElement> driver, String devName, String deviceId,
			String browserName, String osVersion, String osName,
			String appPakageName, String appActivityName, String ip, String port)
					throws IOException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("udid", deviceId);
		capabilities.setCapability("deviceName", devName);
		capabilities.setCapability(CapabilityType.BROWSER_NAME, browserName);
		capabilities.setCapability(CapabilityType.VERSION, osVersion);
		capabilities.setCapability("platformName", osName);
		capabilities.setCapability("appPackage", appPakageName);
		capabilities.setCapability("appActivity", appActivityName);
		driver = new AndroidDriver<WebElement>(new URL("http://" + ip + ":"
				+ port + "/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		debugLogging("Initiated driver for device Id: " + deviceId, "Info");

		return driver;
	}

	public void startAppiumServer(String ipAddress, String portNumber,
			String cdPort) throws Exception {
		ServerArguments serverArguments = new ServerArguments();
		serverArguments.setArgument("--address", ipAddress);
		serverArguments.setArgument("--port", portNumber);
		serverArguments.setArgument("--chromedriver-port", cdPort);
		serverArguments.setArgument("--no-reset", true);
		serverArguments.setArgument("--local-timezone", true);
		AppiumServer appiumServer = new AppiumServer(serverArguments);
		appiumServer.startServer();
	}

	/**
	 * @param millis
	 * @throws InterruptedException
	 */
	public void sleep(long millis) throws InterruptedException {
		debugLogging("Holding current thread for: " + (millis / (double) 1000)
				+ " seconds ..", "Info");
		Thread.sleep(millis);
	}

	/**
	 * Configure log4j.xml file for logging functionality
	 */

	public void configureLog4jPropertiesFile() {
		System.setProperty("logfile.name", CONSTANT.ANDROID_LOG_FILE_PATH);
		PropertyConfigurator.configure(CONSTANT.LOG4J_PROPERTIES_PATH);
	}

	/**
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 *             Load Properties file
	 */

	public Properties loadProperties() throws FileNotFoundException,
	IOException {
		Properties locator = new Properties();
		locator.load(new FileInputStream(
				CONSTANT.ANDROID_OBJECT_REPOSITORY_PATH));
		return locator;
	}

	/**
	 * Load config file
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Properties loadConfigFile() throws FileNotFoundException,
	IOException {
		Properties locator = new Properties();
		locator.load(new FileInputStream(CONSTANT.CONFIG_FILE_PATH));
		return locator;
	}

	/**
	 * @param logMessage
	 * @param infoOrError
	 * 
	 *            Print messages in log file as well as console
	 */

	public void debugLogging(String logMessage, String infoOrError) {
		if (infoOrError.equals("Info")) {
			Log.info(logMessage);
		} else if (infoOrError.equals("Error")) {
			Log.error(logMessage);
		} else {
			System.out.println(logMessage);
		}
	}

	public String formatInteger(int input) {
		return String.format("%02d", input);
	}

	/**
	 * @throws IOException
	 * 
	 *             Capture Screenshot with name as time stamp
	 */

	public void captureScreenShot(AndroidDriver<WebElement> driver)
			throws IOException {
		File imageFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		GregorianCalendar gcalendar = new GregorianCalendar();
		String failureImageFileName = CONSTANT.SCREENSHOTS_DIRECTORY
				+ File.separator + "Screenshots" + File.separator
				+ CONSTANT.GLOBAL_DATE_FORMAT.format(new java.util.Date())
				+ File.separator + formatInteger(gcalendar.get(Calendar.HOUR))
				+ "-" + formatInteger(gcalendar.get(Calendar.MINUTE)) + "_"
				+ gcalendar.getTimeInMillis() + ".png";
		File failureImageFile = new File(failureImageFileName);
		FileUtils.moveFile(imageFile, failureImageFile);
		screenshots.add(failureImageFile);
		debugLogging("Something went wrong. Check Screenshot.", "Error");
	}

	/**
	 * @param by
	 * @param timeOut
	 * 
	 *            Wait for element to be present in page
	 * @throws Exception
	 */

	public void waitForElementPresence(AndroidDriver<?> driver,
			String elementLocator, int timeOut) throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		long startTime = System.nanoTime();
		wait.until(ExpectedConditions
				.presenceOfElementLocated(getLocator(elementLocator)));
		long endTime = System.nanoTime();
		debugLogging("Waited: " + ((endTime - (double) startTime) / 1000000000)
				+ " seconds for element: " + elementLocator, "Info");

	}

	/**
	 * @param elementLocator
	 * @param timeOut
	 * @throws Exception
	 * 
	 *             Wait for element to be clickable in page
	 */

	public void waitForElemToBeClickable(AndroidDriver<?> driver,
			String elementLocator, int timeOut) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		long startTime = System.nanoTime();
		wait.until(ExpectedConditions
				.elementToBeClickable(getLocator(elementLocator)));
		long endTime = System.nanoTime();
		debugLogging("Waited: " + ((endTime - (double) startTime) / 1000000000)
				+ " seconds for element: " + elementLocator, "Info");
	}

	/**
	 * @param elementLocator
	 * @param timeOut
	 * @throws Exception
	 * 
	 *             Wait for element to be visible in the page
	 */

	public void waitUntilVisibility(AndroidDriver<?> driver,
			String elementLocator, int timeOutInSeconds) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		long startTime = System.nanoTime();
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(getLocator(elementLocator)));
		long endTime = System.nanoTime();
		debugLogging("Waited: " + ((endTime - (double) startTime) / 1000000000)
				+ " seconds for element: " + elementLocator, "Info");
	}

	/**
	 * @param driver
	 * @param elementLocator
	 * @param expectedDate
	 * @throws Exception
	 * 
	 *             Fill date in date searching
	 */

	public void enterdate(AndroidDriver<?> driver, String elementLocator,
			String expectedDate) throws Exception {
		WebElement idOfBox = driver.findElement(getLocator(elementLocator));
		((JavascriptExecutor) driver).executeScript("document.getElementById('"
				+ idOfBox.getAttribute("id")
				+ "').removeAttribute('readonly',0);"); // Enables the from date
		// box
		Thread.sleep(2000);
		// WebElement fromDateBox= idOfBox;
		// idOfBox.clear();
		Thread.sleep(450);
		idOfBox.sendKeys(expectedDate);
		debugLogging("Filled date " + expectedDate, "Info");
	}

	/**
	 * @param elementLocator
	 * @return
	 * @throws Exception
	 * 
	 *             Return locator of particular element
	 */

	public By getLocator(String elementLocator) throws Exception {
		Properties p = loadProperties();
		String locator = p.getProperty(elementLocator);

		String locatorType = locator.split(":")[0];
		String locatorValue = locator.split(":")[1];

		if (locatorType.toLowerCase().equals("id"))
			return By.id(locatorValue);
		else if (locatorType.toLowerCase().equals("name"))
			return By.name(locatorValue);
		else if ((locatorType.toLowerCase().equals("classname"))
				|| (locatorType.toLowerCase().equals("class")))
			return By.className(locatorValue);
		else if ((locatorType.toLowerCase().equals("tagname"))
				|| (locatorType.toLowerCase().equals("tag")))
			return By.tagName(locatorValue);
		else if ((locatorType.toLowerCase().equals("linktext"))
				|| (locatorType.toLowerCase().equals("link")))
			return By.linkText(locatorValue);
		else if (locatorType.toLowerCase().equals("partiallinktext"))
			return By.partialLinkText(locatorValue);
		else if ((locatorType.toLowerCase().equals("cssselector"))
				|| (locatorType.toLowerCase().equals("css")))
			return By.cssSelector(locatorValue);
		else if (locatorType.toLowerCase().equals("xpath"))
			return By.xpath(locatorValue);
		else
			throw new Exception("Locator type '" + locatorType
					+ "' not defined!!");
	}

	/**
	 * @param elementLocator
	 * @throws Exception
	 * 
	 *             Click element
	 */

	public void clickElement(AndroidDriver<?> driver, String elementLocator)
			throws Exception {
		WebElement element = driver.findElement(getLocator(elementLocator));
		float opacity = Float.parseFloat(element.getCssValue("opacity"));
		if (opacity == 1 && element.isEnabled()) {
			debugLogging(
					"Opacity of button "
							+ element.getText().replaceAll("[\r\n]+", " :")
							+ " is: " + opacity, "Info");
			debugLogging("Is element enabled: " + element.isEnabled(), "Info");
			debugLogging(
					"Clicked on : "
							+ element.getText().replaceAll("[\r\n]+", " "),
					"Info");
			element.click();
		} else {
			debugLogging("Element is already enabled: " + element.getText(),
					"Error");
		}
	}

	/**
	 * @param elementLocator
	 * @param value
	 * @throws Exception
	 * 
	 *             Fill value to any element
	 */

	public void fillValue(AndroidDriver<?> driver, String elementLocator,
			String value) throws Exception {
		WebElement element = driver.findElement(getLocator(elementLocator));
		element.clear();
		element.sendKeys(value);
		debugLogging("Filled value: " + value, "Info");
	}

	/**
	 * @param elementName
	 * @param value
	 * @throws IOException
	 * 
	 *             Select value from dropdown using value
	 */

	public void selectValueFromDropDowm(AndroidDriver<WebElement> driver,
			String elementName, String value) throws IOException {
		try {
			Select dropdown = new Select(
					driver.findElement(getLocator(elementName)));
			dropdown.selectByVisibleText(value);
			debugLogging("Selected value: " + value, "Info");
		} catch (Exception e) {
			debugLogging("Unable to select value from the drop down.", "Error");
			captureScreenShot(driver);
		}
	}

	/**
	 * @param elementLocator
	 * @return
	 * @throws Exception
	 * 
	 *             Get text from element and return it as well as print it
	 */

	public String getTextFromElement(AndroidDriver<?> driver,
			String elementLocator) throws Exception {
		String text = driver.findElement(getLocator(elementLocator)).getText();
		debugLogging(text, "Info");
		return text;
	}

	/**
	 * @param driver
	 * @param option
	 * @throws IOException
	 * 
	 * 
	 */
	public void chooseOption(AndroidDriver<WebElement> driver, String option)
			throws IOException {
		List<WebElement> li = driver.findElements(By
				.className("android.widget.TextView"));
		int size = li.size();

		if (size > 0) {
			boolean bool = false;
			for (int i = 0; i < size; i++) {
				if (li.get(i).getText().contains(option)) {
					li.get(i).click();
					bool = true;
					break;
				}
			}

			if (!bool) {
				debugLogging("No options present with given name: " + option,
						"Error");
				captureScreenShot(driver);
			}
		} else {
			debugLogging("No options present ..", "Error");
			captureScreenShot(driver);
		}
	}

	/**
	 * @param driver
	 * @throws IOException
	 * 
	 *             Hide the keyboard if displayed
	 */

	public void hideKeyBoard(AndroidDriver<WebElement> driver)
			throws IOException {
		try {
			driver.hideKeyboard();
		} catch (Exception e) {
			debugLogging("Key board is not opened ..", "Error");
			debugLogging("Continuing without closing keyboard ..", "Info");
			captureScreenShot(driver);
		}
	}

	/**
	 * Writes the user message in testNg email report
	 * 
	 * @param message
	 */
	public void reporterLog(String message) {
		Reporter.log("<font color=\"red\"><b>" + message + "</b></font><br/>");
	}

}
