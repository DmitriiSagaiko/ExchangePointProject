package reposiroty;

import java.util.Map;
import java.util.Optional;
import models.User;

public class UserRepository {

  public Optional<User> registerUser(String name, String password) {
    //TODO
    //выполнять все проверки на логины и пароли/ через отдельные методы. В этих методах предусмотреть выброс ошибок
  }

  public Optional<User> login(String name, String password) {
    //TODO
    //Проверка логина и пароля, если такой юзер есть, вернуть его.
  }

  public Map<Integer, Integer> showTheBalance(User activeUser) {
    //TODO
    return activeUser.getAccounts();
  }

  public Map<Integer, Integer> deposit(User activeUser, String currency, double amount) {
    //TODO
  }

  public Map<Integer, Integer> withdraw(User activeUser, String currency) {
    //TODO
  }

  public Map<Integer, Integer> openNewAccount(User activeUser, String currency, double depositSum) {
    //TODO
  }

  public Map<Integer, Integer> closeAccount(User activeUser, String currency) {
    //TODO. Проверяем все счета, если есть только текущая currency. удаляем ее из мапы
    //если счетов несколько, надо делать пересчет по текущему курсу и удалить из мапы
    //для пересчета сделать отдельный метод.
  }

  public Map<Integer, Integer> exchangeCurrency(User activeUser, String from, String to, double amount) {
    //выполняется проверка на возможность такого обмена
    //TODO. Берется приватный метод пересчета и в мапе меняется значение по двум валютам на величину amount;
  }
}
