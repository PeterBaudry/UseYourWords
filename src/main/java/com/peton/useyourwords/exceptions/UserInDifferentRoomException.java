package com.peton.useyourwords.exceptions;

public class UserInDifferentRoomException extends RuntimeException {

    //<editor-fold desc="Methods">

    @Override
    public String getMessage() {
        return "The user is in a different room.";
    }

    //</editor-fold>

}
