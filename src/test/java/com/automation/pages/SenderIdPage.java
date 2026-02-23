package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;

public class SenderIdPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public SenderIdPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // =========================
    // Locators
    // =========================

    private By createButton =
            By.xpath("//a[contains(@href,'/channels/sms/senderid/create')]");
    private By senderIdInput = By.id("sender_id");
    private By countryDropdown = By.id("country_code");
    private By typeDropdown = By.id("type");
    private By entityIdInput = By.id("entity_id");
    private By submitButton = By.xpath("//button[@type='submit' and contains(.,'Save Sender ID')]");

    private By successToast = By.xpath("//*[contains(text(),'success') or contains(@class,'toast')]");

    // =========================
    // Actions
    // =========================

    public void enterSenderId(String senderId) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(senderIdInput));
        input.clear();
        input.sendKeys(senderId);
    }

    public void selectCountryIndia() {
        Select select = new Select(wait.until(ExpectedConditions.elementToBeClickable(countryDropdown)));
        select.selectByValue("IN");   // India value = IN
    }

    public void selectTypeTransactional() {
        Select select = new Select(wait.until(ExpectedConditions.elementToBeClickable(typeDropdown)));
        select.selectByVisibleText("Transactional");
    }

    public void enterEntityId(String entityId) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(entityIdInput));
        input.clear();
        input.sendKeys(entityId);
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    public void clickCreateNewSenderId() {
        wait.until(ExpectedConditions.elementToBeClickable(createButton)).click();

        // wait until form loads
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sender_id")));
    }

    // =========================
// Utilities
// =========================

    // Generate sender ID based on type
    public String generateSenderIdByType(String type) {

        if (type.equalsIgnoreCase("Promotional")) {
            return generateNumericSenderId();
        } else {
            return generateAlphabeticSenderId();
        }
    }

    // Promotional → numeric only
    private String generateNumericSenderId() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // Transactional / OTP → alphabet only
    private String generateAlphabeticSenderId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }



    // =========================
    // Combined Flow Method
    // =========================

    public String createSenderId(String type) {

        clickCreateNewSenderId();

        String senderId = generateSenderIdByType(type);
        String entityId = generateRandomEntityId();

        enterSenderId(senderId);
        selectCountryIndia();

        if (type.equalsIgnoreCase("Transactional")) {
            selectTypeTransactional();
        } else if (type.equalsIgnoreCase("Promotional")) {
            selectTypePromotional();
        } else if (type.equalsIgnoreCase("OTP")) {
            selectTypeOTP();
        }

        enterEntityId(entityId);
        clickSubmit();

        // Wait for redirect to listing page
        wait.until(ExpectedConditions.urlContains("/channels/sms/senderid"));

        return senderId;
    }

    public boolean isSenderIdPresentInList(String senderId) {

        // Wait for table to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table")));

        By senderLocator = By.xpath(
                "//td[normalize-space()='" + senderId + "']");

        return wait.until(ExpectedConditions.visibilityOfElementLocated(senderLocator))
                .isDisplayed();
    }

    // =========================
    // Validation
    // =========================

    public boolean isSenderIdCreated() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successToast)).isDisplayed();
    }

    // =========================
    // Utilities
    // =========================

    public String generateRandomSenderId() {
        return "TXN" + System.currentTimeMillis();
    }

    // Entity ID must be 10–19 digits
    public String generateRandomEntityId() {
        Random random = new Random();
        int length = 12; // safe length between 10–19
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public void selectTypePromotional() {
        new Select(wait.until(ExpectedConditions.elementToBeClickable(typeDropdown)))
                .selectByVisibleText("Promotional");
    }

    public void selectTypeOTP() {
        new Select(wait.until(ExpectedConditions.elementToBeClickable(typeDropdown)))
                .selectByVisibleText("OTP");
    }
}