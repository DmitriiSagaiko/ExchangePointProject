package reposiroty;

import exception.EmailValidateException;
import exception.EmailValidator;
import exception.PasswordValidateExcepton;
import exception.PasswordValidator;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import models.Account;
import models.Role;
import models.User;

public class UserRepository {

  public UserRepository() {
    try {
      init();
    } catch (EmailValidateException e) {
      throw new RuntimeException(e);
    } catch (PasswordValidateExcepton e) {
      throw new RuntimeException(e);
    }
  }

  private final Map<Integer, User> users = new HashMap<>();

  public int counter = 99999;
  public int id = 0;

  public User registerUser(String email, String password, String name)
      throws EmailValidateException, PasswordValidateExcepton {
    EmailValidator.validate(email);
    PasswordValidator.validate(password);
    User newUser = new User(name, Role.USER, generateIdForUser(), email, password);
    users.put(newUser.getId(), newUser);
    System.out.println("Новый юзер успешно создан!");
    return newUser;
  }

  public User registerUser(String email, String password, String name, int a)
      throws EmailValidateException, PasswordValidateExcepton { // костыль
    EmailValidator.validate(email);
    PasswordValidator.validate(password);
    User newUser = new User(name, Role.ADMINISTRATOR, generateIdForUser(), email, password);
    users.put(newUser.getId(), newUser);
    System.out.println("Новый юзер успешно создан!");
    return newUser;

  }

  public Optional<User> login(String email, String password) {
    for (User user : users.values()) {
      if (email.equals(user.getEmail()) && password.equals(user.getPassword())) {
        return Optional.of(user);
      }
    }
    return Optional.empty();
  }

  public Map<Integer, Account> showTheBalance(User activeUser) {
    return activeUser.getAccounts();
  }

  public Map<Integer, Account> deposit(User activeUser, Integer accountNumber, double amount) {

    Account account = activeUser.getAccounts().get(accountNumber);
    account.setAmount(account.getAmount() + amount);
    activeUser.getAccounts().put(accountNumber, account);
    System.out.println("Счет успешно пополнен на сумму :" + amount + " " + account.getCurrency());
    System.out.println("На счету осталось:" + account.getAmount() + " " + account.getCurrency());
    return activeUser.getAccounts(); // спросить у сергея что лучше возвращать?

  }

  public Account deposit(User activeUser) {
    Account newAccount = openNewAccount(activeUser, "RUB",
        0); // создается счет по дефолту в рублях
    activeUser.getAccounts().put(newAccount.getAccountNumber(), newAccount);
    return newAccount;
  }


  public Map<Integer, Account> withdraw(User activeUser, Integer accountNumber, double amount) {
    if ((isUserHasAccount(activeUser, accountNumber))) {
      if (isUserHasEnoughMoney(activeUser, accountNumber, amount)) {
        Account account = activeUser.getAccounts().get(accountNumber);
        account.setAmount(account.getAmount() - amount);
        activeUser.getAccounts().put(accountNumber, account);
        System.out.println(
            "Деньги со счета успешно сняты :" + amount + " " + account.getCurrency());
        System.out.println(
            "На счету осталось:" + account.getAmount() + " " + account.getCurrency());
        return activeUser.getAccounts(); // спросить у сергея что лучше возвращать?
      } else {
        System.out.println("Недостаточно денег на счету");
        return Collections.emptyMap();
      }
    }
    System.out.println("Нет такого счета");
    return Collections.emptyMap();
  }

  public Account openNewAccount(User activeUser, String currency, double depositSum) {
    int id = generateAccountNumber();
    Account newAccount = new Account(id, currency, depositSum);
    activeUser.addNewAccount(id, newAccount);
    return newAccount;
  }

  public Account closeAccount(User activeUser, Integer accountNumber) {
    if (activeUser.getOneAccount(accountNumber).get(accountNumber).getAmount()
        <= 1) { // проверка на баланс
      Account account = activeUser.getAccounts().get(accountNumber);
      activeUser.deleteAccount(accountNumber);
      return account;
    }
    System.out.println("Переведите или снимите все деньги с запрашеваемого счета");
    return null;
  }


  private boolean isUserHasAccount(User activeUser, Integer accountNumber) {
    return activeUser.getAccounts().containsKey(accountNumber);
  }

  private boolean isUserHasEnoughMoney(User activeUser, Integer accountNumber, double amount) {
    Account account = activeUser.getAccounts().get(accountNumber);
    return ((account.getAmount() - amount) >= 0) && amount >= 0;
  }

  private int generateAccountNumber() {
    counter++;
    return getCounter();
  }

  private int generateIdForUser() {
    id++;
    return getId();
  }

  private int getCounter() {
    return counter;
  }

  public int getId() {
    return id;
  }

  public Map<Integer, Account> transfer(User activeUser, Integer from, Integer to, double amount,
      double rateFrom, double rateTo) {
    Account fromAcc = activeUser.getAccounts().get(from);
    fromAcc.setAmount(fromAcc.getAmount() - amount);
    String currencyFrom = fromAcc.getCurrency();

    Account toAcc = activeUser.getAccounts().get(to);
    String currencyTo = toAcc.getCurrency();
    if (currencyFrom.equals(currencyTo)) {
      toAcc.setAmount(toAcc.getAmount() + amount); // можно if убрать и оставить только else
    } else {
      toAcc.setAmount(toAcc.getAmount() + amount * rateFrom / rateTo);
    }

    activeUser.getAccounts().put(from, fromAcc);
    activeUser.getAccounts().put(to, toAcc);

    return new HashMap<>(activeUser.getAccounts());
  }

  public Map<Integer, User> getUsers() {
    return users;
  }

  public Set<Account> getAllUsersAccounts() {
    Map<Integer, User> input = new HashMap<>(getUsers());

    Set<Entry<Integer, User>> userSet = input.entrySet();

    Set<Account> accounts = new HashSet<>();

    for (Entry<Integer, User> entry : userSet) {
      Map<Integer, Account> map = entry.getValue().getAccounts();
      accounts.addAll(map.values());
    }
    return accounts;
  }

  private void init() throws EmailValidateException, PasswordValidateExcepton {
    registerUser("user1@mail.ru", "User123$", "Андрей Юзеровских");
    registerUser("user2@mail.ru", "User123$", "Максим Юзеровских");
    registerUser("user3@mail.ru", "User123$", "Антон Юзеровских", 100);
  }
}
