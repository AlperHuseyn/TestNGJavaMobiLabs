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

public class Onboarding extends BaseTest{
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
     * Checks if an element is present on the account.
     * 
     * @return true if the element is present, false otherwise.
     */
    private boolean isDeviceAdded(){
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
        
        // After clicking the delete confirmation, assert that the device list is empty
        WebElement toolbarHeaderTxt = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.id(config.getProperty("toolbarHeaderTxtLocator"))));
        System.out.println("Header text displayed: " + toolbarHeaderTxt.isDisplayed());
        Assert.assertEquals(toolbarHeaderTxt.getText(), "Cihazlar");
    }

    /**
     * Begins the device addition process by clicking add device
     * 
     */
    private void beginAddDeviceFlow(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait until "No Device Added" message is visiable
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(config.getProperty("noDeviceAddedLocator"))));
        // Initiate the process of adding a new device by clicking the "Add Device" button.
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.className(config.getProperty("addDeviceButtonLocator")))).click();
    }

    /**
     * This method handles the access the device's location request pop-up 
     * that may appear during the device addition process.
     * 
     */
    private void handlePermissionPopUp(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait for any transitional elements to disappear to ensure we are dealing with the current UI state.
        wait.until(ExpectedConditions.invisibilityOfElementWithText(By.xpath("noDeviceAddedLocator"), "Henüz cihaz eklemediniz."));

        // Check if the permission pop-up is displayed.
        boolean popUp = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("allowButtonLocator")))).isDisplayed();
        if (popUp){
           // If the pop-up is present, click the "Allow" button to grant permission and proceed.
           wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("allowButtonLocator"))))
                .click();
        }
    }

    /**
     * Navigates through the device selection UI
     * 
     */
    private void completeDeviceSelectionProcess(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Proceed with the device addition process by selecting the type of device.
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(config.getProperty("chooseHowYouProceedTxtLocator"))));
        // Select "Kitchen Devices" and then "Fridge" from the available options
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("kitchenDevicesLocator"))))
            .click();
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("fridgeLocator"))))
            .click();
    }

    /**
     * Enters the SKU number into the relevant input field and submits it to
     * continue with the device addition process.
     * 
     */
    private void enterAndSubmitSKUNumber(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Enter the SKU number into the "SKU Pin Entry, then submit the form to proceed
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(config.getProperty("applianceStockNumberTxtLocator"))));
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("skuPinEntryLocator"))))
            .sendKeys(config.getProperty("SKU"));
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("useSkuPinButtonLocator"))))
            .click();
    }

    /**
     * Acknowledges the connection instructions provided in a pop-up
     */
    private void acknowledgeConnectionInstructions(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Click the "Understood" or equivalent button to acknowledge the instructions.
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("proceedButtonLocator"))))
            .click();
    }

    /**
     * Validates that the device has been put into the correct setup mode as indicated by
     * the instruction on the screen.
     * 
     */
    private void initiateConnectionSetup(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Validate that the "Wireless button" is displayed, indicating readiness to connect
        Assert.assertTrue(driver.findElement(AppiumBy.id(config.getProperty("wirelessBottonTxtLocator"))).isDisplayed());
        // Click the "Next" button after the user has followed the instructions to put the device in setup mode.
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(config.getProperty("nextButtonLocator"))))
            .click();
    }

    /**
     * Adds a device to the user's account.
     * This method first checks if a device is already added using the isDeviceAdded method.
     * If a device is found, it removes the existing device to ensure not to add same device.
     * The method then performs a series of steps to add a new device, handling potential UI changes and pop-ups.
     */
    @Test(priority = 2, dependsOnMethods = "loginApp")
    public void addDevice(){
        // Check if a device is already added. If so, remove it.
        if(isDeviceAdded()){
            removeDevice();
        }

        // Begin the process of adding a new device
        beginAddDeviceFlow();
        
        // Handle the access the device's location request pop-up
        handlePermissionPopUp();

        // Continue with device type selection
        completeDeviceSelectionProcess();
        
        // Enter and submit the SKU number for the device
        enterAndSubmitSKUNumber();

        // Acknowledge the connection instructions provided by the app
        acknowledgeConnectionInstructions();
        
        // Finalize the setup by initiating the connection process
        initiateConnectionSetup();
    }
}
