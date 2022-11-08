package com.officemap.authorization;

public class UnauthorizedTokenException extends  Exception {
    public UnauthorizedTokenException(String message) {
        super(message);
    }
}
