package com.example.springtraining.springlibrary.exceptions;

public class BookRentalLimitMetException extends RuntimeException {

    public BookRentalLimitMetException(Long accountId) {
        super("Too many books rented for account: " + accountId);
    }
}
