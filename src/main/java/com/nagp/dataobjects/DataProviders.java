package com.nagp.dataobjects;

import java.lang.reflect.Method;


import org.testng.annotations.DataProvider;

import com.nagp.core.Config;
import com.nagp.util.ReadFileUtil;

/**
 * This DataProvider class is used in order to create data-driven tests.It will run the same test case, but with different data sets.
 */
public class DataProviders {

	/** The use of the comma character (,) typically separates each field of text.
	 */
	private static String separator = ",";
	
	/**
	 * Get the data set required to test all the test cases.
	 * 
	 * @return Object[][], return data from the specified file.
	 */
	@DataProvider(name="TestData")
	public static Object[][] getTestData(Method method){
		String [][] dataSet = ReadFileUtil.CSVDataProvider(method.getName(), separator ,Config.dataInputFile);
		return dataSet;
	}
}