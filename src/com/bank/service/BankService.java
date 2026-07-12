package com.bank.service;

import com.bank.exception.*;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.util.DataStore;
import com.bank.util.FileManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Central service layer for the Banking Information System. Encapsulates
 * all business rules: registration, login, account management, deposits,
 * withdrawals, transfers and statement generation. Every mutating
 * operation persists the updated state to disk immediately afterwards.
 */
public class BankService {

    private final DataStore store;

    public BankService() {
        this.store = FileManager.load();
    }

    // ---------- Password hashing ----------

    private String hashPassword(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawPassword.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Unable to hash password", e);
        }
    }

    // ---------- User registration & login ----------

    public User registerUser(String name, String address, String phone, String email, String password)
            throws DuplicateUserException, InvalidTransactionException {
        String normalizedEmail = email.trim().toLowerCase();
        if (store.getUserIdByEmail().containsKey(normalizedEmail)) {
            throw new DuplicateUserException("An account with email '" + email + "' already exists.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidTransactionException("Name cannot be empty.");
        }
        if (password == null || password.length() < 4) {
            throw new InvalidTransactionException("Password must be at least 4 characters long.");
        }

        String userId = store.nextUserId();
        User user = new User(userId, name.trim(), address, phone, normalizedEmail, hashPassword(password));
        store.getUsersById().put(userId, user);
        store.getUserIdByEmail().put(normalizedEmail, userId);
        FileManager.save(store);
        return user;
    }

    public User login(String email, String password) throws AuthenticationException {
        String normalizedEmail = email.trim().toLowerCase();
        String userId = store.getUserIdByEmail().get(normalizedEmail);
        if (userId == null) {
            throw new AuthenticationException("No account found with that email address.");
        }
        User user = store.getUsersById().get(userId);
        if (!user.getPasswordHash().equals(hashPassword(password))) {
            throw new AuthenticationException("Incorrect password.");
        }
        return user;
    }

    public void updateUserProfile(String userId, String name, String address, String phone) {
        User user = store.getUsersById().get(userId);
        if (user == null) {
            return;
        }
        if (name != null && !name.trim().isEmpty()) {
            user.setName(name.trim());
        }
        if (address != null && !address.trim().isEmpty()) {
            user.setAddress(address.trim());
        }
        if (phone != null && !phone.trim().isEmpty()) {
            user.setPhone(phone.trim());
        }
        FileManager.save(store);
    }

    // ---------- Account management ----------

    public Account createAccount(String userId, Account.AccountType type, double initialDeposit)
            throws InvalidTransactionException {
        if (initialDeposit < 0) {
            throw new InvalidTransactionException("Initial deposit cannot be negative.");
        }
        User user = store.getUsersById().get(userId);
        if (user == null) {
            throw new InvalidTransactionException("User does not exist.");
        }
        String accountNumber = store.nextAccountNumber();
        Account account = new Account(accountNumber, userId, type, initialDeposit);

        if (initialDeposit > 0) {
            Transaction openingTxn = new Transaction(store.nextTransactionId(), accountNumber,
                    Transaction.Type.DEPOSIT, initialDeposit, initialDeposit, "Opening deposit");
            account.addTransaction(openingTxn);
        }

        store.getAccountsByNumber().put(accountNumber, account);
        user.addAccountNumber(accountNumber);
        FileManager.save(store);
        return account;
    }

    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        Account account = store.getAccountsByNumber().get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("No account found with number '" + accountNumber + "'.");
        }
        return account;
    }

    public List<Account> getAccountsForUser(String userId) {
        List<Account> result = new ArrayList<>();
        User user = store.getUsersById().get(userId);
        if (user == null) {
            return result;
        }
        for (String accNo : user.getAccountNumbers()) {
            Account acc = store.getAccountsByNumber().get(accNo);
            if (acc != null) {
                result.add(acc);
            }
        }
        return result;
    }

    // ---------- Deposit / Withdraw / Transfer ----------

    public void deposit(String accountNumber, double amount) throws AccountNotFoundException, InvalidTransactionException {
        if (amount <= 0) {
            throw new InvalidTransactionException("Deposit amount must be greater than zero.");
        }
        Account account = getAccount(accountNumber);
        double newBalance = round2(account.getBalance() + amount);
        account.setBalance(newBalance);
        account.addTransaction(new Transaction(store.nextTransactionId(), accountNumber,
                Transaction.Type.DEPOSIT, amount, newBalance, "Cash deposit"));
        FileManager.save(store);
    }

    public void withdraw(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidTransactionException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidTransactionException("Withdrawal amount must be greater than zero.");
        }
        Account account = getAccount(accountNumber);
        if (amount > account.getBalance()) {
            throw new InsufficientFundsException(
                    String.format("Insufficient funds. Available balance is %.2f.", account.getBalance()));
        }
        double newBalance = round2(account.getBalance() - amount);
        account.setBalance(newBalance);
        account.addTransaction(new Transaction(store.nextTransactionId(), accountNumber,
                Transaction.Type.WITHDRAWAL, amount, newBalance, "Cash withdrawal"));
        FileManager.save(store);
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, double amount)
            throws AccountNotFoundException, InvalidTransactionException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidTransactionException("Transfer amount must be greater than zero.");
        }
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new InvalidTransactionException("Cannot transfer to the same account.");
        }
        Account fromAccount = getAccount(fromAccountNumber);
        Account toAccount = getAccount(toAccountNumber);

        if (amount > fromAccount.getBalance()) {
            throw new InsufficientFundsException(
                    String.format("Insufficient funds. Available balance is %.2f.", fromAccount.getBalance()));
        }

        double fromNewBalance = round2(fromAccount.getBalance() - amount);
        double toNewBalance = round2(toAccount.getBalance() + amount);

        fromAccount.setBalance(fromNewBalance);
        toAccount.setBalance(toNewBalance);

        fromAccount.addTransaction(new Transaction(store.nextTransactionId(), fromAccountNumber,
                Transaction.Type.TRANSFER_OUT, amount, fromNewBalance, "Transfer to " + toAccountNumber));
        toAccount.addTransaction(new Transaction(store.nextTransactionId(), toAccountNumber,
                Transaction.Type.TRANSFER_IN, amount, toNewBalance, "Transfer from " + fromAccountNumber));

        FileManager.save(store);
    }

    // ---------- Statements ----------

    public List<Transaction> getStatement(String accountNumber) throws AccountNotFoundException {
        return getAccount(accountNumber).getTransactions();
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
