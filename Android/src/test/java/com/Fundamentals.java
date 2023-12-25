package com;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
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
        // Ensure element is visible before interaction.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try{
            System.out.println("Attempting to find the element.");
            wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(config.getProperty("applianceNameLocator"))));
            System.out.println("Device element found.");
            return true;  // Element is found, return true
        } catch (TimeoutException err) {
            System.out.println("Device element not found." + "\n" + err.getMessage());
            return false;  // Element not found, return false
        }
    }

    /**
     * Removes a device by performing a long click gesture.
     */
    private void removeDevice(){
        // Ensure elements are visible and clickable before interaction.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        // Locate the element
        WebElement element = driver.findElement(AppiumBy.id(config.getProperty("applianceNameLocator")));
        // Execute long press
        longPressAction(element);

        // After long press, re-fetch the remove button to avoid stale reference
        WebElement removeButton = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.id(config.getProperty("removeDeviceButtonLocator"))));

        // Log the state of the remove button
        boolean isRemoveButtonDisplayed = removeButton.isDisplayed();
        boolean isRemoveButtonEnabled = removeButton.isEnabled();

        System.out.println("Remove button is displayed: " + isRemoveButtonDisplayed);
        System.out.println("Remove button is enabled: " + isRemoveButtonEnabled);

        wait.until(ExpectedConditions.elementToBeClickable(removeButton))
            .click();

        // Handle confirmation pop-up
        WebElement deleteConfirmationButton = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.id(config.getProperty("deleteConfirmationButtonLocator"))));

        // Log the state of the delete confirmation button
        boolean isDeleteConfirmationButtonDisplayed = deleteConfirmationButton.isDisplayed();
        boolean isDeleteConfirmationButtonEnabled = deleteConfirmationButton.isEnabled();

        System.out.println("Delete confirmation button is displayed: " + isDeleteConfirmationButtonDisplayed);
        System.out.println("Delete confirmation button is enabled: " + isDeleteConfirmationButtonEnabled);

        wait.until(ExpectedConditions.elementToBeClickable(deleteConfirmationButton))
            .click();
    }

    /**
     * Adds a device to the user's account.
     * This method first checks if a device is already added using the isDeviceAdded method.
     * If a device is found, it removes the existing device to ensure not to add same    device.
     * The method then performs a series of steps to add a new device, handling potential UI changes and pop-ups.
     */
    @Test(priority = 2, dependsOnMethods = "loginApp")
    public void addDevice(){
        // Check if a device is already added. If so, remove it.
        if(isDeviceAdded()){
            removeDevice();
        }

        // Ensure elements are visible and clickable before interaction.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait until "No Device Added" message is visiable
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(config.getProperty("noDeviceAddedLocator"))));
        // Initiate the process of adding a new device by clicking the "Add Device" button.
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.className(config.getProperty("addDeviceButtonLocator")))).click();
        
        // Handle potential pop-ups
        wait.until(ExpectedConditions.invisibilityOfElementWithText(AppiumBy.id("noDeviceAddedLocator"), "Henüz cihaz eklemediniz."));
        boolean popUp = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("allowButtonLocator")))).isDisplayed();
        if (popUp){
           // If a pop-up is present, click the "Allow" button to proceed.
           wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("allowButtonLocator"))))
                .click();
        }

        // Proceed with the device addition process by selecting the type of device.
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(config.getProperty("chooseTheWayLocator"))));
        // Select "Kitchen Devices" and then "Fridge" from the available options
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("kitchenDevicesLocator"))))
            .click();
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("fridgeLocator"))))
            .click();
        
        // Enter the SKU number into the "SKU Pin Entry, then submit the form to proceed
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(config.getProperty("applianceStockNumberTxtLocator"))));
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("skuPinEntryLocator"))))
            .sendKeys(config.getProperty("SKU"));
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("useSkuPinButtonLocator"))))
            .click();

        // Handle "instructions for onboarding" pop-up
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("proceedButtonLocator"))))
            .click();
        
        // Assertion: Check if the "Wireless" button text is displayed, indicating proceeding next page successfully
        Assert.assertTrue(driver.findElement(AppiumBy.id(config.getProperty("wirelessBottonTxtLocator"))).isDisplayed());
        // Continue with the next steps
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("nextButtonLocator"))))
            .click();
    }
}
