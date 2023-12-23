package com;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.AppiumBy;

public class Fundamentals extends BaseTest{
    ConfigReader config = new ConfigReader();

    /**
     * Test method to log into the application.
     * Configuration parameters are retrieved from a properties file.
     */
    @Test
    public void loginApp(){
        // Ensure elements are visible and clickable before interaction.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Input email
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(config.getProperty("emailLocator"))))
            .sendKeys(config.getProperty("EMAIL"));

        // Input password
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(config.getProperty("passwordLocator"))))
            .sendKeys(config.getProperty("PASSWORD"));
        
        // Submit login form
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("loginButtonLocator"))))
            .click();
    }

    /**
     * Checks if an element is present on the web page.
     * 
     * @return true if the element is present, false otherwise.
     */
    private boolean isDeviceAdded(){
        try{
            driver.findElement(AppiumBy.id(config.getProperty("applianceNameLocator")));
            return true;  // Element is found, return true
        } catch (NoSuchElementException err) {
            return false;  // Element not found, return false
        }
    }

    /**
     * Removes a device by performing a long click gesture.
     */
    private void removeDevice(){
        // Locate the element
        WebElement element = driver.findElement(AppiumBy.id(config.getProperty("applianceNameLocator")));  // HERE
        // Execute a long click gesture on the element using the JavascriptExecutor interface.
        ((JavascriptExecutor) driver).executeScript("mobile: longClickGesture", ImmutableMap.of(
            "elementId", ((RemoteWebElement) element).getId(),
            "duration", 2000
        ));
        // ...
    }

    @Test
    public void addDevice(){
        if(isDeviceAdded()){
            removeDevice();
        }

        // ...
    }
}
