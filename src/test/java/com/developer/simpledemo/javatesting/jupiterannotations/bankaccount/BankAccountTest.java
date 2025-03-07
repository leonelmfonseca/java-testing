package com.developer.simpledemo.javatesting.jupiterannotations.bankaccount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BankAccountTest {

  private static final double INITIAL_BALANCE = 1000D;

  private final BankAccount bankAccount = new BankAccount(INITIAL_BALANCE);

  
  @Test // Denotes that a method is a test method.
  void depositTest() {
    double depositAmount = 10D;
    bankAccount.deposit(depositAmount);
    assertEquals(INITIAL_BALANCE + depositAmount, bankAccount.getBalance());
  }
}
