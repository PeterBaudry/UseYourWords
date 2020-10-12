package com.peton.useyourwords.exceptions;

public class ItemNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "The requested item does not exist.";
    }
}
