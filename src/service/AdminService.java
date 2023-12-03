package service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import models.Account;
import models.Role;
import models.Transaction;
import models.User;
import reposiroty.DataRepository;
import reposiroty.UserRepository;

public class AdminService {

  private final UserRepository userRepository;
  private final DataRepository dataRepository;

  public AdminService(UserRepository userRepository, DataRepository dataRepository) {
    this.userRepository = userRepository;
    this.dataRepository = dataRepository;
  }

  public boolean changeCurrencyExchange (String currency, Double amount) {
    if (currency.equals("RUB") || amount <= 0) {
      return false;
    }
    dataRepository.changeTheRate(currency, amount);
    return true;
  }

  public boolean addTheCurrency(String currency, Double rate) {
    return !dataRepository.addTheCurrency(currency, rate).isEmpty();
  }

  public boolean deleteTheCurrency(String currency) {
    if(!dataRepository.getCurrency().containsKey(currency)) {
      return false; // проверка курсов на наличие удаляемого
    }
    Set<Account> accountSet = userRepository.getAllUsersAccounts();
    long result = accountSet.stream()
        .filter(account -> account.getCurrency().equals(currency))
        .count();
    if (result != 0) {
      System.out.println("У пользователей есть открытые счета. Закройте их самостоятельно и повторите запрос");
      return false;
    } else {
      System.out.println("Удаляю валюту" + currency);
      System.out.println(dataRepository.deleteTheCurrency(currency));
      return true;
    }
  }

  public List<Transaction> showUsersOperations(int id) {
    if (id < 1) {
      return Collections.emptyList();
    }
    return dataRepository.filterByPredicate(transaction -> transaction.getUser().getId() == id);
  }


  public List<Transaction> showCurrencyOperations(String currency) {
    if(!dataRepository.getCurrency().containsKey(currency)) {
      System.out.println("Такой валюты нет");
      return Collections.emptyList(); // проверка курсов на наличие валюты
    }
    return dataRepository.filterByPredicate(transaction -> transaction.getCurrency().equals(currency));
  }

  public boolean assignCashier(Optional<User> userOptional) {
    if (userOptional.isEmpty()) {
      System.out.println("такого юзера нет!");
      return false;
    }
    User user = userOptional.get();
    if (user.getRole().equals(Role.ADMINISTRATOR)) {
      System.out.println("Нельзя из Администратора сделать кассира!");
      return false;
    }
    user.setRole(Role.CASHOFFICER);
    return true;
  }
}
