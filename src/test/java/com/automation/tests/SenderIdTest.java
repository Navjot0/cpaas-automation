package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.SenderIdPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SenderIdTest extends BaseTest {

    private SenderIdPage sender;

    @BeforeClass
    public void init() {
        navigateToSenderIdPage();
        sender = new SenderIdPage(driver);
    }

    // ✅ Transactional
    @Test(priority = 1)
    public void test_CreateTransactionalSenderId() {

        String createdId = sender.createSenderId("Transactional");

        Assert.assertTrue(
                sender.isSenderIdPresentInList(createdId),
                "Transactional Sender ID not found!"
        );
    }

    // ✅ OTP
    @Test(priority = 2)
    public void test_CreateOtpSenderId() {

        String createdId = sender.createSenderId("OTP");

        Assert.assertTrue(
                sender.isSenderIdPresentInList(createdId),
                "OTP Sender ID not found!"
        );
    }

    // ✅ Promotional
    @Test(priority = 3)
    public void test_CreatePromotionalSenderId() {

        String createdId = sender.createSenderId("Promotional");

        Assert.assertTrue(
                sender.isSenderIdPresentInList(createdId),
                "Promotional Sender ID not found!"
        );
    }
}