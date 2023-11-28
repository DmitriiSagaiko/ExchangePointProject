package reposiroty;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import models.Currency;
import models.Transaction;
import models.TypeOfTransaction;
import models.User;

public class DataRepository {

  public static class Currency {

    final Map<String, Double> currencyRate = new HashMap<>();

    //RUB to Currency

    public Map<String, Double> getCurrencyRate() {
      return currencyRate;
    }

  }

  List<Transaction> transactions = new LinkedList<>();

  Currency currency = new Currency();





  private void feelTheRate() {
    currency.currencyRate.put("USD", 95.0);
    currency.currencyRate.put("EUR", 100.0);
    currency.currencyRate.put("RUB", 1.0);
  }

  public double getTheRate(String input) {
    return currency.getCurrencyRate().get(input);
  }

  public boolean changeTheRate(String input, double amount) {
    currency.currencyRate.put(input,amount);
    return true;
  }

  public List<Transaction> showAllTransactions(int id) {
    return transactions;
  }


  public Transaction deposit(User activeUser, String currency, double amount) {
    transactions.add(new Transaction(activeUser,currency, TypeOfTransaction.DEBIT, amount));
    return transactions.get(transactions.size()-1);
  }

  public Transaction withdraw(User activeUser, String currency, double amount) {
    transactions.add(new Transaction(activeUser,currency, TypeOfTransaction.WITHDRAW, amount));
    return transactions.get(transactions.size()-1);
  }

  public Optional<String[]> showTheHistory(int typeOfOperation, User activeUser) {
    //TODO в зависимости от типа операции создать 2 доп метода Private и вызывать их
    //история по валюте/ по всем счетам
  }

  public Transaction exchangeCurrency(User activeUser, String from, String to, double amount) {
    transactions.add(new Transaction(activeUser,from,to,TypeOfTransaction.TRANSFER, amount));
    return transactions.get(transactions.size()-1);
  }

  public Map<String, Double> getCurrency() {
    return currency.getCurrencyRate();
  }
}
