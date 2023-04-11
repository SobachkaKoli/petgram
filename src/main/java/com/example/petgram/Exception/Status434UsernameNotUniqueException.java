package com.example.petgram.Exception;

public class Status434UsernameNotUniqueException extends ErrorCodeException{
    public static final int CODE = 434;

    public Status434UsernameNotUniqueException() {
        super(CODE,"This nickname is already taken","434");
    }
}
