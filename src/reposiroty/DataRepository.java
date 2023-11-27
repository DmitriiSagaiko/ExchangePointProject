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

  List<Transaction> transactions = new LinkedList<>();
  Map<Currency, Double> exchangeRateToEur = new HashMap<>();





  private void feelTheRate() {
    //TODO Переделать
    exchangeRateToEur.put(Currency.EUR, 1.0);
    exchangeRateToEur.put(Currency.USD, 0.95);
    exchangeRateToEur.put(Currency.RUB,0.095);
  }

  public double getTheRate(Currency currency) {
    return exchangeRateToEur.get(currency);
  }

  public boolean changeTheRate(Currency currency, double amount) {
    //TODO переделать
    exchangeRateToEur.put(currency, amount);
    return true;
  }

  public List<Transaction> showAllTransactions(int id) {
    return transactions;
  }


  public Transaction deposit(User activeUser, Currency currency, double amount) {
    transactions.add(new Transaction(activeUser,currency, TypeOfTransaction.DEBIT, amount));
    return transactions.get(transactions.size()-1);
  }

  public Transaction withdraw(User activeUser, Currency currency, double amount) {
    transactions.add(new Transaction(activeUser,currency, TypeOfTransaction.WITHDRAW, amount));
    return transactions.get(transactions.size()-1);
  }

  public Optional<String[]> showTheHistory(int typeOfOperation, User activeUser) {
    //TODO в зависимости от типа операции создать 2 доп метода Private и вызывать их
    //история по валюте/ по всем счетам
  }

  public Transaction exchangeCurrency(User activeUser, Currency from, Currency to, double amount) {
    transactions.add(new Transaction(activeUser,from,to,TypeOfTransaction.TRANSFER, amount));
    return transactions.get(transactions.size()-1);
  }
}
