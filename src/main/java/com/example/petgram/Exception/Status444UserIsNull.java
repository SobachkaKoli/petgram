package com.example.petgram.Exception;

public class Status444UserIsNull extends ErrorCodeException{
    private static final int CODE = 444;
    public Status444UserIsNull(String message) {
        super(CODE, message,"444");
    }
}
