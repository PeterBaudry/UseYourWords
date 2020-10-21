package com.peton.useyourwords.exceptions;

public class ItemNotFoundException extends RuntimeException {
    //<editor-fold desc="Methods">

    @Override
    public String getMessage() {
        return "The requested item does not exist.";
    }

    //</editor-fold>
}
