package org.example;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.SelenideAppium.$x;

public class StartTest extends AbstractTest{

    private SelenideElement startANDROID = $(By.id("com.sovcombank.mpp:id/buttonBaseLogIn"));
    private SelenideElement startIOS =$x("//XCUIElementTypeButton[@name='KitButton_Войти']");


   @Test
    public void docker() throws InterruptedException {
        System.out.println("Привет ферма!");
        if (desiredOs == MobileOS.ANDROID) startANDROID.click();
        else {
            startIOS.click();
        }
        Thread.sleep(7000);
        Assertions.assertTrue(true);
    }
}
