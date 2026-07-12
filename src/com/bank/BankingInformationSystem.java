package com.bank;

import com.bank.exception.*;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.service.BankService;

import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Banking Information System prototype.
 * Presents a console-based menu that drives all the functionality
 * described in the problem statement: registration, login, account
 * management, deposits, withdrawals, transfers and statements.
 */
public class BankingInformationSystem {

    private static final Scanner scanner = new Scanner(System.in);
    private static final BankService bankService = new BankService();

    public static void main(String[] args) {
        printBanner();
        boolean running = true;
        while (running) {
            printWelcomeMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    handleRegistration();
                    break;
                case "2":
                    User loggedInUser = handleLogin();
                    if (loggedInUser != null) {
                        sessionMenu(loggedInUser);
                    }
                    break;
                case "3":
                    running = false;
                    System.out.println("Thank you for using the Banking Information System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
        }
        scanner.close();
    }

    private static void printBanner() {
        System.out.println("=========================================");
        System.out.println("     BANKING INFORMATION SYSTEM (Demo)  ");
        System.out.println("=========================================");
    }

    private static void printWelcomeMenu() {
        System.out.println("\n---- MAIN MENU ----");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    // ---------- Registration & Login ----------

    private static void handleRegistration() {
        System.out.println("\n-- New User Registration --");
        System.out.print("Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password (min 4 characters): ");
        String password = scanner.nextLine();

        try {
            User user = bankService.registerUser(name, address, phone, email, password);
            System.out.println("Registration successful! Your User ID is: " + user.getUserId());

            System.out.print("Would you like to open your first account now? (y/n): ");
            String openNow = scanner.nextLine().trim();
            if (openNow.equalsIgnoreCase("y")) {
                openNewAccount(user);
            }
        } catch (DuplicateUserException | InvalidTransactionException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static User handleLogin() {
        System.out.println("\n-- Login --");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        try {
            User user = bankService.login(email, password);
            System.out.println("Login successful. Welcome, " + user.getName() + "!");
            return user;
        } catch (AuthenticationException e) {
            System.out.println("Login failed: " + e.getMessage());
            return null;
        }
    }

    // ---------- Session (post-login) ----------

    private static void sessionMenu(User user) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n---- ACCOUNT MENU (" + user.getName() + ") ----");
            System.out.println("1. View My Accounts");
            System.out.println("2. Open New Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer Funds");
            System.out.println("6. View Account Statement");
            System.out.println("7. Update Profile");
            System.out.println("8. Logout");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAccounts(user);
                    break;
                case "2":
                    openNewAccount(user);
                    break;
                case "3":
                    handleDeposit(user);
                    break;
                case "4":
                    handleWithdraw(user);
                    break;
                case "5":
                    handleTransfer(user);
                    break;
                case "6":
                    handleStatement(user);
                    break;
                case "7":
                    handleUpdateProfile(user);
                    break;
                case "8":
                    loggedIn = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewAccounts(User user) {
        List<Account> accounts = bankService.getAccountsForUser(user.getUserId());
        if (accounts.isEmpty()) {
            System.out.println("You have no accounts yet. Choose 'Open New Account' from the menu.");
            return;
        }
        System.out.println("\n-- Your Accounts --");
        for (Account acc : accounts) {
            System.out.println(acc);
        }
    }

    private static void openNewAccount(User user) {
        System.out.println("\n-- Open New Account --");
        System.out.println("Account Type: 1) Savings  2) Current");
        System.out.print("Choose type: ");
        String typeChoice = scanner.nextLine().trim();
        Account.AccountType type = typeChoice.equals("2") ? Account.AccountType.CURRENT : Account.AccountType.SAVINGS;

        System.out.print("Initial deposit amount: ");
        double initialDeposit = readDouble();

        try {
            Account account = bankService.createAccount(user.getUserId(), type, initialDeposit);
            System.out.println("Account created successfully! Your new account number is: " + account.getAccountNumber());
        } catch (InvalidTransactionException e) {
            System.out.println("Could not open account: " + e.getMessage());
        }
    }

    private static void handleDeposit(User user) {
        String accountNumber = selectOwnAccount(user, "deposit into");
        if (accountNumber == null) return;

        System.out.print("Enter deposit amount: ");
        double amount = readDouble();
        try {
            bankService.deposit(accountNumber, amount);
            Account acc = bankService.getAccount(accountNumber);
            System.out.printf("Deposit successful. Amount: %.2f | New Balance: %.2f%n", amount, acc.getBalance());
        } catch (AccountNotFoundException | InvalidTransactionException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    private static void handleWithdraw(User user) {
        String accountNumber = selectOwnAccount(user, "withdraw from");
        if (accountNumber == null) return;

        System.out.print("Enter withdrawal amount: ");
        double amount = readDouble();
        try {
            bankService.withdraw(accountNumber, amount);
            Account acc = bankService.getAccount(accountNumber);
            System.out.printf("Withdrawal successful. Amount: %.2f | New Balance: %.2f%n", amount, acc.getBalance());
        } catch (AccountNotFoundException | InvalidTransactionException | InsufficientFundsException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
    }

    private static void handleTransfer(User user) {
        String fromAccount = selectOwnAccount(user, "transfer from");
        if (fromAccount == null) return;

        System.out.print("Enter recipient account number: ");
        String toAccount = scanner.nextLine().trim();
        System.out.print("Enter transfer amount: ");
        double amount = readDouble();

        try {
            bankService.transfer(fromAccount, toAccount, amount);
            Account from = bankService.getAccount(fromAccount);
            Account to = bankService.getAccount(toAccount);
            System.out.println("Transfer successful!");
            System.out.printf("Your account (%s) new balance: %.2f%n", from.getAccountNumber(), from.getBalance());
            System.out.printf("Recipient account (%s) new balance: %.2f%n", to.getAccountNumber(), to.getBalance());
        } catch (AccountNotFoundException | InvalidTransactionException | InsufficientFundsException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    private static void handleStatement(User user) {
        String accountNumber = selectOwnAccount(user, "view the statement for");
        if (accountNumber == null) return;

        try {
            List<Transaction> transactions = bankService.getStatement(accountNumber);
            System.out.println("\n-- Account Statement: " + accountNumber + " --");
            if (transactions.isEmpty()) {
                System.out.println("No transactions yet.");
                return;
            }
            System.out.printf("%-14s | %-8s | %-12s | %10s | %10s | %s%n",
                    "Date/Time", "TxnID", "Type", "Amount", "Balance", "Description");
            System.out.println("-".repeat(90));
            for (Transaction t : transactions) {
                System.out.println(t);
            }
        } catch (AccountNotFoundException e) {
            System.out.println("Could not fetch statement: " + e.getMessage());
        }
    }

    private static void handleUpdateProfile(User user) {
        System.out.println("\n-- Update Profile -- (leave blank to keep current value)");
        System.out.print("Name [" + user.getName() + "]: ");
        String name = scanner.nextLine();
        System.out.print("Address [" + user.getAddress() + "]: ");
        String address = scanner.nextLine();
        System.out.print("Phone [" + user.getPhone() + "]: ");
        String phone = scanner.nextLine();

        bankService.updateUserProfile(user.getUserId(), name, address, phone);
        System.out.println("Profile updated successfully.");
    }

    // ---------- Helpers ----------

    private static String selectOwnAccount(User user, String actionDescription) {
        List<Account> accounts = bankService.getAccountsForUser(user.getUserId());
        if (accounts.isEmpty()) {
            System.out.println("You have no accounts yet. Open one first from the account menu.");
            return null;
        }
        if (accounts.size() == 1) {
            return accounts.get(0).getAccountNumber();
        }
        System.out.println("Which account would you like to " + actionDescription + "?");
        for (Account acc : accounts) {
            System.out.println(acc);
        }
        System.out.print("Enter account number: ");
        return scanner.nextLine().trim();
    }

    private static double readDouble() {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number, please try again: ");
            }
        }
    }
}
