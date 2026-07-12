package com.bank.exception;

/**
 * Thrown when a registration request uses an email address that is
 * already associated with an existing account.
 */
public class DuplicateUserException extends Exception {
    public DuplicateUserException(String message) {
        super(message);
    }
}
