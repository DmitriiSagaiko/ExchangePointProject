package models;

import java.time.LocalDateTime;

public class Transaction {

  private LocalDateTime localDateTime = LocalDateTime.now();

  private User user;

  private double amount;

  private String currencyFrom;
  private String currencyTo;

 private TypeOfTransaction type;


  public Transaction(User user, String currencyFrom, TypeOfTransaction type, double amount) {
    this.user = user;
    this.currencyFrom = currencyFrom;
    this.type = type;
    this.amount = amount;
  }

  public Transaction(User user, String currencyFrom, String currencyTo,
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

  public String getCurrencyFrom() {
    return currencyFrom;
  }

  public void setCurrencyFrom(String currencyFrom) {
    this.currencyFrom = currencyFrom;
  }

  public String getCurrencyTo() {
    return currencyTo;
  }

  public void setCurrencyTo(String currencyTo) {
    this.currencyTo = currencyTo;
  }

  public TypeOfTransaction getType() {
    return type;
  }

  public void setType(TypeOfTransaction type) {
    this.type = type;
  }
}
