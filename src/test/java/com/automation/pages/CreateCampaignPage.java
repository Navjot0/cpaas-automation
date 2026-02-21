package com.automation.pages;

import org.openqa.selenium.*;
import com.automation.utils.WaitUtils;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.io.File;



public class CreateCampaignPage {

    protected WebDriver driver;
    protected WaitUtils wait;

    public CreateCampaignPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    /* =====================================================
       COMMON LOCATORS
    ===================================================== */

    private By campaignNameField = By.id("name");

    private By importContactsBtn =
            By.xpath("//button[normalize-space()='Import Contacts']");

    private By contactTextArea = By.id("cp_contacts");

    private By continueBtn =
            By.xpath("//button[normalize-space()='Continue']");

    private By senderDropdownBtn =
            By.xpath("//button[.//span[contains(text(),'Select Sender ID')]]");

    private By senderSearchInput =
            By.xpath("//input[@type='search']");

    private By swalConfirmBtn =
            By.cssSelector("button.swal2-confirm");

    private List<String> defaultTemplateValues = Arrays.asList(
            "Navjot",
            "10000",
            "Delhi",
            "TestValue",
            "500"
    );

    private By templateDropdownBtn =
            By.xpath("//button[.//span[normalize-space()='Select Template']]");



    /* =====================================================
       CAMPAIGN NAME
    ===================================================== */

    public void enterCampaignName(String name) {

        WebElement field = wait.waitForVisible(campaignNameField);
        field.clear();
        field.sendKeys(name);
    }

    /* =====================================================
       IMPORT CONTACTS
    ===================================================== */

    public void importContacts(String numbers, boolean keepDuplicates) {

        WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(20));

        // 1️⃣ Open Import Contacts modal
        wait.waitForClickable(importContactsBtn).click();

        wait.waitForVisible(contactTextArea);

        // 2️⃣ Handle Duplicate Toggle
        By duplicateCheckbox = By.id("keep_duplicates"); // Confirm ID in DOM

        WebElement checkbox = waitCustom.until(
                ExpectedConditions.presenceOfElementLocated(duplicateCheckbox)
        );

        boolean isChecked = checkbox.isSelected();

        if (keepDuplicates && !isChecked) {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", checkbox);
            System.out.println("✅ Duplicate Handling turned ON");
        }
        else if (!keepDuplicates && isChecked) {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", checkbox);
            System.out.println("✅ Duplicate Handling turned OFF");
        }
        else {
            System.out.println("ℹ Duplicate Handling already correct");
        }

        // 3️⃣ Enter contact numbers
        WebElement textArea = driver.findElement(contactTextArea);
        textArea.clear();
        textArea.sendKeys(numbers);

        // 4️⃣ Click Continue
        wait.waitForClickable(continueBtn).click();

        // 5️⃣ Wait until modal closes
        wait.waitForInvisibility(contactTextArea);

