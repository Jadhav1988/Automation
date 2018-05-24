/**
 * 
 */
package com.fission.callx.utilis;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

/**
 * @author Mohan.Jadhav
 *
 */
public class CommonSettings {

	// private Logger log = Logger.getLogger(this.getClass());

	public static WebDriver driver;
	public Properties properties;
	public Properties config;

	static List<File> screenshots = new ArrayList<File>();

	/**
	 * 
	 * @author Mohan.jadhav
	 * @CreatedOn -
	 * @LastModified 03-Apr-2017 11:34:56 AM
	 * 
	 *               Default constructor to initialize properties file (Object
	 *               repository) and configure XML file for log4j
	 */
	public CommonSettings() {
		try {
			configureLog4jPropertiesFile();
			properties = loadObjectRepositoryProperties();
			config = loadConfigFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open web-browser for provided driver instance based on input, i.e.
	 * allowed options to open firefox or chrome browser which is to be passed
	 * as parameter with preferences like - open browser maximized, disable
	 * info-bars, disable notifications about password save notifications
	 * 
	 * @param driver
	 * @param browserName
	 * @return Returns webdriver instance which is passed as parameter
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @author Mohan.jadhav
	 * @CreatedOn -
	 * @LastModified 03-Apr-2017 11:31:06 AM
	 * 
	 */
	public WebDriver openWebbrowser(WebDriver driver, String browserName)
			throws FileNotFoundException, IOException {
		configureLog4jPropertiesFile();

		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("credentials_enable_service", false);
		prefs.put("password_manager_enabled", false);

		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		options.addArguments("disable-infobars");
		options.setExperimentalOption("prefs", prefs);

		if (browserName.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					CALLXConstants.CHROME_DRIVER_PATH);
			driver = new ChromeDriver(options);
			debugLogging("Chrome browser is initialized .. ", "Info");
		} else if (browserName.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
			debugLogging("FireFox browser is initialized ..", "Info");
		}

		return driver;
	}

	/**
	 * Open web-browser based on input, i.e. allowed options to open firefox or
	 * chrome browser which is to be passed as parameter with preferences like -
	 * open browser maximized, disable info-bars, disable notifications about
	 * password save notifications
	 * 
	 * @param browserName
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @author Mohan.jadhav
	 * @CreatedOn -
	 * @LastModified 03-Apr-2017 11:27:38 AM
	 *
	 */
	public void openWebbrowser(String browserName)
			throws FileNotFoundException, IOException {
		configureLog4jPropertiesFile();

		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.default_content_setting_values.notifications", 2);
		prefs.put("credentials_enable_service", false);
		prefs.put("password_manager_enabled", false);

		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		options.addArguments("disable-infobars");
		options.setExperimentalOption("prefs", prefs);

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("geo.enabled", true);
		profile.setPreference("geo.provider.use_corelocation", true);
		profile.setPreference("geo.prompt.testing", true);
		profile.setPreference("geo.prompt.testing.allow", true);

		if (browserName.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					CALLXConstants.CHROME_DRIVER_PATH);
			driver = new ChromeDriver(options);
			debugLogging("Chrome browser is initialized .. ", "Info");
		} else if (browserName.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver",
					CALLXConstants.FIREFOX_DRIVER_PATH);
			// driver = new FirefoxDriver(profile);
			debugLogging("FireFox browser is initialized ..", "Info");
		}
	}

	/**
	 * Go to application web-page based on given property name
	 * 
	 * @param driver
	 * @param urlPropertiesName
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @author Mohan.jadhav
	 * @CreatedOn -
	 * @LastModified 03-Apr-2017 11:37:07 AM
	 * 
	 */
	public void openApplicationWebPage(WebDriver driver,
			String urlPropertiesName) throws FileNotFoundException, IOException {
		driver.get(config.getProperty(urlPropertiesName));
		debugLogging("Opened WebPage: " + config.getProperty(urlPropertiesName)
				+ " -> " + driver.getCurrentUrl(), "Info");
	}

	/**
	 * Configure log4j.xml file for logging functionality
	 * 
	 * @author Mohan.jadhav
	 * @CreatedOn 03-Apr-2017
	 * @LastModified 03-Apr-2017 11:38:25 AM
	 * 
	 */
	public void configureLog4jPropertiesFile() {

		System.setProperty("logfile.name", CALLXConstants.LOG_FILE_PATH);
		PropertyConfigurator.configure(CALLXConstants.LOG4J_PROPERTIES_PATH);
	}

	/**
	 * Load object repository properties file
	 * 
	 * @return Return instance of Properties which is mapped to Object
	 *         repository
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @author Mohan.jadhav
	 * @CreatedOn -
	 * @LastModified 03-Apr-2017 11:40:39 AM
	 * 
	 */
	public Properties loadObjectRepositoryProperties()
			throws FileNotFoundException, IOException {
		Properties locator = new Properties();
		locator.load(new FileInputStream(CALLXConstants.OBJECT_REPOSITORY_PATH));
		return locator;
	}

	/**
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 *             Load Config file
	 */

	public Properties loadConfigFile() throws FileNotFoundException,
			IOException {
		Properties locator = new Properties();
		locator.load(new FileInputStream(CALLXConstants.CONFIG_FILE_PATH));
		return locator;
	}

	/**
	 * Log info/errors on file as well as console
	 * 
	 * @param logMessage
	 * @param infoOrError
	 * @author Mohan.jadhav
	 * @CreatedOn -
	 * @LastModified 03-Apr-2017 11:43:33 AM
	 * 
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

	/**
	 * Take screenshot for driver instance which is passed as parameter File
	 * name for screenshot will be in format of 'HH-mm_millis'.
	 * 
	 * @param driver
	 * @throws IOException
	 * @author Mohan.jadhav
	 * @CreatedOn -
	 * @LastModified 03-Apr-2017 11:46:35 AM
	 * 
	 */
	public void captureScreenShot(WebDriver driver) throws IOException {
		File imageFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		GregorianCalendar gcalendar = new GregorianCalendar();
		String failureImageFileName = CALLXConstants.SCREENSHOTS_DIRECTORY
				+ File.separator
				+ "Screenshots"
				+ File.separator
				+ CALLXConstants.GLOBAL_DATE_FORMAT
						.format(new java.util.Date()) + File.separator
				+ formatInteger(gcalendar.get(Calendar.HOUR)) + "-"
				+ formatInteger(gcalendar.get(Calendar.MINUTE)) + "_"
				+ gcalendar.getTimeInMillis() + ".png";
		File failureImageFile = new File(failureImageFileName);
		FileUtils.moveFile(imageFile, failureImageFile);
		screenshots.add(failureImageFile);
		debugLogging("Something went wrong. Check Screenshot.", "Error");
	}

	/**
	 * Scroll the window with respect to x or y coordinate and an web-element or
	 * scroll to that web-element based on provided element locator.
	 * 
	 * @param driver
	 * @param elementLocator
	 * @param x
	 * @param y
	 * @throws Exception
	 * @author Mohan.jadhav
	 * @CreatedOn -
	 * @LastModified 12-Apr-2017 2:44:27 PM
	 * 
	 */
	public void scrollwindow(WebDriver driver, String elementLocator, int x,
			int y) throws Exception {
		WebElement scrollToThatElement = driver
				.findElement(getLocator(elementLocator));
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		if (y == 0 && x != 0) {
			js.executeScript("window.scrollTo(0,"
					+ (scrollToThatElement.getLocation().x + x) + ")");
			debugLogging("Scrolled to element with x position at: " + x + " "
					+ scrollToThatElement.getText(), "Info");
		} else if (x == 0 && y != 0) {
			js.executeScript("window.scrollTo(0,"
					+ (scrollToThatElement.getLocation().y + y) + ")");
			debugLogging("Scrolled to element with y position at: " + y + " "
					+ scrollToThatElement.getText(), "Info");
		} else {
			js.executeScript("window.scrollTo(0,true)", scrollToThatElement);
			debugLogging(
					"Scrolled to element: " + scrollToThatElement.getText(),
					"Info");
		}

	}

	/**
	 * Switch to another tab on browser which is opened recently (last). If only
	 * one window available, it will take a screenshot for the error.
	 * 
	 * @param driver
	 * @throws IOException
	 * @author Mohan.jadhav
	 * @CreatedOn -
	 * @LastModified 12-Apr-2017 2:47:34 PM
	 * 
	 */
	public void switchToAnotherWindow(WebDriver driver) throws IOException {
		String winHandleBefore = driver.getWindowHandle();
		debugLogging("Current window handle: " + winHandleBefore, "Info");

		if (driver.getWindowHandles().size() > 1) {
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				debugLogging("Switched to new window opened with ID: "
						+ winHandle, "Info");
			}
		} else {
			debugLogging("Only window available is: " + winHandleBefore,
					"Error");
			captureScreenShot(driver);
		}
	}

	/**
	 * @param elementLocator
	 * @param timeOut
	 * @throws Exception
	 * 
	 *             Wait for element to be clickable in page
	 */

	public void waitForElemToBeClickable(WebDriver driver,
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
	 * Take screenshot for driver instance which is passed as parameter File
	 * name for screenshot will be in format of 'HH-mm_millis_fileName' Also,
	 * keeping this file in a list, which is to used to send all screenshots
	 * taken, via mail
	 * 
	 * @param driver
	 * @param fileName
	 * @throws IOException
	 * @author bhumit.shingala
	 * @CreatedOn -
	 * @LastModified 03-Apr-2017 11:51:55 AM
	 * 
	 */
	public void captureScreenShot(WebDriver driver, String fileName)
			throws IOException {
		File imageFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		GregorianCalendar gcalendar = new GregorianCalendar();
		String failureImageFileName = CALLXConstants.SCREENSHOTS_DIRECTORY
				+ File.separator
				+ "Screenshots"
				+ File.separator
				+ CALLXConstants.GLOBAL_DATE_FORMAT
						.format(new java.util.Date()) + File.separator
				+ formatInteger(gcalendar.get(Calendar.HOUR)) + "-"
				+ formatInteger(gcalendar.get(Calendar.MINUTE)) + "_"
				+ gcalendar.getTimeInMillis() + "_" + fileName + ".png";
		File failureImageFile = new File(failureImageFileName);
		FileUtils.moveFile(imageFile, failureImageFile);
		screenshots.add(failureImageFile);
		debugLogging("Something went wrong. Check Screenshot.", "Error");
	}

	/**
	 * @param elementLocator
	 * @param timeOutInSeconds
	 * @throws Exception
	 * 
	 *             Wait for element to be visible in the page
	 */

	public void waitUntilVisibility(WebDriver driver, String elementLocator,
			int timeOutInSeconds) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		long startTime = System.nanoTime();
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(getLocator(elementLocator)));
		long endTime = System.nanoTime();
		debugLogging("Waited: " + ((endTime - (double) startTime) / 1000000000)
				+ " seconds for element: " + elementLocator, "Info");
	}

	public void waitUntilInVisibility(WebDriver driver, String elementLocator,
			int timeOutInSeconds) throws Exception {
		try {
			sleep(250);
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			long startTime = System.nanoTime();
			wait.until(ExpectedConditions
					.invisibilityOfElementLocated(getLocator(elementLocator)));
			long endTime = System.nanoTime();
			debugLogging("Waited: "
					+ ((endTime - (double) startTime) / 1000000000)
					+ " seconds for element: " + elementLocator, "Info");
		} catch (Exception e) {
			Log.info("Loading icon does not appeared...");
		}
	}

	/**
	 * @param elementLocator
	 * @param time
	 * @return
	 * @throws Exception
	 * 
	 *             Fluent wait and polling
	 */

	public String fluentWait(WebDriver driver, String elementLocator, int time)
			throws Exception {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(time, TimeUnit.SECONDS)
				.pollingEvery(3, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(getLocator(elementLocator)));
		return driver.findElement(getLocator(elementLocator)).getText();
	}

	/**
	 * @param elementLocator
	 * @return
	 * @throws Exception
	 * 
	 *             Return locator of particular element
	 */

	public By getLocator(String elementLocator) throws Exception {
		String locator = properties.getProperty(elementLocator);

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

	public void clickElement(WebDriver driver, String elementLocator)
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

	public void fillValue(WebDriver driver, String elementLocator, String value)
			throws Exception {
		WebElement element = driver.findElement(getLocator(elementLocator));
		element.clear();
		element.sendKeys(value);
		debugLogging("Filled value: " + value, "Info");
	}

	/**
	 * @param elementName
	 * @param value
	 * @throws Exception
	 */

	public void selectValueFromDropDown(WebDriver driver, String elementName,
			String value) throws Exception {
		waitUntilVisibility(driver, elementName, 25);
		sleep(450);
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
	 * @throws Exception
	 * 
	 *             Switch to different frame
	 */

	public void switchToFrame(WebDriver driver, String elementLocator)
			throws Exception {
		WebElement element = driver.findElement(getLocator(elementLocator));
		driver.switchTo().frame(element);
		debugLogging("Switched to frame: " + elementLocator, "Info");
	}

	/**
	 * @param elementLocator
	 * @return
	 * @throws Exception
	 * 
	 *             Get text from element and return it as well as print it
	 */

	public String getTextFromElement(WebDriver driver, String elementLocator)
			throws Exception {
		String text = driver.findElement(getLocator(elementLocator)).getText();
		debugLogging(text, "Info");
		return text;
	}

	/**
	 * @throws AWTException
	 * 
	 *             Scroll down with page down key (Robot class)
	 * 
	 */

	public void scrollDownByPageDownKey() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_PAGE_DOWN);
		robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
		debugLogging("Scrolled down the window ..", "Info");
	}

	/**
	 * @throws AWTException
	 * 
	 *             Scroll Up with page down key (Robot class)
	 * 
	 */
	public void scrollUpByPageUpKey() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_PAGE_UP);
		robot.keyRelease(KeyEvent.VK_PAGE_UP);
		debugLogging("Scrolled up the window ..", "Info");
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
	 * @param driver
	 * @param elementLocator
	 * @param option
	 * @throws Exception
	 */
	public void selectOptionFromDropDownByPartialText(WebDriver driver,
			String elementLocator, String option) throws Exception {
		List<WebElement> allOptions = driver.findElement(
				getLocator(elementLocator)).findElements(By.tagName("option"));
		debugLogging(
				"No of options in current drop down: " + allOptions.size(),
				"Info");
		List<String> all = new ArrayList<String>();
		boolean bool = false;

		if (allOptions.size() > 0) {
			for (int i = 0; i < allOptions.size(); i++) {
				all.add(allOptions.get(i).getText());
				if (allOptions.get(i).getText().contains(option)) {
					bool = true;
					selectValueFromDropDown(driver, elementLocator, allOptions
							.get(i).getText());
					break;
				}
			}

			if (!bool) {
				debugLogging(
						"Some error occurred while selecting option from drop down ..",
						"Error");
				debugLogging(all.toString(), "Info");
			}
		} else {
			debugLogging("No options found in drop down ..", "Error");
			captureScreenShot(driver);
		}
	}

	/**
	 * @param driver
	 * @param labelName
	 * @throws IOException
	 * 
	 *             Click on label based on it's name
	 */
	public void clickOnLabel(WebDriver driver, String labelName)
			throws IOException {
		List<WebElement> li = driver.findElements(By.tagName("label"));
		debugLogging("Numbers of label elements: " + li.size(), "Info");

		boolean bol = false;
		if (li.size() > 0) {
			for (int i = 0; i < li.size(); i++) {
				if (li.get(i).getText().contains(labelName)) {
					debugLogging("Found element with name: "
							+ li.get(i).getText(), "Info");
					li.get(i).click();
					bol = true;
					break;
				}
			}
			if (!bol) {
				debugLogging("No labels found with name: " + labelName, "Error");
				captureScreenShot(driver);
			}

		} else {
			debugLogging("No label elements on current screen", "Error");
		}
	}

	/**
	 * @param input
	 * @return Returns formatted integer with two decimal places
	 * 
	 *         Format integer for two decimal places
	 */
	public String formatInteger(int input) {
		return String.format("%02d", input);
	}

	/**
	 * Get environment
	 * 
	 * @return
	 */
	public String getTestEnvironment() {
		String environment;

		if (System.getProperty("testEnv") == null
				|| System.getProperty("testEnv").trim().isEmpty()) {
			environment = config.getProperty("Environment");
			debugLogging(
					"System property not found for testEnv, continuing execution with OR property value "
							+ environment, "Info");
		} else {
			environment = System.getProperty("testEnv");
			debugLogging("Found system property for testEnv: " + environment,
					"Info");
		}

		return environment;
	}

	/**
	 * Get the test suite
	 * 
	 * @return
	 */
	public String getTestSuite() {
		String suite;

		if (System.getProperty("suite") == null
				|| System.getProperty("suite").trim().isEmpty()) {
			suite = config.getProperty("SuiteName");
			debugLogging(
					"System property not found for suite, continuing execution with OR property value "
							+ suite, "Info");
		} else {
			suite = System.getProperty("suite");
			debugLogging("Found system property for suite: " + suite, "Info");
		}

		return suite;
	}

	/**
	 * Get browser
	 * 
	 * @return
	 */
	public String getBrowserName() {
		String browser;

		if (System.getProperty("browser") == null
				|| System.getProperty("browser").trim().isEmpty()) {
			browser = config.getProperty("Browser");
			debugLogging(
					"System property not found for browser, continuing execution with OR property value "
							+ browser, "Info");
		} else {
			browser = System.getProperty("browser");
			debugLogging("Found system property for browser: " + browser,
					"Info");
		}

		return browser;
	}

	/**
	 * 
	 * @param webelement
	 * @param scrollPoints
	 * @return
	 * @author mohan.jadhav
	 * @CreatedOn May 25, 2017
	 * @LastModified May 25, 2017 1:01:35 PM
	 */
	public void scrollPage(WebDriver driver, String webelement, int scrollPoints) {
		try {
			Actions dragger = new Actions(driver);
			int numberOfPixelsToDragTheScrollbarDown = 10;
			for (int i = 10; i < scrollPoints; i = i
					+ numberOfPixelsToDragTheScrollbarDown) {
				dragger.moveToElement(
						driver.findElement(getLocator(webelement)))
						.clickAndHold()
						.moveByOffset(0, numberOfPixelsToDragTheScrollbarDown)
						.release(driver.findElement(getLocator(webelement)))
						.build().perform();
			}
			Thread.sleep(500);

		} catch (Exception e) {
			e.printStackTrace();
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
