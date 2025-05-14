package org.example;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.example.AbstractTest.desiredOs;
import static org.example.MobileOS.ANDROID;
import static org.example.MobileOS.IOS;

public class ConfigurationResolver implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {

        String platform = "android";
        desiredOs = platform.equals("android2") ? ANDROID : IOS;
        System.out.println(desiredOs.getValue());

    }
}
