package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class SmsCampaignTest extends BaseTest {

    // 🔥 Reset state before EACH test (IMPORTANT)
    @BeforeMethod
    public void resetState() {

        // 1️⃣ Wait for SweetAlert to disappear if present
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.invisibilityOfElementLocated(
                            By.cssSelector(".swal2-container")));
        } catch (Exception ignored) {}

        // 2️⃣ Wait for any loader/skeleton
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(
                            By.cssSelector(".animate-pulse, .animate-spin")));
        } catch (Exception ignored) {}

        // 3️⃣ Always start from SMS campaign list page
        driver.get("https://testqa.gtsstaging.com/campaigns/sms");

        // 4️⃣ Extra safety wait
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(
                            By.cssSelector(".animate-pulse, .animate-spin")));
        } catch (Exception ignored) {}
    }

    private void createSmsCampaign(
            boolean duplicateToggle,
            boolean variableTemplate,
            boolean scheduleMode,
            boolean useCsvImport  // 🔥 New parameter for CSV import
    ) {

        String campaignName = "Auto_" + System.currentTimeMillis();

        CampaignListPage list = new CampaignListPage(driver, "sms");
        list.clickCreateNewCampaign();

        CreateCampaignPage create = new CreateCampaignPage(driver);

        create.enterCampaignName(campaignName);

        // 1️⃣ Import Contacts - Choose between manual entry or CSV
        if (useCsvImport) {
            create.importContactsFromCsv(duplicateToggle);
            create.waitForUploadSuccessToDisappear();  // 🔥 Wait for CSV upload success
        } else {
            create.importContacts(
                    "919876543210\n919988776655\n919988776655",
                    duplicateToggle
            );
        }

        create.waitForSweetAlertToClose();   // 🔥 IMPORTANT

        // 2️⃣ Select Sender
        create.selectSender("DUMMY");

        // 3️⃣ Select Template
        if (variableTemplate) {
            create.selectTemplate("var2temp");
            create.autoFillTemplateVariablesIfPresent();
        } else {
            create.selectTemplate("sample_test");
        }

        // 4️⃣ Mode Selection
        if (scheduleMode) {

            create.selectScheduleForLater();
            create.scheduleForNextDayAtNoon();
            create.clickPreviewCampaign();
            create.clickScheduleCampaign();

        } else {

            create.selectSendNow();
            create.clickPreviewCampaign();
            create.clickSendCampaign();
        }

        create.waitForSweetAlertToClose();  // 🔥 safety

        list.verifyCampaignCreated(campaignName);
    }

    // ===========================
    // 🔥 TEST CASES
    // ===========================

    @Test(priority = 1)
    public void test_DuplicateOn_Variable_SendNow() {
        createSmsCampaign(true, true, false, false);
    }

    @Test(priority = 2)
    public void test_DuplicateOff_Variable_Schedule() {
        createSmsCampaign(false, true, true, false);
    }

    @Test(priority = 3)
    public void test_DuplicateOn_NonVariable_SendNow() {
        createSmsCampaign(true, false, false, false);
    }

    @Test(priority = 4)
    public void test_DuplicateOff_NonVariable_Schedule() {
        createSmsCampaign(false, false, true, false);
    }

    // ===========================
    // 🔥 NEW CSV IMPORT TEST CASES
    // ===========================

    @Test(priority = 5)
    public void test_CsvImport_DuplicateOn_Variable_SendNow() {
        // Test CSV import with duplicates kept, variable template, send now
        createSmsCampaign(true, true, false, true);
    }

    @Test(priority = 6)
    public void test_CsvImport_DuplicateOff_Variable_Schedule() {
        // Test CSV import with duplicates removed, variable template, schedule
        createSmsCampaign(false, true, true, true);
    }

    @Test(priority = 7)
    public void test_CsvImport_DuplicateOn_NonVariable_SendNow() {
        // Test CSV import with duplicates kept, non-variable template, send now
        createSmsCampaign(true, false, false, true);
    }

    @Test(priority = 8)
    public void test_CsvImport_DuplicateOff_NonVariable_Schedule() {
        // Test CSV import with duplicates removed, non-variable template, schedule
        createSmsCampaign(false, false, true, true);
    }
}