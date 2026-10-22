package com.kestrel.chat.auth;

public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException() {
        super("Email is already taken");
    }
}