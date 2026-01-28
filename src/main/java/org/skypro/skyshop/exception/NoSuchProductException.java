package org.skypro.skyshop.exception;

public class NoSuchProductException extends RuntimeException {
    private final String code;
    private final String message;

    public NoSuchProductException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
