package com.developer.simpledemo.javatesting.jupiterannotations.bankaccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BankAccountImplTest {

  private static final double INITIAL_BALANCE = 1000D;

  private BankAccount bankAccountImpl;
  private final Faker faker = new Faker();
  private static Logger logger;

  // Initialize ONCE before all test
  @BeforeAll
  static void initAll() {
    System.out.println("Starting tests for BankAccountImpl...");
    // Example: Could be initializing a mock database or other shared resources
    logger = LoggerFactory.getLogger(BankAccountImplTest.class);
  }

  @AfterAll
  static void tearDownAll() {
    System.out.println("All tests completed. Cleaning up resources...");
    // Example: Could be closing a database connection or clearing caches
  }

  @BeforeEach
  void init() {
    System.out.println("Setting up new BankAccount instance...");
    // Initialize before EACH test
    bankAccountImpl = new BankAccountImpl(INITIAL_BALANCE);
    logger.info("Balance: {}", bankAccountImpl.getBalance());
  }

  @AfterEach
  void tearDown() {
    System.out.println("Cleaning up after test...");
    bankAccountImpl = null; // Clear the instance to ensure no shared state
  }

  @DisplayName("Test withdraw amount")
  // Denotes that a method is a test method.
  @Test
  void testWithdrawAmount() {
    double withdrawAmount = generatePositiveRandomAmount((int) INITIAL_BALANCE);
    logger.info("Withdraw amount: {}", withdrawAmount);
    bankAccountImpl.withdraw(withdrawAmount);
    logger.info("Account balance: {}", bankAccountImpl.getBalance());
    assertEquals(INITIAL_BALANCE - withdrawAmount, bankAccountImpl.getBalance());
  }
  
  private Double generatePositiveRandomAmount(int max) {
    return ThreadLocalRandom.current().nextDouble(0, max);
  }
  
  // Used when you want to organize test cases that share a common context
  @Nested
  @DisplayName("Valid basic operations")
  class ValidOperations {

    @DisplayName("Test deposit amount")
    // Denotes that a method is a test method.
    @Test
    void testDepositAmount() {
      // arrange
      double depositAmount = 10D;
      // act
      bankAccountImpl.deposit(depositAmount);
      logger.info("Deposited amount: {}", depositAmount);
      logger.info("Account balance: {}", bankAccountImpl.getBalance());

      // assert
      assertEquals(INITIAL_BALANCE + depositAmount, bankAccountImpl.getBalance());
    }

    @DisplayName("Test withdraw amount")
    // Denotes that a method is a test method.
    @Test
    void testWithdrawAmount() {
      double withdrawAmount = generatePositiveRandomAmount((int) INITIAL_BALANCE);
      logger.info("Withdraw amount: {}", withdrawAmount);
      bankAccountImpl.withdraw(withdrawAmount);
      logger.info("Account balance: {}", bankAccountImpl.getBalance());
      assertEquals(INITIAL_BALANCE - withdrawAmount, bankAccountImpl.getBalance());
    }
  }

  // TODO: Test renaming

  @DisplayName("Parameterized Test demo")
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

  @DisplayName("Repeated Test demo")
  @RepeatedTest(10)
  void testWithdrawal() {
    double withdrawAmount;
    withdrawAmount = faker.number().randomDouble(2, 0L, (int) INITIAL_BALANCE);
      bankAccountImpl.withdraw(withdrawAmount);

    assertEquals(INITIAL_BALANCE - withdrawAmount, bankAccountImpl.getBalance());
  }
  
  @DisplayName("Dynamic Test demo")
  /*
  Dynamic tests are advantageous in scenarios requiring flexibility, such as data-driven testing, iterative validation,
  real-time data handling, combinatorial testing, or robustness checks like error injection(wide range of unexpected, malformed, or edge-case inputs).
  They simplify generating tests for varying inputs, combinations, or external data at runtime, reducing redundancy and improving adaptability.
  Are generated @ runtime
  */
  @TestFactory
  Stream<DynamicTest> dynamicDepositTests() {
    final int size = 10;
    final int min = -2000;
    final int max = 3000;

    // Generate test data
    List<Double> depositAmounts = generateRandomAmounts(size, min, max);
    List<Double> initialBalances = generateRandomAmounts(size, min, max);
    // Create a list containing the sums of corresponding pairs
    List<Double> expectedBalances =
        IntStream.range(0, Math.min(depositAmounts.size(), initialBalances.size()))
            .mapToObj(i -> depositAmounts.get(i) + initialBalances.get(i))
            .toList();
    // Generate dynamic tests
    return IntStream.range(0, size)
        .mapToObj(
            i ->
                DynamicTest.dynamicTest(
                    String.format(
                        "Test deposit with Initial Balance: %.2f, Deposit: %.2f, Expected Balance: %.2f",
                        initialBalances.get(i), depositAmounts.get(i), expectedBalances.get(i)),
                    () -> {

                      // Validate and create BankAccount
                      BankAccountImpl account =
                          createAccountOrValidateFailure(initialBalances.get(i));

                      if (account != null) {
                        System.out.println(account);
                        // Validate deposit behavior
                        validateDeposit(account, depositAmounts.get(i), expectedBalances.get(i));
                      }
                    }));
  }
  

  private List<Double> generateRandomAmounts(int size, int min, int max) {
    return IntStream.range(0, size)
        .mapToObj(i -> ThreadLocalRandom.current().nextDouble(min, max))
        .toList();
  }

  @Nested
  @DisplayName("Invalid basic operations")
  class InvalidOperations {
    private static Logger loggerInvalidOps;
    private BankAccount bankAccountInvalidOps;

    // Initialize ONCE before all test
    @BeforeAll
    static void initAll() {
      System.out.println("Starting tests for BankAccountImpl...");
      // Example: Could be initializing a mock database or other shared resources
      loggerInvalidOps = LoggerFactory.getLogger(BankAccountImplTest.class);
    }
    
    @BeforeEach
    void init() {
      System.out.println("Setting up new BankAccount instance...");
      // Initialize before EACH test
      bankAccountInvalidOps = new BankAccountImpl(INITIAL_BALANCE);
      loggerInvalidOps.info("Balance: {}", bankAccountInvalidOps.getBalance());
    }
    
    @AfterEach
    void tearDown() {
      System.out.println("Cleaning up after test...");
      bankAccountInvalidOps = null; // Clear the instance to ensure no shared state
    }
    
    @DisplayName("Zeroing account")
    @Test
    void testZeroingAccountByMultipleWithdraws() {

      
      
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            while (bankAccountInvalidOps.getBalance() > 0) {

              Double withdraw = generatePositiveRandomAmount(144);
              loggerInvalidOps.info("Withdraw: {}", withdraw);
              bankAccountInvalidOps.withdraw(withdraw);
              loggerInvalidOps.info("Balance: {}", bankAccountInvalidOps.getBalance());
            }
          },
          "Negative deposit should throw exception");
    }
    }

  

  // Helper method to validate initial balance or return null for invalid balance
  private BankAccountImpl createAccountOrValidateFailure(double initialBalance) {
    if (initialBalance <= 0) {

      assertThrows(
          IllegalArgumentException.class,
          () -> new BankAccountImpl(initialBalance),
          "Negative or zero initial balance should throw exception");
      System.out.println("Initial balance cannot be negative: " + initialBalance);
      return null; // Skip remaining logic if initial balance is invalid
    }
    return new BankAccountImpl(initialBalance);
  }

  // Helper method to validate deposit logic
  private void validateDeposit(
      BankAccountImpl account, double depositAmount, double expectedBalance) {
    if (depositAmount < 0) {
      // Assert that an exception is thrown for negative deposits
      assertThrows(
          IllegalArgumentException.class,
          () -> account.deposit(depositAmount),
          "Negative deposit should throw exception");
    } else {
      // Assert that deposit succeeds and balance is updated
      account.deposit(depositAmount);
      assertEquals(
          expectedBalance,
          account.getBalance(),
          String.format("Expected balance mismatch for deposit %.2f", depositAmount));
    }
  }
  
}
