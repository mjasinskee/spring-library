package com.example.springtraining.springlibrary.exceptions;

public class NoSuchBookException extends RuntimeException {

    public NoSuchBookException(String isbn) {
        super("Could not find book with ISBN: " + isbn);
    }
}
