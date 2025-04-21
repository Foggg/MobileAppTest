package org.example;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.appium.AppiumDriverRunner.getAndroidDriver;
import static com.codeborne.selenide.appium.AppiumDriverRunner.getIosDriver;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * кастомный
 */
public class MobileDriverResolver implements BeforeEachCallback, AfterEachCallback
        {
    private final int CONFIGURATION_TIMEOUT = 30_000;
    private final Logger LOGGER = LoggerFactory.getLogger(MobileDriverResolver.class);
    private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock(true);


    @Override
    public void beforeEach(ExtensionContext context) {
        try {
            REENTRANT_LOCK.lock();

            closeWebDriver();
            Configuration.browserSize = null;
            Configuration.timeout = CONFIGURATION_TIMEOUT;
            Configuration.browser = CustomMobileDriver.class.getName();
            open();
            //getAndroidDriver().activateApp("com.sovcombank.mpp");
        } finally {
            REENTRANT_LOCK.unlock();
        }
    }

    //@Override
    public void afterEach(ExtensionContext extensionContext) {
        try {
            REENTRANT_LOCK.lock();
            Selenide.closeWebDriver();
            LOGGER.info("Webdriver closed");
        } finally {
            REENTRANT_LOCK.unlock();
        }
    }
}
