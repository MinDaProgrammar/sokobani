package com.game; // Use your actual package

public class InvalidLevelFormatException extends Exception {
    public InvalidLevelFormatException(String message) {
        super(message);
        System.out.println();
        System.out.println("******************************************************" +
                           "******************************************************");
    }
}