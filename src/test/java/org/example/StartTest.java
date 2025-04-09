package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class StartTest extends AbstractTest{

    @Test
    public void docker(){
        System.out.println("Docker, Hi!");
        Assertions.assertTrue(true);
    }
}
