package com.example.springtraining.springlibrary.exceptions;

public class NoSuchRentalException extends RuntimeException {

    public NoSuchRentalException(Long accountId, String ISBN) {
        super(String.format("Could not find rental with account %s and ISBN %s", accountId, ISBN));
    }
}
