package reposiroty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import models.Transaction;
import models.TypeOfOperation;
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

//  public boolean changeTheRate(String input, double amount) {
//    currency.currencyRate.put(input,amount);
//    return true;
//  }

  public List<Transaction> showAllTransactions(int id) {
    return transactions;
  }


  public Transaction deposit(User activeUser, Integer accountNumber, double amount, String value) {
    transactions.add(new Transaction(activeUser,accountNumber, TypeOfOperation.DEBIT, amount, value));
    return transactions.get(transactions.size()-1);
  }

  public Transaction withdraw(User activeUser, Integer accountNumber, double amount, String value) {
    transactions.add(new Transaction(activeUser,accountNumber, TypeOfOperation.WITHDRAW, amount, value));
    return transactions.get(transactions.size()-1); // возвращаем последнюю транзакцию
  }

  public Transaction transfer(User activeUser,Integer from, Integer to, double amount, String value) {
    transactions.add(new Transaction(activeUser,from,to,TypeOfOperation.TRANSFER,amount, value));
    return transactions.get(transactions.size()-1); // возвращаем последнюю транзакцию
  }

  public Optional<String[]> showTheHistory(int typeOfOperation, User activeUser) {
    //TODO в зависимости от типа операции создать 2 доп метода Private и вызывать их
    //история по валюте/ по всем счетам
    return Optional.empty();
  }

//  public Transaction exchangeCurrency(User activeUser, Integer from, Integer to, double amount) {
//    transactions.add(new Transaction(activeUser,from,to,TypeOfTransaction.TRANSFER, amount));
//    return transactions.get(transactions.size()-1);
//  }

  public Map<String, Double> getCurrency() {
    return currency.getCurrencyRate();
  }

  private List<Transaction> showTheHistoryByTheCurrency(User activeUser, String currency) {
    List<Transaction> list = new ArrayList<>(transactions);
    List<Transaction> result = list.stream()
        .filter(transaction -> transaction.getUser().equals(activeUser))
        .filter(transaction -> transaction.getCurrency().equals(currency))
        .collect(Collectors.toList());
    return result;
  }

  private List<Transaction> showTheHistoryOfAllAccounts(User activeUser) {
    List<Transaction> list = new ArrayList<>(transactions);
    List<Transaction> result = list.stream()
        .filter(transaction -> transaction.getUser().equals(activeUser))
        .filter(transaction -> transaction.getCurrency().equals(currency))
        .collect(Collectors.toList());
    return result;
  }


}
