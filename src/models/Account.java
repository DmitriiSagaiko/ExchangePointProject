package models;

public class Account {

  private final int accountNumber;

  private String currency;

  private int amount;

  public Account(int accountNumber, String currency, int amount) {
    this.accountNumber = accountNumber;
    this.currency = currency;
    this.amount = amount;
  }

  public int getAccountNumber() {
    return accountNumber;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
}
