package com.bank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a registered user of the Banking Information System.
 * A user can own one or more accounts.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String passwordHash;
    private List<String> accountNumbers;

    public User(String userId, String name, String address, String phone, String email, String passwordHash) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.passwordHash = passwordHash;
        this.accountNumbers = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<String> getAccountNumbers() {
        return accountNumbers;
    }

    public void addAccountNumber(String accountNumber) {
        this.accountNumbers.add(accountNumber);
    }

    @Override
    public String toString() {
        return String.format("User ID: %s | Name: %s | Email: %s | Phone: %s | Address: %s | Accounts: %d",
                userId, name, email, phone, address, accountNumbers.size());
    }
}
