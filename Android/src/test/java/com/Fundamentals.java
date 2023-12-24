package com;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;

public class Fundamentals extends BaseTest{
    ConfigReader config = new ConfigReader();

    /**
     * Test method to log into the application.
     * Configuration parameters are retrieved from a properties file.
     */
    @Test(priority = 1)
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
            System.out.println("Attempting to find the element.");
            driver.findElement(AppiumBy.id(config.getProperty("applianceNameLocator")));
            return true;  // Element is found, return true
        } catch (NoSuchElementException err) {
            System.out.println("Element not found." + err.getMessage());
            return false;  // Element not found, return false
        }
    }

    /**
     * Removes a device by performing a long click gesture.
     */
    private void removeDevice(){
        // Locate the element
        WebElement element = driver.findElement(AppiumBy.id(config.getProperty("applianceNameLocator")));
        // Execute long press
        longPressAction(element);
        
        // ...
    }

    /**
     * Adds a device to the account.
     */
    @Test(priority = 2, dependsOnMethods = "loginApp")
    public void addDevice(){
        if(isDeviceAdded()){
            removeDevice();
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait until "No Device Added" message is visiable
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(config.getProperty("noDeviceAddedLocator"))));
        // Click add dvice button
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.className(config.getProperty("addDeviceButtonLocator")))).click();
        
        // Check if permission message is displayed and if so, click the "Allow" button
        if (!(driver.findElement(AppiumBy.id("permissionMessageLocator"))).isDisplayed()){
           wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("allowButtonLocator"))))
                .click();
        }

        // Wait until the "Choose the Way" message is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(config.getProperty("chooseTheWayLocator"))));
        // Select "Kitchen Devices" and then "Fridge" from the available options
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("kitchenDevicesLocator"))))
            .click();
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("fridgeLocator"))))
            .click();
        
        // Wait until the "Appliance Stock Number" text field is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(config.getProperty("applianceStockNumberTxtLocator"))));
        // Enter the SKU number into the "SKU Pin Entry
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("skuPinEntryLocator"))))
            .sendKeys(config.getProperty("SKU"));
    }
}
