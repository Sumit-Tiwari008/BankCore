package com.bank.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single transaction (deposit, withdrawal, or transfer leg)
 * recorded against an account.
 */
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public enum Type {
        DEPOSIT, WITHDRAWAL, TRANSFER_OUT, TRANSFER_IN
    }

    private String transactionId;
    private String accountNumber;
    private Type type;
    private double amount;
    private double balanceAfter;
    private LocalDateTime timestamp;
    private String description;

    public Transaction(String transactionId, String accountNumber, Type type, double amount,
                        double balanceAfter, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Type getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%-14s | %-8s | %-12s | %10.2f | %10.2f | %s",
                timestamp.format(FORMATTER), transactionId, type, amount, balanceAfter, description);
    }
}
