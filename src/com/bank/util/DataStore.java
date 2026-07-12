package com.bank.util;

import com.bank.model.Account;
import com.bank.model.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Container for all persisted application state: users, accounts and
 * the running counters used to generate unique IDs. A single instance
 * of this class is serialized to disk so that data survives between
 * runs of the prototype (satisfies the persistence requirement).
 */
public class DataStore implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, User> usersById = new HashMap<>();
    private Map<String, String> userIdByEmail = new HashMap<>();
    private Map<String, Account> accountsByNumber = new HashMap<>();
    private int userCounter = 0;
    private int accountCounter = 0;
    private int transactionCounter = 0;

    public Map<String, User> getUsersById() {
        return usersById;
    }

    public Map<String, String> getUserIdByEmail() {
        return userIdByEmail;
    }

    public Map<String, Account> getAccountsByNumber() {
        return accountsByNumber;
    }

    public String nextUserId() {
        userCounter++;
        return String.format("USR%04d", userCounter);
    }

    public String nextAccountNumber() {
        accountCounter++;
        return String.format("ACC%06d", accountCounter);
    }

    public String nextTransactionId() {
        transactionCounter++;
        return String.format("TXN%06d", transactionCounter);
    }
}
