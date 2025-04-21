package org.example;

import static java.lang.System.getProperty;

import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import javax.annotation.Nonnull;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
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
        return setUpIos(capabilities);
    }

    private WebDriver setUpIos(Capabilities capabilities) {
        String packageApp = getProperty("android.game.package");
        //String activityApp = getProperty("android.emp.activity");

        XCUITestOptions options = new XCUITestOptions();
        //UiAutomator2Options options = new UiAutomator2Options();
        options.merge(capabilities);
        options.setCapability("deviceName", "IosAutomationMobile");
        options.setCapability("platformName", "iOS");
        options.setCapability("udid", "00008110-000279E83ABB801E");
        options.setCapability("appium:automationName", "XCUITest");
        options.setCapability("appium:bundleId", "ru.smartbuys.couriers");
        options.setCapability("appium:platformVersion", "18.3");
        options.setCapability("webDriverAgentUrl", "http://localhost:7777");
        //options.setAppPackage(packageApp);
        //options.setCapability("appium:remoteAdbHost", "localhost");//"host.docker.internal");
        //options.setCapability("appium:remoteAdbPort", "5037");
        //options.setCapability("uiautomator2ServerLaunchTimeout", 100000);
        //options.setAutoGrantPermissions(true);
        options.setNoReset(true);
        //options.setSkipDeviceInitialization(true);
        try {
            var remoteAddress = new URL("http://127.0.0.1:4723/");
            System.out.println("remoteAddress: " + remoteAddress);
            System.out.println("platform: " + options.getPlatformName());
            return new IOSDriver(remoteAddress, options);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
