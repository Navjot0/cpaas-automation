package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SmsCampaignPage {

    WebDriver driver;

    public SmsCampaignPage(WebDriver driver) {
        this.driver = driver;
    }


    private By createNewCampaignBtn =
            By.xpath("//button[contains(text(),'Create New Campaign')]");


}
