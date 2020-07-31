package com.ssw.linkedinmanager.common;

public class ExceptionManager {
    public static void exceptionLog(Exception exception) {
        exception.printStackTrace();
    }

    public static void errorLog(Error error) {
        error.printStackTrace();
    }

    public static void throwableLog(Throwable throwable) {
        throwable.printStackTrace();
    }
}
