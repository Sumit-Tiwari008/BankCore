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

  text
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



## How It Works

The application starts with a simple console menu:
---- MAIN MENU ----
1. Register
2. Login
3. Exit

After login, users can access their account menu:

---- ACCOUNT MENU ----
1. View My Accounts
2. Open New Account
3. Deposit
4. Withdraw
5. Transfer Funds
6. View Account Statement
7. Update Profile
8. Logout

## How to Run
Prerequisites
Java 17 or later
VS Code
Extension Pack for Java
Run in VS Code
Clone or download the repository.
Open the project folder in VS Code.
Open:
src/com/bank/Main.java
Click the Run button above the main() method.

You can also run the project from the terminal:

mkdir -p bin
javac -d bin $(find src -name "*.java")
java -cp bin com.bank.Main



# Author

## SUMIT TIWARI

Built as a Core Java project while learning and practicing Java development.
