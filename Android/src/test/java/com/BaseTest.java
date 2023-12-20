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

public class BaseTest extends ConfigReader{
    /**
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    ConfigReader config = new ConfigReader();

    public AndroidDriver driver;
    public AppiumDriverLocalService service;

    @Test
    @BeforeClass
    public void initialiseAndroidDriverWithAppium() throws MalformedURLException, URISyntaxException{
        String appium_js_path_string = config.getProperty("appium_js_path");
        File appium_js_path = new File(appium_js_path_string);
        String appium_IP_adress = config.getProperty("appium_ip_address");
        int appium_port = 4723;
        
        service = new AppiumServiceBuilder()
            .withAppiumJS(appium_js_path)
            .withIPAddress(appium_IP_adress)
            .usingPort(appium_port)
            .build();
        service.start();
        
        String deviceName = config.getProperty("device_name");
        String path_to_APK = config.getProperty("path_to_apk");
        
        UiAutomator2Options capabilities = new UiAutomator2Options();
        capabilities.setDeviceName(deviceName);
        capabilities.setApp(path_to_APK);

        String uri_string = config.getProperty("appium_server_url");
        URI uri = new URI(uri_string);
        URL url = uri.toURL();

        driver = new AndroidDriver(url, (Capabilities) capabilities);
    }
}