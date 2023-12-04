package models;

public class Account {

  private final int accountNumber;

  private String currency;

  private double amount;

  public Account(int accountNumber, String currency, double amount) {
    this.accountNumber = accountNumber;
    this.currency = currency;
    this.amount = amount;
  }

  @Override
  public String toString() {
    return
        " сумма " + amount + " " + currency + "\n";
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

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }
}
