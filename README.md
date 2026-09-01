# 🏦 BankCore 

## Banking Information System
A console-based Banking Information System built using **Core Java**.

The project demonstrates how Java concepts such as **Object-Oriented Programming, exception handling, file handling, serialization, and password hashing** can be combined to build a functional banking application.

## Features

- User Registration
- User Login & Authentication
- Savings and Current Accounts
- Account Management
- Deposit Money
- Withdraw Money
- Fund Transfer
- Account Statements
- Profile Updates
- SHA-256 Password Hashing
- Custom Exception Handling
- File-based Data Persistence

## Tech Stack

- Java
- Object-Oriented Programming (OOP)
- Exception Handling
- Java Serialization
- File Handling
- SHA-256 Hashing
- VS Code

## Project Structure

```text
BankCore/
│
├── src/
│   └── com/
│       └── bank/
│           ├── Main.java
│           │
│           ├── model/
│           │   ├── User.java
│           │   ├── Account.java
│           │   └── Transaction.java
│           │
│           ├── exception/
│           │   ├── InsufficientFundsException.java
│           │   ├── InvalidTransactionException.java
│           │   ├── AccountNotFoundException.java
│           │   ├── AuthenticationException.java
│           │   └── DuplicateUserException.java
│           │
│           ├── service/
│           │   └── BankService.java
│           │
│           └── util/
│               ├── DataStore.java
│               └── FileManager.java
│
├── .vscode/
│   ├── launch.json
│   └── settings.json
│
├── README.md
└── .gitignore

