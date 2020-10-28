package com.peton.useyourwords.exceptions;

public class NoFunnyItemsException extends RuntimeException {

    //<editor-fold desc="Methods">

    @Override
    public String getMessage() {
        return "There are no funny items saved in the database.";
    }

    //</editor-fold>

}
