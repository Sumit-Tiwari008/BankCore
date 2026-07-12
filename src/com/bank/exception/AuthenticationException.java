package com.bank.exception;

/**
 * Thrown when login fails due to an unknown email or an incorrect
 * password.
 */
public class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }
}
