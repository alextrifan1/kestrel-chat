package com.kestrel.chat.auth;

public class UsernameAlreadyTakenException extends RuntimeException {

    public UsernameAlreadyTakenException() {
        super("Username is already taken");
    }
}