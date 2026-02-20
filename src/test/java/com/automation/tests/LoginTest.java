package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginTest(){

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login("Kanchan.shinde@globeteleservices.com", "Testqa@123");

        String currentUrl = driver.getCurrentUrl();

        Assert.assertNotEquals(currentUrl,
                "https://testqa.gtsstaging.com/login",
                "Login failed with valid credentials");
    }

    @Test
    public void invalidLoginTest(){

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login("wrong@email.com", "wrongpass");

        String currentUrl = driver.getCurrentUrl();

        Assert.assertEquals(currentUrl,
                "https://testqa.gtsstaging.com/login",
                "User logged in with invalid credentials");
    }

    @Test
    public void emptyFieldTest(){

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login("", "");

        String currentUrl = driver.getCurrentUrl();

        Assert.assertEquals(currentUrl,
                "https://testqa.gtsstaging.com/login",
                "Login should not work with empty fields");
    }
}

