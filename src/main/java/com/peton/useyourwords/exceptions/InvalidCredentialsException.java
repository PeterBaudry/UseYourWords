package com.peton.useyourwords.exceptions;

public class InvalidCredentialsException extends RuntimeException {

    //<editor-fold desc="Methods">

    @Override
    public String getMessage() {
        return "The username/password don't match our records.";
    }

    //</editor-fold>

}
