package org.example;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.appium.SelenideAppium.$x;


public class StartTest extends AbstractTest{

    private SelenideElement start =$x("//XCUIElementTypeButton[@name='KitButton_Войти']");


   @Test
    public void docker(){
        System.out.println("iOS, Hi!");
        start.click();
        Assertions.assertTrue(true);
    }
}
