package com.automation.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.function.Function;

public class WaitUtils {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final int DEFAULT_TIMEOUT = 20;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        this.wait.ignoring(StaleElementReferenceException.class);
    }

    // ✅ Wait for Visibility
    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }


    // ✅ Wait for Clickable
    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ✅ Wait for Invisibility
    public boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // ✅ Wait for URL Contains
    public boolean waitForUrlContains(String partialUrl) {
        return wait.until(ExpectedConditions.urlContains(partialUrl));
    }

    // ✅ Custom Fluent Wait
    public <T> T fluentWait(Function<WebDriver, T> condition, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(condition);
    }



}
