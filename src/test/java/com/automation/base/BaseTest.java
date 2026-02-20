package com.automation.base;

import com.automation.pages.LoginPage;
import com.automation.utils.ScreenshotUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;

    // 🔐 Runs ONCE before all test methods in class
    @BeforeClass
    public void setup() {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://testqa.gtsstaging.com/login");

        // ✅ Login only once
        LoginPage login = new LoginPage(driver);
        login.login("Kanchan.shinde@globeteleservices.com", "Testqa@123");
    }

    // 📸 Runs after EACH test method (for screenshot)
    @AfterMethod
    public void captureFailure(ITestResult result) {

        if (ITestResult.FAILURE == result.getStatus()) {
            ScreenshotUtils.captureScreenshot(driver, result.getName());
        }
    }

    // 🔚 Runs ONCE after all test methods finish
    @AfterClass
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }


}
