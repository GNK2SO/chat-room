package com.gnk2so.chatroom.user.exception;

public class AlreadyUsedEmailException extends RuntimeException {

    public static final String MESSAGE = "Already used email";

    public AlreadyUsedEmailException() {
        super(MESSAGE);
    }

}
