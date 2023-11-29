package models;

import java.time.LocalDateTime;

public class Transaction {

  private LocalDateTime localDateTime = LocalDateTime.now();

  private User user;

  private double amount;

  private Integer accountFrom;
  private Integer accountTo;

  private TypeOfOperation type;

  private String currency;


  public Transaction(User user, Integer accountFrom, TypeOfOperation type, double amount, String currency) {
    this.user = user;
    this.accountFrom = accountFrom;
    this.type = type;
    this.amount = amount;
    this.currency = currency;
  }

  public Transaction(User user, Integer accountFrom, Integer accountTo,
      TypeOfOperation type, double amount, String currency) {
    this.user = user;
    this.accountFrom = accountFrom;
    this.accountTo = accountTo;
    this.type = type;
    this.amount = amount;
    this.currency = currency;
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

  public Integer getAccountFrom() {
    return accountFrom;
  }

  public void setAccountFrom(Integer accountFrom) {
    this.accountFrom = accountFrom;
  }

  public Integer getAccountTo() {
    return accountTo;
  }

  public void setAccountTo(Integer accountTo) {
    this.accountTo = accountTo;
  }

  public TypeOfOperation getType() {
    return type;
  }

  public void setType(TypeOfOperation type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Transaction{" +
        "Date=" + localDateTime.getDayOfMonth() + localDateTime.getMonth() + localDateTime.getYear() + localDateTime.getHour() + localDateTime.getMinute() +
        ", user=" + user.getName() +
        ", amount=" + amount +
        ", accountFrom=" + accountFrom +
        ", accountTo=" + accountTo +
        ", type=" + type +
        ", currency='" + currency + '\'' +
        '}';
  }

  public String getCurrency() {
    return currency;
  }
}
