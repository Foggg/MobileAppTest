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
//        options.setCapability("unlockType", "pin"); //device pin
//        options.setCapability("allowInvisibleElements", true);
//        options.setCapability("appium:settings[allowInvisibleElements]", true);
//        options.setCapability("appium:remoteAdbHost", "host.docker.internal");
//        options.setCapability("appium:remoteAdbPort", "5037");
//        options.setCapability("df:liveVideo", false);
//        options.setCapability("df:recordVideo", false);
//        options.setCapability("appWaitforLaunch", false);
//        options.setCapability("disableAndroidWatchers", true);
//        options.setCapability("noSign", false); // Пропустить проверку и подписание приложения с помощью отладочных ключей
        options.setCapability("uiautomator2ServerLaunchTimeout", "180000"); // number of milliseconds to wait util UiAutomator2Server is listening on the device. 30000 ms by default
//        options.setCapability("allow-cors", true);
//        options.setCapability("appium:waitForQuiescence", false);
//        options.setCapability("newCommandTimeout", "300"); // как минимум 300сек, чтобы сессия доживала до конца установки апк
//        options.setCapability("appium:disableIdLocatorAutocompletion", true);
//        options.setCapability("project", "ЕМП/МП МК v2.0");

//        options.setAutoGrantPermissions(true);
//        options.setNoReset(true);
//        options.setSkipDeviceInitialization(true);

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
        options.setCapability("resetOnSessionStartOnly", "true");
        options.setCapability("appium:showXcodeLog", true);
        options.setCapability("unlockType", "pin");
        options.setNoReset(true);

        try {
            var remoteAddress = new URL("http://127.0.0.1:4723/wd/hub");
            System.out.println("remoteAddress: " + remoteAddress);
            System.out.println("platform: " + options.getPlatformName());
            return new IOSDriver(remoteAddress, options);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to create IOSDriver", e);
        }
    }


//    private WebDriver setUpIos(Capabilities capabilities) {
//        String packageApp = getProperty("android.game.package");
//        //String activityApp = getProperty("android.emp.activity");
//
//        XCUITestOptions options = new XCUITestOptions();
//        //UiAutomator2Options options = new UiAutomator2Options();
//        options.merge(capabilities);
//        //options.setCapability("deviceName", "IosAutomationMobile");
//        options.setCapability("platformName", "iOS");
//        options.setCapability("appium:udid", "00008110-000279E83ABB801E");
//        options.setCapability("appium:automationName", "XCUITest");
//        options.setCapability("appium:bundleId", "ru.smartbuys.couriers");
//        options.setCapability("appium:platformVersion", "18.4");
//        options.setCapability("appium:webDriverAgentUrl", "http://localhost:7777");
//        options.setCapability("appium:usePreinstalledWDA", "true");
//        options.setCapability("app", "file-1747121561286.ipa");
//        options.setCapability("appium:updatedWDABundleId", "file-1747121561286.ipa");
//        options.setCapability("appium:prebuiltWDAPath", "/home/fog/WebDriverAgentRunner-Runner.app");
//        //options.setAppPackage(packageApp);
//        //options.setCapability("appium:remoteAdbHost", "localhost");//"host.docker.internal");
//        //options.setCapability("appium:remoteAdbPort", "5037");
//        //options.setCapability("uiautomator2ServerLaunchTimeout", 100000);
//        //options.setAutoGrantPermissions(true);
//        //options.setNoReset(true);
//        //options.setSkipDeviceInitialization(true);
//        try {
//            var remoteAddress = new URL("http://127.0.0.1:4723/wd/hub");
//            System.out.println("remoteAddress: " + remoteAddress);
//            System.out.println("platform: " + options.getPlatformName());
//            return new IOSDriver(remoteAddress, options);
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
