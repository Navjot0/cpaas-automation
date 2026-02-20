package com.automation.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    public static String captureScreenshot(WebDriver driver, String testName) {

        if (driver == null) {
            System.out.println("Driver is null. Screenshot not captured.");
            return null;
        }

        try {

            // Create timestamp
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timeStamp + ".png";

            // Create screenshots directory if not exists
            String projectPath = System.getProperty("user.dir");
            Path screenshotDir = Paths.get(projectPath, "screenshots");

            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
            }

            // Final screenshot path
            Path screenshotPath = screenshotDir.resolve(fileName);

            // Take screenshot
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            Files.copy(srcFile.toPath(), screenshotPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Screenshot saved at: " + screenshotPath);

            return screenshotPath.toString();

        } catch (IOException e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
