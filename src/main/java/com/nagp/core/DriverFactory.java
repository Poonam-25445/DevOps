package com.nagp.core;

import java.time.Duration;
import java.util.HashMap;

import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ThreadGuard;

import com.nagp.logs.LoggingManager;
import com.nagp.util.CommonUtil;
/**
 * This class is responsible for creating the WebDriver instance and opening the browser with default URL and required set up.
 *
 */
public class DriverFactory {

	private static final String POPUP_ENABLE_COMMAND = "REG ADD \"HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\New Windows\" /F /V \"PopupMgr\" /T REG_SZ /D \"no\"";
	public static final String CHROME = "chrome";
	public static final String FIREFOX = "firefox";
	public static final String INTERNET_EXPLORER = "internet explorer";
	private final ThreadLocal<WebDriver> driver;// thread local driver object for webdriver

	/**
	 * 
	 * Helper class to create singleton instance of DriverFactory
	 *
	 */
	private static class LazyHolder {
		private static final DriverFactory INSTANCE = new DriverFactory();
	}

	/**
	 * Creates singleton instance of DriverFactory
	 */
	public static DriverFactory getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * Private constructor to avoid instantiation from outside
	 */
	private DriverFactory() {
		driver = new ThreadLocal<WebDriver>();
	}

	/**
	 * This method will do the initial browser set up and open the default URL in
	 * case the execution is done without using Selenium grid
	 * 
	 * @param driverOptions			provided by TestBase
	 * 
	 * @throws FrameworkException	User defined exception to handle driver issues
	 */
	public void setDriver(DriverOptions driverOptions) throws FrameworkException {
		try {
			LoggingManager.getConsoleLogger().info(" : setDriver Method Called");
			DesiredCapabilities capability = new DesiredCapabilities();
			System.setProperty("webdriver.ie.driver", Config.IEDriverPath);

			switch (driverOptions.browser.toLowerCase()) {

				case DriverFactory.CHROME:
					capability.setBrowserName(CHROME);
					ChromeOptions option = DriverFactory.setChromeCapabilities(capability,driverOptions.platformType);
					driver.set(new ChromeDriver(option));
					break;
				case DriverFactory.FIREFOX:
					setFirefoxCapabilities(capability);
					break;
				case DriverFactory.INTERNET_EXPLORER:
					setIECapabilities();
					break;
				default:
					throw new FrameworkException(
							"Invalid browser option chosen for local execution : " + driverOptions.browser);
			}
			ThreadGuard.protect(getDriver());
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			getDriver().manage().timeouts().getPageLoadTimeout();
			getDriver().manage().window().maximize();
			getDriver().get(driverOptions.url);
			LoggingManager.getConsoleLogger().info(" : Application Opened : Browser Open Request Sent to -> "+ "http://" + driverOptions.webdriverHost + ":" + driverOptions.webdriverPort + "/wd/hub");
			LoggingManager.getConsoleLogger().info(" : Webdriver fingerprint : " + getDriver().hashCode());
		} catch (Exception e) {
			LoggingManager.getConsoleLogger().error(e.getMessage());
			throw new FrameworkException(e.getMessage());
		}
	}

	/**
	 * Sets the desired capabilities of firefox.
	 */
	private void setFirefoxCapabilities(DesiredCapabilities capability) {
		capability.setBrowserName(FIREFOX);
		capability.setCapability("marionatte", true);
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.merge(capability);
		// Creating firefox profile
		FirefoxProfile profile = new FirefoxProfile();
		// Instructing firefox to use custom download location
		profile.setPreference("browser.download.folderList", 2);
		// Setting custom download directory
		profile.setPreference("browser.download.dir", Config.ExportFilePath);
		// Skipping Save As dialog box for types of files with their MIME
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
		profile.setPreference("fission.webContentIsolationStrategy", 0);
		profile.setPreference("fission.bfcacheInParent", false);
		// Creating FirefoxOptions to set profile
		firefoxOptions.setProfile(profile);

		driver.set(new FirefoxDriver(firefoxOptions));
	}

