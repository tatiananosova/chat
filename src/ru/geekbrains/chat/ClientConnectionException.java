package ru.geekbrains.chat;

public class ClientConnectionException extends RuntimeException {
    public ClientConnectionException(String message) {
        super(message);
    }
    public ClientConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
