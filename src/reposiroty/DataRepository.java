package reposiroty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import models.Transaction;
import models.TypeOfOperation;
import models.User;

public class DataRepository {

  public DataRepository() {
    feelTheRate();
  }

  public Map<String, Double> changeTheRate(String currency, Double amount) {
    Map<String, Double> result = new HashMap<>();
    getCurrency().put(currency, amount);
    result.put(currency, amount);
    return result;
  }

  public Map<String, Double> addTheCurrency(String currency, Double rate) {
    if (getCurrency().containsKey(currency)) {
      System.out.println("Такая валюта уже есть");
      return Collections.emptyMap();
    }
    if (rate <= 0) {
      System.out.println("Некорректный курс валюты " + currency);
      return Collections.emptyMap();
    }
    getCurrency().put(currency, rate);
    return new HashMap<>(getCurrency());
  }

  public Map<String, Double> deleteTheCurrency(String currency) {
    getCurrency().remove(currency);
    return new HashMap<>(getCurrency());
  }



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

  public Optional<Double> getTheRate(String input) {
    return Optional.ofNullable(currency.getCurrencyRate().get(input));
  }

  public Transaction deposit(User activeUser, Integer accountNumber, double amount, String value) {
    transactions.add(
        new Transaction(activeUser, accountNumber, TypeOfOperation.DEBIT, amount, value));
    return transactions.get(transactions.size() - 1);
  }


  public Transaction withdraw(User activeUser, Integer accountNumber, double amount, String value) {
    transactions.add(
        new Transaction(activeUser, accountNumber, TypeOfOperation.WITHDRAW, amount, value));
    return transactions.get(transactions.size() - 1); // возвращаем последнюю транзакцию
  }

  public Transaction transfer(User activeUser, Integer from, Integer to, double amount,
      String value) {
    transactions.add(
        new Transaction(activeUser, from, to, TypeOfOperation.TRANSFER, amount, value));
    return transactions.get(transactions.size() - 1); // возвращаем последнюю транзакцию
  }


  public Map<String, Double> getCurrency() {
    return currency.getCurrencyRate();
  }

  public List<Transaction> filterByPredicate(Predicate<Transaction> predicate) {
    List<Transaction> list = new ArrayList<>(transactions);
    List<Transaction> result = list.stream()
        .filter(predicate)
        .collect(Collectors.toList());
    return result;
  }

}
