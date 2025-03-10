package com.developer.simpledemo.javatesting.jupiterannotations.bankaccount;

import java.util.Collection;

public interface BankAccount {

  void deposit(double amount);

  void withdraw(double amount);
  
  double getBalance();
  
  Collection<Transaction> getTransactions();
  
  Transaction getMostRecentTransaction();
}