        System.out.println("✅ Contacts imported successfully");
    }


    /* =====================================================
       SELECT SENDER
    ===================================================== */

    public void selectSender(String senderName) {

        // Open dropdown
        wait.waitForClickable(senderDropdownBtn);
        driver.findElement(senderDropdownBtn).click();

        // Wait for search input
        wait.waitForVisible(senderSearchInput);

        // Type sender name
        driver.findElement(senderSearchInput).clear();
        driver.findElement(senderSearchInput).sendKeys(senderName);

        // Wait for filtered result
        By senderOption =
                By.xpath("//*[normalize-space()='" + senderName + "']");

        wait.waitForVisible(senderOption);
        wait.waitForClickable(senderOption);

        driver.findElement(senderOption).click();

        // Optional: verify selected
        By selectedValue =
                By.xpath("//button[.//span[contains(text(),'" + senderName + "')]]");

        wait.waitForVisible(selectedValue);
    }


    public void selectTemplate(String templateName) {

        // 1️⃣ Open dropdown
        wait.waitForClickable(templateDropdownBtn);
        driver.findElement(templateDropdownBtn).click();

        // 2️⃣ Wait for search input
        By templateSearchInput = By.xpath("(//input[@type='search'])[last()]");
        wait.waitForVisible(templateSearchInput);

        WebElement searchBox = driver.findElement(templateSearchInput);
        searchBox.clear();
        searchBox.sendKeys(templateName);

        // Small wait for Alpine filtering
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        // 3️⃣ Click correct option container (IMPORTANT FIX 🔥)
        By templateOption = By.xpath(
                "//div[@select-option]//div[contains(normalize-space(),'"
                        + templateName + "')]/parent::div[@select-option]"
        );

        wait.waitForVisible(templateOption);
        wait.waitForClickable(templateOption);

        WebElement option = driver.findElement(templateOption);
        option.click();

        // 4️⃣ Verify selection appears in button like Sender
        By selectedValue = By.xpath(
                "//button[.//span[contains(text(),'" + templateName + "')]]"
        );

        wait.waitForVisible(selectedValue);

        System.out.println("✅ Template selected successfully");
    }


    public void selectSendNow() {

        // Click the visible Send Now card
        By sendNowCard = By.xpath("//label[contains(.,'Send Now')]");

        wait.waitForClickable(sendNowCard);
        driver.findElement(sendNowCard).click();

        // Verify selection by checking underlying radio input
        By sendNowInput = By.id("send_now");

        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(driver -> driver.findElement(sendNowInput).isSelected());

        System.out.println("✅ Send Now selected");
    }

    /* =====================================================
       PREVIEW CAMPAIGN
    ===================================================== */

    public void clickPreviewCampaign() {

        By previewBtn =
                By.xpath("//button[contains(.,'Preview Campaign')]");

        WebDriverWait customWait =
                new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement button =
                customWait.until(ExpectedConditions.visibilityOfElementLocated(previewBtn));

        customWait.until(driver -> button.isEnabled());

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", button);

        System.out.println("✅ Preview clicked");
    }

    /* =====================================================
       SEND CAMPAIGN
    ===================================================== */

    public void clickSendCampaign() {

        By sendBtn =
                By.xpath("//button[contains(.,'Send Campaign')]");

        WebDriverWait customWait =
                new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement button =
                customWait.until(ExpectedConditions.visibilityOfElementLocated(sendBtn));

        customWait.until(driver -> button.isEnabled());

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", button);

        By spinner =
                By.xpath("//svg[contains(@class,'animate-spin')]");

        wait.waitForInvisibility(spinner);

        System.out.println("✅ Campaign submitted successfully");
    }

    public void autoFillTemplateVariablesIfPresent() {

        WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Find all variable inputs dynamically
        List<WebElement> variableInputs = driver.findElements(
                By.xpath("//input[starts-with(@id,'columnMapping.')]")
        );


        if (variableInputs.isEmpty()) {
            System.out.println("ℹ No template variables required.");
            return;
        }

        System.out.println("🔹 Variables detected: " + variableInputs.size());

        for (int i = 0; i < variableInputs.size(); i++) {

            WebElement input = variableInputs.get(i);

            String valueToUse;

            // Pick value from default list safely
            if (i < defaultTemplateValues.size()) {
                valueToUse = defaultTemplateValues.get(i);
            } else {
                valueToUse = "AutoValue" + (i + 1);
            }

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", input);

            // Livewire safe input update
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = arguments[1];" +
                            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                    input,
                    valueToUse
            );

            System.out.println("✅ Auto-filled variable " + (i + 1) + " with: " + valueToUse);
        }
    }



    public void selectScheduleForLater() {

        By scheduleCard = By.xpath("//label[contains(.,'Schedule for Later')]");

        wait.waitForClickable(scheduleCard);
        driver.findElement(scheduleCard).click();

        // 🔥 IMPORTANT: Wait for date-time picker section to appear
        By dateInput = By.xpath("//input[@type='datetime-local' or @type='date']");

        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(dateInput));

        System.out.println("✅ Schedule for Later selected and date section visible");
    }


    public void scheduleForNextDayAtNoon() {

        WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(20));

        // 1️⃣ Wait for date input
        By dateLocator = By.xpath("//input[@type='date']");
        WebElement dateInput = waitCustom.until(
                ExpectedConditions.visibilityOfElementLocated(dateLocator)
        );

        // 2️⃣ Calculate tomorrow
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        // IMPORTANT: type="date" requires yyyy-MM-dd format
        String formattedDate = tomorrow.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        // 3️⃣ Set date directly
        dateInput.clear();
        dateInput.sendKeys(formattedDate);

        System.out.println("✅ Date set to: " + formattedDate);

        // 4️⃣ Select time using Select class
        By timeLocator = By.xpath("//select");
        WebElement timeDropdown = waitCustom.until(
                ExpectedConditions.visibilityOfElementLocated(timeLocator)
        );

        Select select = new Select(timeDropdown);
        select.selectByValue("12:00");

        System.out.println("✅ Time set to: 12:00");
    }

    public void clickScheduleCampaign() {

        By scheduleBtn =
                By.xpath("//button[contains(.,'Schedule Campaign')]");

        WebDriverWait customWait =
                new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement button =
                customWait.until(ExpectedConditions.visibilityOfElementLocated(scheduleBtn));

        customWait.until(driver -> button.isEnabled());

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", button);

        By spinner =
                By.xpath("//svg[contains(@class,'animate-spin')]");

        wait.waitForInvisibility(spinner);

        System.out.println("✅ Campaign scheduled successfully");
    }

    public void waitForSweetAlertToClose() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(
                            By.cssSelector(".swal2-container")));
        } catch (Exception ignored) {}
    }

    public void importContactsFromCsv(boolean keepDuplicates) {

        WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(40));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // ===============================
        // 📂 CSV Path
        // ===============================
        String filePath = Paths.get(
                "src",
                "test",
                "testdata",
                "phonenumbers.csv"
        ).toAbsolutePath().toString();

        File file = new File(filePath);

        if (!file.exists()) {
            throw new RuntimeException("❌ CSV file not found at: " + filePath);
        }

        System.out.println("📂 Using CSV file: " + filePath);

        // ===============================
        // 1️⃣ Open Import Modal
        // ===============================
        wait.waitForClickable(importContactsBtn).click();

        By modalLocator = By.xpath("//div[contains(@class,'fixed')]");
        waitCustom.until(ExpectedConditions.visibilityOfElementLocated(modalLocator));

        // ===============================
        // 2️⃣ Click File Upload Tab
        // ===============================
        By fileUploadTab = By.xpath("//a[contains(.,'File Upload')]");
        waitCustom.until(ExpectedConditions.elementToBeClickable(fileUploadTab)).click();

        // ===============================
        // 3️⃣ Upload File
        // ===============================
        By fileInput = By.xpath("//input[@type='file']");
        WebElement upload = waitCustom.until(
                ExpectedConditions.presenceOfElementLocated(fileInput)
        );

        upload.sendKeys(filePath);
        System.out.println("✅ CSV file uploaded");

        // ===============================
        // 4️⃣ Wait For Upload Processing
        // ===============================
        By skeletonLoader = By.xpath("//div[contains(@class,'animate-pulse')]");
        waitCustom.until(ExpectedConditions.invisibilityOfElementLocated(skeletonLoader));

        // Small DOM stabilization wait
        waitCustom.until(driver ->
                js.executeScript("return document.readyState").equals("complete")
        );

        // ===============================
        // 5️⃣ Handle Duplicate Toggle
        // ===============================
        By duplicateCheckbox = By.id("keep_duplicates");

        WebElement checkbox = waitCustom.until(
                ExpectedConditions.presenceOfElementLocated(duplicateCheckbox)
        );

        boolean isChecked = checkbox.isSelected();

        if (keepDuplicates && !isChecked) {
            js.executeScript("arguments[0].click();", checkbox);
            System.out.println("✅ Duplicate Handling turned ON");
        } else if (!keepDuplicates && isChecked) {
            js.executeScript("arguments[0].click();", checkbox);
            System.out.println("✅ Duplicate Handling turned OFF");
        } else {
            System.out.println("ℹ Duplicate Handling already correct");
        }

        // ===============================
        // 6️⃣ Click Continue (Scoped Inside Modal)
        // ===============================
        By continueBtn = By.xpath(
                "//div[contains(@class,'fixed')]//button[normalize-space()='Continue']"
        );

        WebElement continueButton = waitCustom.until(
                ExpectedConditions.visibilityOfElementLocated(continueBtn)
        );

        waitCustom.until(driver -> continueButton.isEnabled());

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", continueButton);

        try { Thread.sleep(400); } catch (Exception ignored) {}

        // Click Continue
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", continueButton);

        System.out.println("✅ Continue clicked");

