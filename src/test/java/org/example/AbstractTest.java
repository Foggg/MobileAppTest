package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({
        MobileDriverResolver.class,
        ConfigurationResolver.class
})
public abstract class AbstractTest {

    public static volatile MobileOS desiredOs;

    @BeforeEach
    public void beforeEach(){
        System.out.println("desired OS: " + desiredOs);
    }

}