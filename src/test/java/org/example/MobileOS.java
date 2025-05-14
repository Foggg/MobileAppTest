package org.example;

import lombok.Getter;

public enum MobileOS {
    ANDROID("android"),
    IOS("ios");

    @Getter
    private String value;

    MobileOS(String value){
        this.value = value;
    }
}
