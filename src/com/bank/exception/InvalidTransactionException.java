package com.bank.exception;

/**
 * Thrown when a transaction request is invalid, e.g. a zero/negative
 * amount, or a transfer to the same account.
 */
public class InvalidTransactionException extends Exception {
    public InvalidTransactionException(String message) {
        super(message);
    }
}
