package com;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class BaseTest {
    /**
     * @throws MalformedURLException
     * @throws URISyntaxException
     */

    public AndroidDriver driver;
    public AppiumDriverLocalService service;

    @Test
    @BeforeClass
    public void initialiseAndroidDriverWithAppium() throws MalformedURLException, URISyntaxException{
        File appium_js_path = new File("//home//machoalpha//.nvm//versions//node//v21.4.0//lib//node_modules//appium//build//lib//main.js");
        String appium_IP_adress = "127.0.0.1";
        int appium_port = 4723;
        
        service = new AppiumServiceBuilder()
            .withAppiumJS(appium_js_path)
            .withIPAddress(appium_IP_adress)
            .usingPort(appium_port)
            .build();
        service.start();
        
        String deviceName = "SamsungS8";
        String path_to_APK = "//home//machoalpha//Documents//APKs//com.homewhiz.global-automation-v2.9.50-231207.apk";
        
        UiAutomator2Options capabilities = new UiAutomator2Options();
        capabilities.setDeviceName(deviceName);
        capabilities.setApp(path_to_APK);

        URI uri = new URI("http://127.0.0.1:4723");
        URL url = uri.toURL();

        driver = new AndroidDriver(url, (Capabilities) capabilities);
    }
}
