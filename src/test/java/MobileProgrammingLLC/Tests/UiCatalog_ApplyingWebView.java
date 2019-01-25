package MobileProgrammingLLC.Tests;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import MobileProgrammingLLC.PageLibraries.Amazon_MobileAccountPage;
import MobileProgrammingLLC.PageLibraries.Amazon_MobileHomePage;
import MobileProgrammingLLC.PageLibraries.UiCatalog_HomePage;
import MobileProgrammingLLC.PageLibraries.UiCatalog_WebViewPage;
import MobileProgrammingLLC.Resources.Base;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

public class UiCatalog_ApplyingWebView {
	IOSDriver<IOSElement> driver;
	Logger log = LogManager.getLogger(UiCatalog_ApplyingWebView.class.getName());
	Properties locators = new Properties();
	Properties config = new Properties();
	Properties data = new Properties();
	UiCatalog_HomePage uhp;
	UiCatalog_WebViewPage uwvp;
	Amazon_MobileHomePage mhp;
	Amazon_MobileAccountPage map;
	Base b = new Base();
	
	@BeforeClass
	public void initConfigs() {
		log.info("Initializing Configurations...");
		locators = b.loadLocators();
		config = b.loadConfig();
		data = b.loadData();
		driver = b.createAppiumServerConnection(config.getProperty("ip"),config.getProperty("port"), config.getProperty("DeviceType"));
		uhp = new UiCatalog_HomePage(driver);
		uwvp = new UiCatalog_WebViewPage(driver);
		mhp = new Amazon_MobileHomePage(driver);
		map = new Amazon_MobileAccountPage(driver);
		log.info("Configurations Successfully Initialized.");
	}
	
	@Test
	public void openInAppBrowser() {
		b.swipeInDirection("up", driver);
		b.tapOn(uhp.getWebViewLnk(), driver);
		//b.waitForSometime();
		b.tapOn(uwvp.getUrlTF(), driver);
		//b.waitForSometime();
		b.tapOn(uwvp.getClearTextIcon(), driver);
		//b.waitForSometime();
		b.enterContentIntoAndSubmit(uwvp.getBlankUrlTF(), data.getProperty("InAppURL"));
		b.switchContext(driver, "web");
		b.waitFor(10);	
	}
	
	@Test(dependsOnMethods= {"openInAppBrowser"})
	public void navigateToSignInPage() {
		b.flash(mhp.gethamburgMenuLink());
		b.clickOn(mhp.gethamburgMenuLink());
		b.scrollPage(driver);
		b.waitForSometime();
		b.flash(mhp.getsignInlink());
		b.clickOn(mhp.getsignInlink());
	}
	
	@Test(dependsOnMethods= {"navigateToSignInPage"})
	public void verifySignInErrorMsg() {
		b.flash(map.getUserNameTF());
		b.enterContentInto(map.getUserNameTF(), data.getProperty("UserName"));
		b.waitForSometime();
		b.flash(map.getContinueBtn());
		b.clickOn(map.getContinueBtn());
		b.flash(map.geterrorMsg());
		b.compareContent(map.geterrorMsg(), data.getProperty("ErrorMsg"));
	}
	
	@AfterClass
	public void tearDown() {
		b.quitDriver(driver);
		b.stopAppiumServer();
	}
}
