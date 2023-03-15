package ru.otus.solid.exception;

public class NotEnoughCashException extends RuntimeException {

    public NotEnoughCashException(String message) {
        super(message);
    }
}
