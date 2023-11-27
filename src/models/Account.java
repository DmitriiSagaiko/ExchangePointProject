package models;

public class Account {

  private final int accountNumber;

  private Currency currency;

  private int amount;

  public Account(int accountNumber, Currency currency, int amount) {
    this.accountNumber = accountNumber;
    this.currency = currency;
    this.amount = amount;
  }

  public int getAccountNumber() {
    return accountNumber;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
}
