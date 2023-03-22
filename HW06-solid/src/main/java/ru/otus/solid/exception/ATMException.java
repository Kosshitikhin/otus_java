package ru.otus.solid.exception;

public class ATMException extends RuntimeException {

    public ATMException(String message) {
        super(message);
    }

    public static ATMException notFound(String message) {
        return new ATMException(message);
    }

    public static ATMException notEnough(String message) {
        return new ATMException(message);
    }

    public static ATMException convert(String message) {
        return new ATMException(message);
    }
}
