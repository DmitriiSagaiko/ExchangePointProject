package service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import models.Currency;
import models.User;
import reposiroty.DataRepository;
import reposiroty.UserRepository;

public class UserService {


  private double usdToEur = getCurrencyToEur(Currency.USD);
  private double rubToEur = 1 / (getCurrencyToEur(Currency.RUB));
  private double rubToUsd = 1 / (usdToEur / rubToEur);

  private User activeUser;

  private final UserRepository userRepository;

  private final DataRepository dataRepository;

  public UserService(UserRepository userRepository, DataRepository dataRepository) {
    this.userRepository = userRepository;
    this.dataRepository = dataRepository;
  }


  public Optional<User> userRegistration(String name, String password) {
    return userRepository.registerUser(name, password);
  }

  public Optional<User> login(String name, String password) {
    Optional<User> user = userRepository.login(name, password);
    if (user.isPresent()) {
      activeUser = user.get();
      return user;
    }
    return Optional.empty();
  }

  public Optional<User> logout() {
    if (isActiveUser()) {
      activeUser = null;
      System.out.println("Вышел из пользователя");
      return Optional.empty();
    }
    return Optional.empty();
  }

  public Map<Integer, Integer> checkTheBalance() {
    if (isActiveUser()) {
      return userRepository.showTheBalance(activeUser);
    }
    return Collections.emptyMap();
  }

  public Map<Integer, Integer> deposit(Currency currency, double amount) {
    if (isActiveUser()) {
      dataRepository.deposit(activeUser, currency, amount);
      return userRepository.deposit(activeUser, currency, amount);
    }
    return Collections.emptyMap();
  }

  public Map<Integer, Integer> withdraw(Currency currency, double amount) {
    if (isActiveUser()) {
      //TODO проверки на реальность суммы
      dataRepository.withdraw(activeUser, currency, amount);
      return userRepository.withdraw(activeUser, currency);
    }
    return Collections.emptyMap();
  }

  public Map<Integer, Integer> openNewAccount(Currency currency, double depositSum) {
    if (isActiveUser()) {
      return userRepository.openNewAccount(activeUser, currency, depositSum);
    }
    return Collections.emptyMap();
  }

  public Map<Integer, Integer> closeAccount(Currency currency) {
    if (isActiveUser()) {
      return userRepository.closeAccount(activeUser, currency);
    }
    return Collections.emptyMap();
  }

  public Optional<String[]> showTheHistory(int typeOfOperation) {
    if (isActiveUser()) {
      return dataRepository.showTheHistory(typeOfOperation, activeUser);
    }
    return Optional.empty();
  }

  public Map<Integer, Integer> exchangeCurrency(Currency from, Currency to, double amount) {
    if (isActiveUser()) {
      dataRepository.exchangeCurrency(activeUser, from, to, amount);
      return userRepository.exchangeCurrency(activeUser, from, to, amount);
    }
    return Collections.emptyMap();
  }


  private double getCurrencyToEur(Currency currency) {
    return dataRepository.getTheRate(currency);
  }


  private boolean isActiveUser() {
    if (activeUser == null) {
      System.out.println("Нет активного пользователя. Войдите в систему.");
      return false;
    }
    return true;
  }
}
