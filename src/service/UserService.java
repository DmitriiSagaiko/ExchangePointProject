package service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import models.Account;
import models.User;
import reposiroty.DataRepository;
import reposiroty.UserRepository;

public class UserService {

  private User activeUser;

  private final UserRepository userRepository;

  private final DataRepository dataRepository;

  public UserService(UserRepository userRepository, DataRepository dataRepository) {
    this.userRepository = userRepository;
    this.dataRepository = dataRepository;
  }


  public Optional<User> userRegistration(String email, String password, String name) {
    return userRepository.registerUser(email, password, name);
  }

  public Optional<User> login(String email, String password) {
    Optional<User> user = userRepository.login(email, password);
    if (user.isPresent()) {
      activeUser = user.get();
      System.out.println("Добро пожаловать " + activeUser.getName());
      return user;
    }
    System.out.println("Вы ввели неверно логин или пароль, либо такого юзера не существует");
    return Optional.empty();
  }

  public Optional<User> logout() {
    if (isActiveUser()) {
      System.out.println("Вышел из пользователя " + activeUser.getName());
      activeUser = null;
      return Optional.empty();
    }
    System.out.println("Пользователь не произвел вход в личный кабинет");
    return Optional.empty();
  }

  public Map<Integer, Account> checkTheBalance() {
    if (isActiveUser()) {
      return activeUser.getAccounts();
    }
    return Collections.emptyMap();
  }
  public Map<Integer, Account> checkTheBalance(Integer accountID) {
    if (isActiveUser()) {
      return activeUser.getOneAccount(accountID);
    }
    return Collections.emptyMap();
  }

  public Map<Integer, Account> deposit(Integer accountNumber, double amount) {
    if (isActiveUser()) {
      String currency = activeUser.getOneAccount(accountNumber).get(accountNumber).getCurrency();
      dataRepository.deposit(activeUser, accountNumber, amount,currency);
      return userRepository.deposit(activeUser, accountNumber, amount);
    }
    return Collections.emptyMap();
  }

  public Map<Integer, Account> withdraw(Integer accountNumber, double amount) {
    if (isActiveUser()) {
      String currency = activeUser.getOneAccount(accountNumber).get(accountNumber).getCurrency();
      dataRepository.withdraw(activeUser, accountNumber, amount, currency);
      return userRepository.withdraw(activeUser, accountNumber,amount);
    }
    return Collections.emptyMap();
  }

  public Map<Integer, Account> transfer(Integer from, Integer to, double amount, String currency) {
    if (amount <= 0  && activeUser.getOneAccount(from).get(from).getAmount() < amount) {
      System.out.println("Нельзя перевести отрицательную сумму или на счету недостаточно средств ");
      return Collections.emptyMap();
    }
    if (isActiveUser()) {
      dataRepository.transfer(activeUser,from,to,amount,currency);
      double rateFrom = dataRepository.getTheRate(currency);
      Map<Integer,Account> map  = activeUser.getOneAccount(to);
      String currencyTo = map.get(to).getCurrency();
      double rateTo = dataRepository.getTheRate(currencyTo);
      return userRepository.transfer(activeUser,from,to,amount, rateFrom, rateTo);
    }
    return Collections.emptyMap();
  }

  public Optional<Account> openNewAccount(String currency,double depositSum) {
    if (isActiveUser() && depositSum>0) {
      return Optional.of(userRepository.openNewAccount(activeUser, currency, depositSum));
    }
    return Optional.empty();
  }

  public Optional<Account> closeAccount(Integer id) {
    Map<Integer, Account> map = getMapOfAccount();
    if (isActiveUser() && map.containsKey(id)) {
      return Optional.of(userRepository.closeAccount(activeUser, id));
    }
    return Optional.empty();
  }

  public Optional<String[]> showTheHistory(int typeOfOperation) {
    if (isActiveUser()) {
      return dataRepository.showTheHistory(typeOfOperation, activeUser);
    }
    return Optional.empty();
  }

//  public Map<Integer, Integer> exchangeCurrency(String from, String to, double amount) {
//    if (isActiveUser()) {
//      dataRepository.exchangeCurrency(activeUser, from, to, amount);
//      return userRepository.exchangeCurrency(activeUser, from, to, amount);
//    }
//    return Collections.emptyMap();
//  }


  private double getCurrencyToEur(String currency) {
    return dataRepository.getTheRate(currency);
  }

//  private Map<String,Double> getOneCurrency(String currency) {
//    userRepository.showTheBalanceByCurrency(activeUser,currency);
//  }

  public void showAllAccountsID() {
    if (isActiveUser()) {
      Map<Integer, Account> map = activeUser.getAccounts();
      if(map.isEmpty()) {
        System.out.println("У вас нет счетов! откройте новый счет");
      } else {
            map.forEach((key, value) -> System.out.println("У Вас есть счета: \n" + key + "\n"));
      }
    }
  }

  public Map<Integer, Account> getMapOfAccount() {
    if (isActiveUser()) {
      return activeUser.getAccounts();
    }
    return Collections.emptyMap();
  }

  private Map<String,Double> getAllCurrencyAndRate () {
    return new HashMap<>(dataRepository.getCurrency());
  }

  public Set<String> getCurrency () {
    Map<String,Double> map = getAllCurrencyAndRate();
    return map.keySet();
  }


  private boolean isActiveUser() {
    if (activeUser == null) {
      System.out.println("Нет активного пользователя. Войдите в систему.");
      return false;
    }
    return true;
  }
}
