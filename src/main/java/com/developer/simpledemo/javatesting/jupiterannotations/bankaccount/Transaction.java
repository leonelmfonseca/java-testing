package com.developer.simpledemo.javatesting.jupiterannotations.bankaccount;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

public record Transaction(
    long id, double amount, double balanceAfterTransaction, Instant transactionDate) {

  private static final ObjectMapper MAPPER = new ObjectMapper();


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Transaction other)) return false; // Pattern matching for cleaner code
    return id == other.id
        && Double.compare(amount, other.amount) == 0
        && Double.compare(balanceAfterTransaction, other.balanceAfterTransaction) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, amount, balanceAfterTransaction);
  }

  @Override
  public String toString() {
    try {
      return MAPPER.writeValueAsString(this); // Use the static ObjectMapper instance
    } catch (IOException e) {
      throw new RuntimeException(
          "Error serializing to JSON", e); // Throw runtime exception for critical failure
    }
  }
}
