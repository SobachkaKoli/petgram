package com.example.petgram.Exception;

//  TODO (Bogdan O.) 26/4/23: check exceptions names and rename to HTTP format
public class Status444UserIsNull extends ErrorCodeException{
    private static final int CODE = 444;
    public Status444UserIsNull(String message) {
        super(CODE, message,"444");
    }
}
