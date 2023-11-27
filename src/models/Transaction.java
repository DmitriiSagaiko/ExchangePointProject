package models;

import java.time.LocalDateTime;

public class Transaction {

  private LocalDateTime localDateTime = LocalDateTime.now();

  private User user;

  private double amount;

  private Currency currencyFrom;
  private Currency currencyTo;

 private TypeOfTransaction type;


  public Transaction(User user, Currency currencyFrom, TypeOfTransaction type, double amount) {
    this.user = user;
    this.currencyFrom = currencyFrom;
    this.type = type;
    this.amount = amount;
  }

  public Transaction(User user, Currency currencyFrom, Currency currencyTo,
      TypeOfTransaction type, double amount) {
    this.user = user;
    this.currencyFrom = currencyFrom;
    this.currencyTo = currencyTo;
    this.type = type;
    this.amount = amount;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public void setLocalDateTime(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Currency getCurrencyFrom() {
    return currencyFrom;
  }

  public void setCurrencyFrom(Currency currencyFrom) {
    this.currencyFrom = currencyFrom;
  }

  public Currency getCurrencyTo() {
    return currencyTo;
  }

  public void setCurrencyTo(Currency currencyTo) {
    this.currencyTo = currencyTo;
  }

  public TypeOfTransaction getType() {
    return type;
  }

  public void setType(TypeOfTransaction type) {
    this.type = type;
  }
}
