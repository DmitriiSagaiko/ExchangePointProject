package models;

import java.time.LocalDateTime;

public class Transaction {

  private LocalDateTime localDateTime = LocalDateTime.now();

  private User user;

  private double amount;

  private Integer accountFrom;
  private Integer accountTo;

 private TypeOfTransaction type;


  public Transaction(User user, Integer accountFrom, TypeOfTransaction type, double amount) {
    this.user = user;
    this.accountFrom = this.accountFrom;
    this.type = type;
    this.amount = amount;
  }

  public Transaction(User user, Integer accountFrom, Integer accountTo,
      TypeOfTransaction type, double amount) {
    this.user = user;
    this.accountFrom = this.accountFrom;
    this.accountTo = this.accountTo;
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

  public TypeOfTransaction getType() {
    return type;
  }

  public void setType(TypeOfTransaction type) {
    this.type = type;
  }
}
