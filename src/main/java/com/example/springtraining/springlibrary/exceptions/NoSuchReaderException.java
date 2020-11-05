package com.example.springtraining.springlibrary.exceptions;

public class NoSuchReaderException extends RuntimeException {

    public NoSuchReaderException(Long accountId) {
        super("Could not find reader with account: " + accountId);
    }
}
