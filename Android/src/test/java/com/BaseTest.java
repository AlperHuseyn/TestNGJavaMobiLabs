package com;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class BaseTest extends ConfigReader{
    ConfigReader config = new ConfigReader();

    public AndroidDriver driver;
    public AppiumDriverLocalService service;

     /**
     * Initializes the AndroidDriver with Appium.
     * 
     * This method sets up the Appium service and the Android driver with the
     * required capabilities. It reads configuration properties such as the
     * path to Appium JavaScript file, device name, APK path, and Appium server URL.
     *
     * @throws MalformedURLException if the constructed URL is not formatted correctly.
     * @throws URISyntaxException    if the URI syntax is incorrect.
     */
    @Test
    @BeforeClass
    public void initialiseAndroidDriverWithAppium() throws MalformedURLException, URISyntaxException{
        try{
            setupAppiumService();
            setupAndroidDriver();
        } catch (MalformedURLException | URISyntaxException err) {
            System.err.println("Error initializing Appium service or Android driver: " + err.getMessage());
            throw err;
        }
    }

    /**
     * Sets up and starts the Appium service.
     *
     * @throws MalformedURLException if the Appium JavaScript file path is incorrect.
     */
    private void setupAppiumService(){
        String appiumJsPathString = config.getProperty("appiumJsPath");
        File appiumJsPath = new File(appiumJsPathString);
        String appiumIpAddress = "127.0.0.1";
        int appiumPort = 4723;

        service = new AppiumServiceBuilder()
                .withAppiumJS(appiumJsPath)
                .withIPAddress(appiumIpAddress)
                .usingPort(appiumPort)
                .build();
        service.start();
    }

     /**
     * Configures and creates the AndroidDriver instance.
     *
     * @throws MalformedURLException if the URL for the Appium server is malformed.
     * @throws URISyntaxException    if the URI syntax for the Appium server is incorrect.
     */
    private void setupAndroidDriver() throws URISyntaxException, MalformedURLException{
        String deviceName = config.getProperty("deviceName");
        String path2APK = config.getProperty("path2APK");
        
        UiAutomator2Options capabilities = new UiAutomator2Options();
        capabilities.setDeviceName(deviceName);
        capabilities.setApp(path2APK);

        String uriString = config.getProperty("appiumServerUrl");
        URI uri = new URI(uriString);
        URL url = uri.toURL();

        driver = new AndroidDriver(url, (Capabilities) capabilities);
    }
    /**
     * Executes a long click gesture on the element using the JavascriptExecutor interface.
     */
    public void longPressAction(WebElement element){
        ((JavascriptExecutor) driver).executeScript("mobile: longClickGesture", ImmutableMap.of(
            "elementId", ((RemoteWebElement) element).getId(),
            "duration", 2000
        ));
    }

    /**
     * Scrolls to an element in the Android app that contains the specified text
     * using the Appium's AndroidUIAutomator's `UiScrollable` class.
     * 
     * @param text The text of the element to scroll to. This is the visible text associated 
     *             with a UI element in the app.
     */
    public void scrollTextIntoViewAction(@NonNull String text){
        driver.findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector()).scrollTextIntoView(new UiSelector().text('" + text + "'));"));
    }

    /**
     * Tears down the test environment.
     * 
     * Waits for 5 seconds to complete any remaining operations, then quits the driver 
     * and stops the Appium service. This ensures that the application and the server 
     * are properly shut down after tests.
     */
    @Test
    @AfterClass
    public void tearDown() throws InterruptedException{
        Thread.sleep(5000);
        driver.quit();
        service.stop();
    }
}