	/**
	 * Sets the desired capabilities of Internet explorer.
	 */
	public void setIECapabilities() {
		InternetExplorerOptions ieOptions = new InternetExplorerOptions();
		ieOptions.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		ieOptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
		ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		ieOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, false);
		ieOptions.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
		ieOptions.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, UnexpectedAlertBehaviour.ACCEPT);
		ieOptions.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
		ieOptions.setCapability("browserstack.ie.enablePopups", "true");
		
		try {
			Runtime.getRuntime().exec(POPUP_ENABLE_COMMAND);
		} catch (Exception e) {
			LoggingManager.getConsoleLogger().info("Error ocured!");
		}
		driver.set(new InternetExplorerDriver(ieOptions));
	}

	/**
	 * Sets the desired capabilities of chrome to download any file at specified
	 * path.
	 */
	private static ChromeOptions setChromeCapabilities(DesiredCapabilities cap, String platform) {
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", CommonUtil.getFullPathBasedOnPlatformType(Config.ExportFilePath));

		ChromeOptions options = new ChromeOptions();
		HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("--test-type");
		options.addArguments("--disable-extensions"); // to disable browser extension pop-up
		if(platform.equalsIgnoreCase("LINUX")) {
			options.addArguments("–headless");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
		}
		options.addArguments("--disable-backgrounding-occluded-windows");
		options.addArguments("--remote-allow-origins=*");
		cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
		cap.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		return options;
	}

	/**
	 * Sets the desired capabilities of specified browser.
	 */
	public static DesiredCapabilities setBrowser(String browser) {

		DesiredCapabilities capabilities = new DesiredCapabilities();
		if (browser.equalsIgnoreCase("firefox")) {
			capabilities.setBrowserName(FIREFOX);
		} else if (browser.equalsIgnoreCase("chrome")) {
			capabilities.setBrowserName(CHROME);;
		} else if (browser.equalsIgnoreCase("internet explorer")) {
			capabilities.setBrowserName(INTERNET_EXPLORER);
		} else if (browser.equalsIgnoreCase("android")) {
			LoggingManager.getConsoleLogger().info("[Debug] Android.....");
			capabilities.setPlatform(Platform.ANDROID);
		} else if (browser.equalsIgnoreCase("ipad")) {
			capabilities.setPlatform(Platform.IOS);
		} else if (browser.equalsIgnoreCase("iphone")) {
			capabilities.setPlatform(Platform.IOS);
		}  
		return capabilities;
	}

	/**
	 * Sets the platform on which user wants to run the test cases.
	 */
	public static Platform setPlatform(String platform) {

		if (platform.equalsIgnoreCase("LINUX")) {
			return Platform.LINUX;
		} else if (platform.equalsIgnoreCase("WINDOWS")) {
			return Platform.WINDOWS;
		} else if (platform.equalsIgnoreCase("MAC")) {
			return Platform.MAC;
		} else if (platform.equalsIgnoreCase("ANDROID")) {
			return Platform.ANDROID;
		} else if (platform.equalsIgnoreCase("WIN8")) {
			return Platform.WIN8;
		} else if (platform.equalsIgnoreCase("XP")) {
			return Platform.XP;
		} else if (platform.equalsIgnoreCase("VISTA")) {
			return Platform.VISTA;
		} else if (platform.equalsIgnoreCase("UNIX")) {
			return Platform.UNIX;
		}
		return Platform.ANY;
	}

	/**
	 * Static method to return the current webdriver instance
	 */
	public static WebDriver getDriver() {
		return getInstance().driver.get();
	}

	/**
	 * This method will close the current browser session, quit the driver and
	 * remove the instance from the ThreadLocal
	 */
	public void removeDriver() {
		try	{
			/* Close All Open Browsers */
			getDriver().quit();
			LoggingManager.getConsoleLogger().info(" : Application Closed : Browser Closed");

			/* Remove current webdriver session */
			getInstance().driver.remove(); 
			LoggingManager.getConsoleLogger().info(" : Webdriver session removed");	
		}
		catch(Exception e){
			LoggingManager.getConsoleLogger().error("Exception while removing webdriver - " + e.getMessage());
		}
	}
}