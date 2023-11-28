package reposiroty;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import models.Account;
import models.User;

public class UserRepository {

  public static int counter = 99999;

  public Optional<User> registerUser(String name, String password) {
    //TODO
    //выполнять все проверки на логины и пароли/ через отдельные методы. В этих методах предусмотреть выброс ошибок
    return Optional.empty();
  }

  public Optional<User> login(String name, String password) {
    //TODO
    //Проверка логина и пароля, если такой юзер есть, вернуть его.
    return Optional.empty();
  }

  public Map<Integer, Account> showTheBalance(User activeUser) {
    //TODO
    return activeUser.getAccounts();
  }
//  public Map<Integer, Account> showTheBalanceByCurrency(User activeUser, String currency) {
//    Map<Integer, Account> map  = activeUser.getAccounts();
//    if(map.isEmpty()) {
//      System.out.println("У вас нет счетов!");
//      return Collections.emptyMap();
//    }
//    Map<Integer, Account> result  = new HashMap<>();
//    result.put(currency, map.get(currency));
//
//    return activeUser.getAccounts();
//  }

  public Map<Integer, Account> deposit(User activeUser, Integer accountNumber, double amount) {
    if (isUserHasAccount(activeUser, accountNumber)) {
      Account account = activeUser.getAccounts().get(accountNumber);
      account.setAmount(account.getAmount() + amount);
      activeUser.getAccounts().put(accountNumber, account);
      System.out.println("Счет успешно пополнен на сумму :" + amount);
      System.out.println("На счету осталось:" + account.getAmount() + " " + account.getCurrency());
      return activeUser.getAccounts(); // спросить у сергея что лучше возвращать?
    }
    Account newAccount = openNewAccount(activeUser, "RUB", amount); // создается счет по дефолту в рублях
    activeUser.getAccounts().put(newAccount.getAccountNumber(), newAccount);
    return activeUser.getAccounts(); // спросить у сергея что лучше возвращать?
  }

  public Map<Integer, Account> withdraw(User activeUser, Integer accountNumber, double amount) {
    if ((isUserHasAccount(activeUser, accountNumber))) {
      if (isUserHasEnoughMoney(activeUser, accountNumber, amount)) {
        Account account = activeUser.getAccounts().get(accountNumber);
        account.setAmount(account.getAmount() - amount);
        activeUser.getAccounts().put(accountNumber, account);
        System.out.println("Деньги со счета успешно сняты :" + amount);
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
    if (activeUser.getOneAccount(accountNumber).get(accountNumber).getAmount() <= 1) {
      Account account = activeUser.getAccounts().get(accountNumber);
      activeUser.deleteAccount(accountNumber);
      return account;
    }
    System.out.println("Переведите или снимите все деньги с запрашеваемого счета");
    return null;
  }

//  public Map<Integer, Integer> exchangeCurrency(User activeUser, Integer from, Integer to,
//      double amount) {
//    //выполняется проверка на возможность такого обмена
//    //TODO. Берется приватный метод пересчета и в мапе меняется значение по двум валютам на величину amount;
//  }

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

  private static int getCounter() {
    return counter;
  }

  public Map<Integer, Account> transfer(User activeUser, Integer from, Integer to, double amount) {
    Account fromAcc = activeUser.getAccounts().get(from);
    fromAcc.setAmount(fromAcc.getAmount() - amount);

    Account toAcc = activeUser.getAccounts().get(to);
    toAcc.setAmount(toAcc.getAmount() + amount);

    activeUser.getAccounts().put(from,fromAcc);
    activeUser.getAccounts().put(to,toAcc);

    return new HashMap<>(activeUser.getAccounts());
  }
}
