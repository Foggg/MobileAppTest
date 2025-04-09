package org.example;

import static java.lang.System.getProperty;

import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import javax.annotation.Nonnull;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

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
        return setUpAndroid(capabilities);
    }

    private WebDriver setUpAndroid(Capabilities capabilities) {
        String packageApp = getProperty("android.game.package");
        //String activityApp = getProperty("android.emp.activity");

        UiAutomator2Options options = new UiAutomator2Options();
        options.merge(capabilities);
        options.setAppPackage(packageApp);
        options.setCapability("appium:remoteAdbHost", "host.docker.internal");
        options.setCapability("appium:remoteAdbPort", "5037");
        options.setCapability("uiautomator2ServerLaunchTimeout", 100000);
        options.setAutoGrantPermissions(true);
        options.setNoReset(true);
        options.setSkipDeviceInitialization(true);
        try {
            var remoteAddress = new URL("http://127.0.0.1:4723/wd/hub");
            System.out.println("remoteAddress: " + remoteAddress);
            System.out.println("platform: " + options.getPlatformName());
            return new AndroidDriver(remoteAddress, options);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
