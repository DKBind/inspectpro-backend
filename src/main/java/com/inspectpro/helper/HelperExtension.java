package com.inspectpro.helper;

import org.springframework.stereotype.Component;

@Component
public class HelperExtension {

    public static void Print(Object message) {
        if (true) {
            System.out.println(message);
        }
    }
}
