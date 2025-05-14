package org.example;

import static org.example.AbstractTest.desiredOs;
import static org.example.MobileOS.ANDROID;

import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import javax.annotation.Nonnull;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import javax.annotation.CheckReturnValue;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * драйвер для доступа к ui android/ios
 */
public class CustomMobileDriver implements WebDriverProvider {
    @Override
    @CheckReturnValue
    @Nonnull
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {
        return (desiredOs == ANDROID) ? setUpAndroid(capabilities) : setUpIOS(capabilities);
    }

    private WebDriver setUpAndroid(Capabilities capabilities) {

        UiAutomator2Options options = new UiAutomator2Options();
        options.merge(capabilities);
        options.setAppPackage("com.sovcombank.mpp");
        options.setAppActivity("com.sovcombank.mpp.start.view.StartActivity");

        try {
            var remoteAddress = new URL("http://127.0.0.1:4723/wd/hub");
            System.out.println("remoteAddress: " + remoteAddress);
            System.out.println("platform: " + options.getPlatformName());
            AndroidDriver driver = new AndroidDriver(remoteAddress, options);

            if (driver.getSessionId() == null) {
                throw new RuntimeException("Session ID is null, session creation failed");
            }

            System.out.println("Session created successfully with ID: " + driver.getSessionId());
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create AndroidDriver", e);
        }
    }

    private WebDriver setUpIOS(Capabilities capabilities) {

        XCUITestOptions options = new XCUITestOptions();
        options.merge(capabilities);
        options.setCapability("bundleId", "ru.smartbuys.couriers");

        try {
            var remoteAddress = new URL("http://127.0.0.1:4723/wd/hub");
            System.out.println("remoteAddress: " + remoteAddress);
            System.out.println("platform: " + options.getPlatformName());
            return new IOSDriver(remoteAddress, options);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to create IOSDriver", e);
        }
    }
}