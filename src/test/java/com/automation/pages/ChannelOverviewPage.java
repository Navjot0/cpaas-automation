package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.automation.utils.WaitUtils;

public class ChannelOverviewPage {

    private WebDriver driver;
    private WaitUtils wait;

    public ChannelOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    private By getCampaignsTab(String channel) {

        String urlPart;

        switch (channel.toLowerCase()) {

            case "sms":
                urlPart = "/campaigns/sms";
                break;

            case "whatsapp":
                urlPart = "/whatsapp/campaigns";
                break;

            default:
                throw new RuntimeException("Unsupported channel: " + channel);
        }

        return By.xpath("//a[contains(@href,'" + urlPart + "') and contains(.,'Campaign')]");
    }


}
