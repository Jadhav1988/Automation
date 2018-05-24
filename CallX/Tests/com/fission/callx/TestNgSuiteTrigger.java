/**
 * 
 */
package com.fission.callx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.collections.Lists;

import com.fission.callx.utilis.CommonSettings;

/**
 * @author mohan.jadhav
 *
 */
public class TestNgSuiteTrigger extends CommonSettings {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException
	 **/

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws FileNotFoundException, IOException {
		CommonSettings set = new CommonSettings();
		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();
		suites.add((System.getProperty("user.dir")+File.separator+set.config.getProperty("SuiteName")+".xml"));
		testng.setTestSuites(suites);
		testng.addListener(tla);
		set.configureLog4jPropertiesFile();
		testng.run();
	}


}
