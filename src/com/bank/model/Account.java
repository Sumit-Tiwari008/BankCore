package com.bank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bank account owned by a user. Holds balance and the
 * complete transaction history for that account.
 */
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum AccountType {
        SAVINGS, CURRENT
    }

    private String accountNumber;
    private String userId;
    private AccountType accountType;
    private double balance;
    private List<Transaction> transactions;

    public Account(String accountNumber, String userId, AccountType accountType, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = initialDeposit;
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getUserId() {
        return userId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    @Override
    public String toString() {
        return String.format("Account No: %s | Type: %s | Balance: %.2f | Owner: %s",
                accountNumber, accountType, balance, userId);
    }
}
