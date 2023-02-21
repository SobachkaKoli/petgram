package com.example.petgram.Exception;

public class Status446ChatRoomAlreadyExsists extends ErrorCodeException{
    private static final int CODE = 446;
    public Status446ChatRoomAlreadyExsists(String message) {
            super(CODE, message,"446");
    }
}
