package com.developer.simpledemo.javatesting.jupiterannotations.bankaccount;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.ToString;

@ToString
public class BankAccountImpl implements BankAccount {
    private double balance;
    private final List<Transaction> transactions;

    public BankAccountImpl(double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
        recordTransaction(initialBalance);
    }

    private void recordTransaction(double amount) {
        Transaction transaction = new Transaction(1L + transactions.size(), amount, this.balance, Instant.now());
        transactions.add(transaction);
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
        recordTransaction(amount);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount >balance) {
            throw new IllegalArgumentException("Insufficient balance.");
        }
        balance -= amount;
        recordTransaction(amount);
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public Collection<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public Transaction getMostRecentTransaction() {
        return transactions.stream()
                .max(Comparator.comparing(Transaction::transactionDate))
                .orElseThrow(() -> new IllegalStateException("No transactions found"));
    }
}