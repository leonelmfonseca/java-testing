package com.developer.simpledemo.javatesting.jupiterannotations.bankaccount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import net.datafaker.Faker;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BankAccountImplTest {

  private static final double INITIAL_BALANCE = 1000D;

  private final BankAccount bankAccountImpl = new BankAccountImpl(INITIAL_BALANCE);
  private final Faker faker = new Faker();

  // Denotes that a method is a test method.
  @Test
  void updatedBalance_EqualToSumOfDepositWithInitialBalance() {
    double depositAmount = 10D;
    bankAccountImpl.deposit(depositAmount);
    assertEquals(INITIAL_BALANCE + depositAmount, bankAccountImpl.getBalance());
  }

  // TODO: Test renaming
  // Parameterized tests make it possible to run a test multiple times with different arguments.
  @ParameterizedTest
  @ValueSource(doubles = {1D, 100D, 200D, 300D, 400D, INITIAL_BALANCE, Double.MAX_VALUE})
  void recordTransactionsTest(double amount) {

    Transaction testTransaction =
        new Transaction(2L, amount, bankAccountImpl.getBalance() + amount, Instant.now());

    bankAccountImpl.deposit(amount);

    Transaction mostRecentTransaction = bankAccountImpl.getMostRecentTransaction();

    assertEquals(testTransaction, mostRecentTransaction);
  }

  // Repeat a test a specified number of times.
  // Each invocation of a repeated test behaves like the execution of a regular @Test method

  @RepeatedTest(10)
  void testWithdrawal() {
    double withdrawAmount;
      withdrawAmount = faker.number().randomDouble(2, 0L, Double.valueOf(INITIAL_BALANCE).longValue());
      bankAccountImpl.withdraw(withdrawAmount);

    assertEquals(INITIAL_BALANCE - withdrawAmount, bankAccountImpl.getBalance());
  }

  // TODO: Remove me
  /*
  @Test
  void testSample(){

    assertTrue(true);
  }

   */
}
