package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import com.automation.utils.WaitUtils;

public class LoginPage {

    WebDriver driver;
    WaitUtils wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    private By email = By.xpath("//input[@type='email']");
    private By password = By.xpath("//input[@type='password']");
    private By loginBtn = By.xpath("//button[contains(text(),'Login')]");

    // Dashboard unique element
    private By channelsMenu =
            By.xpath("//nav//a[contains(@href,'/channels')]");


    public void login(String user, String pass) {

        wait.waitForVisible(email);

        driver.findElement(email).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        driver.findElement(loginBtn).click();

        // Wait until page fully loads
        waitForPageLoad();

        // Wait until dashboard element visible
        wait.waitForVisible(channelsMenu);
    }

    private void waitForPageLoad() {
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(20))
                .until(webDriver ->
                        ((JavascriptExecutor) webDriver)
                                .executeScript("return document.readyState")
                                .equals("complete"));
    }
}