// Wait for success banner
        By successBanner = By.xpath("//*[contains(text(),'Contacts Imported') or contains(text(),'Ready to send')]");
        waitCustom.until(ExpectedConditions.visibilityOfElementLocated(successBanner));

        System.out.println("✅ CSV contacts imported successfully");

// Stabilize DOM
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    public void waitForUploadSuccessToDisappear() {

        WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            By successPopup = By.xpath("//*[contains(text(),'success') or contains(text(),'Imported')]");
            waitCustom.until(ExpectedConditions.visibilityOfElementLocated(successPopup));
            waitCustom.until(ExpectedConditions.invisibilityOfElementLocated(successPopup));
            System.out.println("✅ Upload success popup closed");
        } catch (Exception ignored) {
            System.out.println("ℹ No upload popup detected");
        }

        // Extra safety wait for Livewire re-render
        try {
            Thread.sleep(600);
        } catch (InterruptedException ignored) {}
    }


    public void enterTemplateVariableValue(int keyNumber, String value) {

        By inputField = By.id("columnMapping." + keyNumber);

        wait.waitForElementVisible(inputField).clear();
        wait.waitForElementVisible(inputField).sendKeys(value);

        System.out.println("✅ Entered value '" + value + "' for Key " + keyNumber);
    }


}
