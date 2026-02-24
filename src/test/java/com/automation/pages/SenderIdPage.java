package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Paths;
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

    // ========== LOCATORS ==========

    private By uploadButton = By.xpath("//button[contains(@onclick,'sms.senderid.fetch')]");
    private By fileInput = By.id("dropzone-file");
    private By yesImportButton = By.xpath("//button[contains(.,'Yes, Import')]");


    // Optional: success toast
    private By successMessage = By.xpath("//*[contains(text(),'success') or contains(text(),'Imported')]");

// ========== METHODS ==========

    public void clickUploadSenderId() {

        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(@onclick,'sms.senderid.fetch')]")
                )
        );

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@role='dialog']")
        ));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("dropzone-file")
        ));
    }

    public void uploadCsvFile(String filePath) {
        WebElement uploadElement = wait.until(ExpectedConditions.presenceOfElementLocated(fileInput));
        uploadElement.sendKeys(filePath);
    }

    public void clickYesImport() {
        wait.until(ExpectedConditions.elementToBeClickable(yesImportButton)).click();
    }

    public boolean isImportSuccessful() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void uploadSenderIdFromCsv() {

        WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(40));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // ===============================
        // 📂 CSV Path
        // ===============================
        String filePath = Paths.get(
                "src",
                "test",
                "testdata",
                "sender_id_sample.csv"
        ).toAbsolutePath().toString();

        File file = new File(filePath);

        if (!file.exists()) {
            throw new RuntimeException("❌ CSV file not found at: " + filePath);
        }

        System.out.println("📂 Using CSV file: " + filePath);

        // ===============================
        // 1️⃣ Click Upload Button
        // ===============================
        By uploadBtn = By.xpath("//button[contains(.,'Upload') and contains(.,'Sender')]");

        waitCustom.until(ExpectedConditions.elementToBeClickable(uploadBtn)).click();

        // ===============================
        // 2️⃣ Wait For Modal
        // ===============================
        By modalLocator = By.xpath("//div[contains(@class,'fixed')]");
        waitCustom.until(ExpectedConditions.visibilityOfElementLocated(modalLocator));

        // ===============================
        // 3️⃣ Upload File
        // ===============================
        By fileInput = By.id("dropzone-file");

        WebElement upload = waitCustom.until(
                ExpectedConditions.presenceOfElementLocated(fileInput)
        );

        // In case hidden
        js.executeScript("arguments[0].style.display='block';", upload);

        upload.sendKeys(filePath);

        System.out.println("✅ Sender CSV file uploaded");

        // ===============================
        // 4️⃣ Wait For Livewire Processing
        // ===============================
        By loader = By.xpath("//div[contains(@class,'animate-pulse') or contains(@class,'animate-spin')]");
        waitCustom.until(ExpectedConditions.invisibilityOfElementLocated(loader));

        waitCustom.until(driver ->
                js.executeScript("return document.readyState").equals("complete")
        );

        // ===============================
        // 5️⃣ Click Yes Import (Scoped Inside Modal)
        // ===============================
        By importBtn = By.xpath(
                "//div[contains(@class,'fixed')]//button[normalize-space()='Yes, Import']"
        );

        WebElement confirmBtn = waitCustom.until(
                ExpectedConditions.visibilityOfElementLocated(importBtn)
        );

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", confirmBtn);

        try { Thread.sleep(400); } catch (Exception ignored) {}

        js.executeScript("arguments[0].click();", confirmBtn);

        System.out.println("✅ Yes, Import clicked");

        // ===============================
        // 6️⃣ Wait For Success Message
        // ===============================
        By successBanner = By.xpath(
                "//*[contains(text(),'Imported') or contains(text(),'successfully')]"
        );

        waitCustom.until(ExpectedConditions.visibilityOfElementLocated(successBanner));

        System.out.println("✅ Sender IDs imported successfully");
    }
}