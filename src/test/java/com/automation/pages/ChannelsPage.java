package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.automation.utils.WaitUtils;

public class ChannelsPage {

    private WebDriver driver;
    private WaitUtils wait;

    public ChannelsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    private By getChannelOverviewBtn(String channel) {

        String urlPart;

        switch (channel.toLowerCase()) {

            case "sms":
                urlPart = "/channels/sms/overview";
                break;

            case "whatsapp":
                urlPart = "/whatsapp/channels/overview";
                break;

            default:
                throw new RuntimeException("Unsupported channel: " + channel);
        }

        return By.xpath("//a[contains(@href,'" + urlPart + "')]");
    }

    public void openChannelManage(String channel) {

        By channelBtn = getChannelOverviewBtn(channel);

        wait.waitForVisible(channelBtn);
        wait.waitForClickable(channelBtn);

        driver.findElement(channelBtn).click();

        // Wait for correct URL
        if (channel.equalsIgnoreCase("whatsapp")) {
            wait.waitForUrlContains("/whatsapp/channels/overview");
        } else {
            wait.waitForUrlContains("/channels/" + channel.toLowerCase() + "/overview");
        }

        System.out.println("✅ Opened " + channel.toUpperCase() + " Manage Page");
    }
}
