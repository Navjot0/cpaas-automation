package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.automation.utils.WaitUtils;

public class DashboardPage {

    private WebDriver driver;
    private WaitUtils wait;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    private By channelsMenu =
            By.xpath("//nav//a[contains(@href,'/channels')]");

    public void openChannels() {

        wait.waitForVisible(channelsMenu);
        wait.waitForClickable(channelsMenu);

        driver.findElement(channelsMenu).click();

        wait.waitForUrlContains("/channels");
    }
}
