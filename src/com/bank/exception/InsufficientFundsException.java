package com.bank.exception;

/**
 * Thrown when a withdrawal or transfer is attempted for an amount
 * greater than the available account balance.
 */
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
