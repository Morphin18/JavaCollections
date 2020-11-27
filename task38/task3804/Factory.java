package com.javarush.task.task38.task3804;

public class Factory {
    public static Throwable Exception(Enum e) {
        if (e == null) return new IllegalArgumentException();
        String message = e.name().toLowerCase().replaceAll("[_]", " ");
        String first = message.substring(0, 1).toUpperCase();
        message = first + message.substring(1);
        switch (e.getClass().getSimpleName()) {
            case "ApplicationExceptionMessage":
                return new Exception(message);
            case "DatabaseExceptionMessage":
                return new RuntimeException(message);
            case "UserExceptionMessage":
                return new Error(message);
            default:
                return new IllegalArgumentException();

        }
    }
}
