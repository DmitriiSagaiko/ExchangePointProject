package service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import models.Account;
import models.Role;
import models.Transaction;
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
    if (isActiveUser()) {
      System.out.println("Вы уже залогинены! Сначала выйдете из аккаунта");
      return Optional.empty();
    }
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
      Optional<User> result = Optional.ofNullable(activeUser);
      activeUser = null;
      return result;
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
    if (amount <= 0) {
      System.out.println("Нельзя внести отрицательную и нулевую сумму!");
      return Collections.emptyMap();
    }
    if (isActiveUser()) {
      if (activeUser.getOneAccount(accountNumber).get(accountNumber) == null) {
        Account created = userRepository.deposit(activeUser);
        dataRepository.deposit(activeUser, created.getAccountNumber(), amount,
            created.getCurrency());
        return userRepository.deposit(activeUser, created.getAccountNumber(), amount);
      }
      String currency = activeUser.getOneAccount(accountNumber).get(accountNumber).getCurrency();
      dataRepository.deposit(activeUser, accountNumber, amount, currency);
      return userRepository.deposit(activeUser, accountNumber, amount);
    }
    return Collections.emptyMap();
  }

  public Map<Integer, Account> withdraw(Integer accountNumber, double amount) {
    if (!isActiveUser()) {
      return Collections.emptyMap();
    }
    if (amount <= 0) {
      System.out.println("Нельзя снять отрицательную и нулевую сумму!");
      return Collections.emptyMap();
    }
    if (activeUser.getOneAccount(accountNumber).get(accountNumber) == null) {
      System.out.println("Нет такого счета. Попробуйте еще раз через с другой");
      return Collections.emptyMap();
    }
    String currency = activeUser.getOneAccount(accountNumber).get(accountNumber).getCurrency();
    dataRepository.withdraw(activeUser, accountNumber, amount, currency);
    return userRepository.withdraw(activeUser, accountNumber, amount);
  }

  public Map<Integer, Account> transfer(Integer from, Integer to, double amount) {
    if (!isActiveUser()) {
      return Collections.emptyMap();
    }
    if (amount <= 0) {
      System.out.println("Нельзя перевести отрицательную сумму");
      return Collections.emptyMap();
    }
    if (activeUser.getOneAccount(from).get(from) == null) {
      System.out.println("Нет такого счета отправления. Попробуйте еще раз через с другой");
      return Collections.emptyMap();
    }
    if (activeUser.getOneAccount(to).get(to) == null) {
      System.out.println("Нет такого счета получения. Попробуйте еще раз через с другой");
      return Collections.emptyMap();
    }
    if (activeUser.getOneAccount(from).get(from).getAmount() < amount) {
      System.out.println("Недостаточно средств на балансе");
      return Collections.emptyMap();
    }

    String currency = activeUser.getOneAccount(from).get(from).getCurrency();
    dataRepository.transfer(activeUser, from, to, amount, currency);
    Optional<Double> rateFrom = dataRepository.getTheRate(currency);

    String currencyTo = activeUser.getOneAccount(to).get(to).getCurrency();
    Optional<Double> rateTo = dataRepository.getTheRate(currencyTo);

    return userRepository.transfer(activeUser, from, to, amount, rateFrom.get(), rateTo.get());
  }

  public Optional<Account> openNewAccount(String currency, double depositSum) {
    if (isActiveUser() && depositSum > 0) {
      if (dataRepository.getCurrency().containsKey(currency)) {

        Account account = userRepository.openNewAccount(activeUser, currency, depositSum);
        dataRepository.deposit(activeUser, account.getAccountNumber(), depositSum, currency);
        return Optional.of(account);
      } else {
        System.out.println("Такой валюты в банке нет!");
        return Optional.empty();
      }
    }
    return Optional.empty();
  }

  public Optional<Account> closeAccount(Integer id) {
    Map<Integer, Account> map = getMapOfAccount();
    if (isActiveUser() && map.containsKey(id)) {
      return Optional.ofNullable(userRepository.closeAccount(activeUser, id));
    }
    return Optional.empty();
  }

  public List<Transaction> showTheHistory(int typeOfOperation, String currency) {
    if (!isActiveUser()) {
      return Collections.emptyList();
    }
    if (typeOfOperation == 1) {
      Predicate<Transaction> predicate1 = transaction -> transaction.getUser().equals(activeUser);
      Predicate<Transaction> predicate2 = transaction -> transaction.getCurrency().equals(currency);
      Predicate<Transaction> predicate = predicate1.and(predicate2);
      return dataRepository.filterByPredicate(predicate);
    } else if (typeOfOperation == 2) {
      return dataRepository.filterByPredicate(
          transaction -> transaction.getUser().equals(activeUser));
    }
    return Collections.emptyList();
  }


  public List<Transaction> showTheHistory(int typeOfOperation) {
    if (isActiveUser()) {
      Predicate<Transaction> predicate = transaction -> transaction.getUser().equals(activeUser);
      return dataRepository.filterByPredicate(predicate);
    }
    return Collections.emptyList();
  }


  public boolean showAllAccountsID() {
    if (isActiveUser()) {
      Map<Integer, Account> map = activeUser.getAccounts();
      if (map.isEmpty()) {
        System.out.println("У вас нет счетов! откройте новый счет");
        return false;
      } else {
        map.forEach((key, value) -> System.out.println("У Вас есть счета: \n" + key + "\n"));
        return true;
      }
    }
    return false;
  }

  public Map<Integer, Account> getMapOfAccount() {
    if (isActiveUser()) {
      return activeUser.getAccounts();
    }
    return Collections.emptyMap();
  }

  public Map<String, Double> getAllCurrencyAndRate() {
    return new HashMap<>(dataRepository.getCurrency());
  }

  public Set<String> getCurrency() {
    Map<String, Double> map = getAllCurrencyAndRate();
    return map.keySet();
  }

  public Map<Integer, User> getUsers() {
    return userRepository.getUsers();
  }


  public boolean isAdministrator() {
    if (isActiveUser()) {
      return activeUser.getRole().equals(Role.ADMINISTRATOR);
    }
    return false;
  }


  public boolean isActiveUser() {
    if (activeUser == null) {
      System.out.println("Нет активного пользователя. Войдите в систему.");
      return false;
    }
    return true;
  }


}
