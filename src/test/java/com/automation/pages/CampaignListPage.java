package com.automation.pages;

import org.openqa.selenium.*;
import com.automation.utils.WaitUtils;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CampaignListPage {

    private WebDriver driver;
    private WaitUtils wait;
    private String channel;

    public CampaignListPage(WebDriver driver, String channel) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
        this.channel = channel.toLowerCase();
    }

    // 🔥 Dynamic Create Button based on Channel
    private By getCreateCampaignBtn() {

        if (channel.equals("whatsapp")) {
            return By.xpath("//a[contains(@href,'/whatsapp/campaigns/create')]");
        }

        return By.xpath("//a[contains(@href,'/campaigns/" + channel + "/create')]");
    }

    public void clickCreateNewCampaign() {

        By createBtn = getCreateCampaignBtn();

        wait.waitForVisible(createBtn);
        wait.waitForClickable(createBtn);

        driver.findElement(createBtn).click();

        // 🔥 Wait for correct URL
        if (channel.equals("whatsapp")) {
            wait.waitForUrlContains("/whatsapp/campaigns/create");
        } else {
            wait.waitForUrlContains("/campaigns/" + channel + "/create");
        }

        System.out.println("✅ Opened " + channel.toUpperCase() + " Create Campaign Page");
    }

    public void verifyCampaignCreated(String campaignName) {

        // 🔥 Wait until back to correct campaign list page
        if (channel.equals("whatsapp")) {
            wait.waitForUrlContains("/whatsapp/campaigns");
        } else {
            wait.waitForUrlContains("/campaigns/" + channel);
        }

        // Wait for table to load
        By tableLocator = By.xpath("//table");
        wait.waitForVisible(tableLocator);

        // Wait for campaign name to appear
        By campaignLocator =
                By.xpath("//table//td[contains(normalize-space(),'" + campaignName + "')]");

        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement campaignElement = customWait.until(
                ExpectedConditions.visibilityOfElementLocated(campaignLocator)
        );

        String text = campaignElement.getText();

        if (!text.contains(campaignName)) {
            throw new RuntimeException("❌ Campaign not created for " + channel);
        }

        System.out.println("✅ " + channel.toUpperCase() + " Campaign created successfully: " + campaignName);
    }
}
