import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import models.Role;
import models.Transaction;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reposiroty.DataRepository;
import reposiroty.UserRepository;
import service.AdminService;
import service.UserService;

public class TestUserService {

  UserRepository userRepository = new UserRepository();
  DataRepository dataRepository = new DataRepository();
  AdminService adminService = new AdminService(userRepository, dataRepository);
  UserService userService = new UserService(userRepository, dataRepository);


  @Test
  @BeforeEach
  void setUp() {
    userService.logout();
    userService.login("user3@mail.ru", "User123$");
  }



  //UserLogin()
  /*
  плохие
  Вводим несуществующий email и сущ пароль, несуществующий пароль и сущ емайл и полностью не существующий емейл и пароль - получаем опшинал пустой
  Хорошие: вводим юзера 1 - получаем Оптинал не пустой
   */

  @Test
  void testLoginWithWrongEmail() {
    assertTrue(userService.login("fake@email.com", "User123$").isEmpty());
  }

  @Test
  void testLoginWithWrongPassword() {
    assertTrue(userService.login("user3@mail.ru", "fakepassword").isEmpty());
  }

  @Test
  void testLoginWithWrongEmailAndPassword() {
    assertTrue(userService.login("fake@email.com", "fakepassword").isEmpty());
  }

  @Test
  void testLoginWithActiveUser() {
    assertTrue(userService.login("user3@mail.ru", "User123$").isEmpty());
  }

  @Test
  void testLogin() {
    userService.logout();
    Optional<User> user = userService.login("user3@mail.ru", "User123$");
    assertEquals("Антон Юзеровских", user.get().getName());
    userService.logout();
    user = userService.login("user1@mail.ru", "User123$");
    assertEquals("Андрей Юзеровских", user.get().getName());
    userService.logout();
    user = userService.login("user2@mail.ru", "User123$");
    assertEquals("Максим Юзеровских", user.get().getName());
  }

  //logout

  /*
   плохие - выходим из учетки не вошедшой - получаем оптионал пустой
   хорошие - выходим из учетки и проверяем isActiveUser  До и после. Проверим так же по пользователю который вышел
   */


  @Test
  void logoutEmptyUser() {
    userService.logout();
    assertTrue(userService.logout().isEmpty());
  }

  @Test
  void logout() {
    boolean before = userService.isActiveUser();
    assertTrue(before);
    Optional<User> userOptional = userService.logout();
    assertFalse(userService.isActiveUser());
    String email = userOptional.get().getEmail();
    assertEquals("user3@mail.ru", email);
  }

  //checkTheBalance ()

  /*
  Плохой - нет юзера - пустая мапа
  Хорошие: есть юзер без операций - пустая мапа.
  Берется юзер, делаются операции, проверяется баланс по полям мапы.
  //checkTheBalance (int id ) - проверяется, что мапа будет либо пустая, либо ее размер равен 1

   */
  @Test
  void testCheckTheBalanceWithoutUser() {
    userService.logout();
    assertTrue(userService.checkTheBalance().isEmpty());
  }

  @Test
  void testCheckTheBalanceWithoutOperations() {
    assertTrue(userService.checkTheBalance().isEmpty());
  }

  @Test
  void testCheckTheBalance() {
    userService.openNewAccount("EUR", 1000);
    userService.openNewAccount("RUB", 100000);
    userService.openNewAccount("USD", 500);
    userService.deposit(100000, 150);
    userService.deposit(100001, 100000);
    userService.deposit(100002, 500);
    double value = userService.checkTheBalance().get(100000).getAmount();
    assertEquals(1150, value);
    value = userService.checkTheBalance().get(100001).getAmount();
    assertEquals(200000, value);
    value = userService.checkTheBalance().get(100002).getAmount();
    assertEquals(1000, value);
  }

  @Test
  void testCheckTheBalanceOfCount() {
    assertEquals(null, userService.checkTheBalance(100000).get(100000));
    userService.openNewAccount("EUR", 1000);
    assertEquals(1000, userService.checkTheBalance().get(100000).getAmount());
  }

  //deposit()

  /*
  Плохие . 1 нет юзера - пустая мапа.
           2 Вносим отрицательную или нулевую сумму - получаем пустую мапу.Проверяем баланс

  Хорошие: Полполнить существубщий счет. Проверить, что сумма на этом счету увеличилась.Проверяем баланс

  Пополнить несуществующий счет - проверить, что создался новый счет в рублях с указанной суммой.Проверяем баланс
   */
  @Test
  void testDepositWithoutUser() {
    logout();
    assertTrue(userService.deposit(1000, 500).isEmpty());
  }

  @Test
  void testDepositNegativeAmount() {
    userService.openNewAccount("EUR", 1000);
    userService.deposit(100000, 0);
    double value = userService.checkTheBalance().get(100000).getAmount();
    assertEquals(1000, value);
    userService.deposit(100000, -1000);
    value = userService.checkTheBalance().get(100000).getAmount();
    assertEquals(1000, value);
  }

  @Test
  void testDepositWithActiveAcc() {
    userService.openNewAccount("EUR", 1000);
    userService.deposit(100000, 1000);
    double value = userService.checkTheBalance().get(100000).getAmount();
    assertEquals(2000, value);
  }

  @Test
  void testDepositWithNotActiveAcc() {
    userService.deposit(100000, 1000);
    double value = userService.checkTheBalance().get(100000).getAmount();
    assertEquals(1000, value);
    assertEquals("RUB", userService.checkTheBalance().get(100000).getCurrency());
  }

  //withdraw()

  /*
  Плохие . 1 нет юзера - пустая мапа. Проверяем баланс
           2 Снимаем отрицательную или нулевую сумму - получаем пустую мапу.Проверяем баланс
           3. Снимаем со счета большую сумму, которую нельзя снять - получаем пустую мапу. Проверяем баланс
           4. Снимаем с несуществующего счета сумму - получаем пустую мапу

  Хорошие: Снимаем какую-то сумму. Проверить, что сумма на этом счету уменьшилась. Проверяем баланс
   */

  @Test
  void testWithdrawWithoutUser() {
    userService.logout();
    assertTrue(userService.withdraw(100000, 100).isEmpty());
  }

  @Test
  void testWithdrawWithInvalidAmonut() {
    userService.openNewAccount("EUR", 1000);
    assertTrue(userService.withdraw(100000, 0).isEmpty());
    assertTrue(userService.withdraw(100000, -100).isEmpty());
  }

  @Test
  void testWithdrawWithBigAmount() {
    userService.openNewAccount("EUR", 1000);
    assertTrue(userService.withdraw(100000, 1000.01).isEmpty());
    assertEquals(1000, userService.checkTheBalance(100000).get(100000).getAmount());
  }

  @Test
  void testWithdrawWithInvalidAcc() {
    userService.openNewAccount("EUR", 1000);
    assertTrue(userService.withdraw(200000, 900).isEmpty());
    assertEquals(1000, userService.checkTheBalance(100000).get(100000).getAmount());
  }

  @Test
  void testWithdraw() {
    userService.openNewAccount("EUR", 1000);
    userService.withdraw(100000, 900);
    assertEquals(100, userService.checkTheBalance(100000).get(100000).getAmount());
  }

  //transfer()

  /*
    Плохие
    1. Переводим отриц сумму с нормальных счетов - получаем пустую мапу. Проверяем что баланс не изменился.
    2. Переводим сумму с несущ счета - получаем пустую мапу. Проверяем что баланс не изменился.
    3. Переводим сумму на несущ счет - получаем пустую мапу. Проверяем что баланс не изменился.

    Хороший
    Переводим с сущ счет на сущ счет в одной валюте. Проверяем балансы на обоих счетах.
    Переводим с сущ счет на сущ счет в разных валютах. Проверяем балансы на обоих счетах.
   */
  @Test
  void testTransferNegativeAmount() {
    userService.openNewAccount("EUR", 1000);
    userService.openNewAccount("EUR", 1000);
    assertTrue(userService.transfer(100000, 100001, -500).isEmpty());
    assertEquals(1000, userService.checkTheBalance(100000).get(100000).getAmount());
    assertEquals(1000, userService.checkTheBalance(100001).get(100001).getAmount());
  }

  @Test
  void testTransferFromInvalidAcc() {
    userService.openNewAccount("EUR", 1000);
    userService.openNewAccount("EUR", 1000);
    assertTrue(userService.transfer(200000, 100001, 500).isEmpty());
    assertEquals(1000, userService.checkTheBalance(100000).get(100000).getAmount());
    assertEquals(1000, userService.checkTheBalance(100001).get(100001).getAmount());
  }

  @Test
  void testTransferToInvalidAcc() {
    userService.openNewAccount("EUR", 1000);
    userService.openNewAccount("EUR", 1000);
    assertTrue(userService.transfer(100000, 200001, 500).isEmpty());
    assertEquals(1000, userService.checkTheBalance(100000).get(100000).getAmount());
    assertEquals(1000, userService.checkTheBalance(100001).get(100001).getAmount());
  }

  @Test
  void testTransferSameCurrency() {
    userService.openNewAccount("EUR", 1000);
    userService.openNewAccount("EUR", 1000);
    assertFalse(userService.transfer(100000, 100001, 500).isEmpty());
    assertEquals(500, userService.checkTheBalance(100000).get(100000).getAmount());
    assertEquals(1500, userService.checkTheBalance(100001).get(100001).getAmount());
    assertEquals("EUR", userService.checkTheBalance(100001).get(100001).getCurrency());
  }

  @Test
  void testTransferDiffCurrency() {
    userService.openNewAccount("EUR", 1000);
    userService.openNewAccount("RUB", 1000);
    assertFalse(userService.transfer(100000, 100001, 500).isEmpty());
    assertEquals(500, userService.checkTheBalance(100000).get(100000).getAmount());
    assertEquals(51000, userService.checkTheBalance(100001).get(100001).getAmount());
    assertEquals("RUB", userService.checkTheBalance(100001).get(100001).getCurrency());
  }

  //openNewAccount()
  /*
  Плохие - открываем счет в несуществующей валюте - получаем пустой счет. Количество счетов не должно измениться.
  Открываем счет со стартовым капиталом меньше 0  - получаем пустую счет. Количество счетов не должно измениться.

  Хорошая - открыть счет в евро-долларах-рублях и проверить что они появились в счетах клиента. проверить суммы и валюты.
   */
  @Test
  void testOpenNewAccountFakeCurrency() {
    assertTrue(userService.openNewAccount("RSD", 1000).isEmpty());
    assertTrue(userService.checkTheBalance().isEmpty());
  }

  @Test
  void testOpenNewAccountFakeAmount() {
    assertTrue(userService.openNewAccount("EUR", 0).isEmpty());
    assertTrue(userService.openNewAccount("EUR", -1).isEmpty());
    assertTrue(userService.checkTheBalance().isEmpty());
  }

  @Test
  void testOpenNewAccount() {
    userService.openNewAccount("EUR", 1000);
    userService.openNewAccount("RUB", 100000);
    userService.openNewAccount("USD", 5000);
    assertEquals("EUR", userService.checkTheBalance(100000).get(100000).getCurrency());
    assertEquals(1000, userService.checkTheBalance(100000).get(100000).getAmount());

    assertEquals("RUB", userService.checkTheBalance(100001).get(100001).getCurrency());
    assertEquals(100000, userService.checkTheBalance(100001).get(100001).getAmount());

    assertEquals("USD", userService.checkTheBalance(100002).get(100002).getCurrency());
    assertEquals(5000, userService.checkTheBalance(100002).get(100002).getAmount());
  }

  //closeNewAccount()
  /*
  Плохие - закрываем счет с несущтсв id  - получаем пустой счет. Количество счетов не должно измениться.
  закрываем счет с балансом > 1  - получаем пустой счет. Количество счетов не должно измениться.

  хорошие: закрываем счет с балансом 0 в долларах. Проверяем, что количество счетов изменилось и теперь данного счета нет у клиента.
   */
  @Test
  void closeAccountFakeID() {
    userService.openNewAccount("EUR", 1000);
    userService.openNewAccount("RUB", 100000);
    userService.openNewAccount("USD", 5000);
    assertTrue(userService.closeAccount(200000).isEmpty());
    assertTrue(userService.closeAccount(200001).isEmpty());
    assertTrue(userService.closeAccount(200002).isEmpty());
    assertEquals(3, userService.checkTheBalance().size());
  }

  @Test
  void closeAccountWithAmount() {
    userService.openNewAccount("EUR", 1000);
    userService.openNewAccount("RUB", 100000);
    userService.openNewAccount("USD", 5000);
    assertTrue(userService.closeAccount(100000).isEmpty());
    assertTrue(userService.closeAccount(100001).isEmpty());
    assertTrue(userService.closeAccount(100002).isEmpty());
    assertEquals(3, userService.checkTheBalance().size());
  }

  @Test
  void closeAccount() {
    userService.openNewAccount("EUR", 1000);
    userService.openNewAccount("RUB", 100000);
    userService.openNewAccount("USD", 5000);
    userService.withdraw(100000, 1000);
    userService.withdraw(100001, 100000);
    userService.withdraw(100002, 5000);
    assertTrue(userService.closeAccount(100000).isPresent());
    assertTrue(userService.closeAccount(100001).isPresent());
    assertTrue(userService.closeAccount(100002).isPresent());
    assertEquals(0, userService.checkTheBalance().size());
  }
  //showTheHistory()

  /*
  Плохие: некорректный тип операции ( не 1 и не 2) - пустой list
  некорретный вид валюты - пустой лист

  Хороший :сделать несколько операций у пользователя с долларом.( тип 1 - валюта доллар) - список операций пользователя по доллару
  Хороший :сделать несколько операций у пользователя с разными валютами.( тип 2) - список операций пользователя по всем операциям
   */
  @Test
  void showTheHistoryWrongButton() {
    assertTrue(userService.showTheHistory(3).isEmpty());
    ;
  }

  @Test
  void showTheHistoryWrongCurrency() {
    assertTrue(userService.showTheHistory(1, "RSD").isEmpty());
    ;
  }

  @Test
  void showTheHistoryByOneCurrency() {
    userService.openNewAccount("EUR", 1000);
    userService.withdraw(100000, 500);
    userService.withdraw(100000, 200);
    userService.deposit(100000, 1700);
    List<Transaction> list = userService.showTheHistory(1, "EUR");
    assertEquals(4, userService.showTheHistory(1, "EUR").size());
    for (Transaction transaction : list) {
      assertEquals("EUR", transaction.getCurrency());
    }
  }

  @Test
  void showTheHistoryByDiffCurrency() {
    userService.openNewAccount("EUR", 1000);
    userService.withdraw(100000, 500);
    userService.withdraw(100000, 200);
    userService.deposit(100000, 1700);

    userService.openNewAccount("RUB", 100000);
    userService.withdraw(100001, 50000);
    userService.withdraw(100001, 50000);
    userService.deposit(100001, 20000);
    List<Transaction> list = userService.showTheHistory(1, "EUR");
    assertEquals(4, userService.showTheHistory(1, "EUR").size());
    for (Transaction transaction : list) {
      assertEquals("EUR", transaction.getCurrency());
    }

    list = userService.showTheHistory(1, "RUB");
    assertEquals(4, userService.showTheHistory(1, "RUB").size());
    for (Transaction transaction : list) {
      assertEquals("RUB", transaction.getCurrency());
    }

    list = userService.showTheHistory(2);
    assertEquals(8, userService.showTheHistory(2).size());
  }
  //showAllaccountsOfUSer()

  //плохой  - нет счетов - результат false. Проверка - взять юзера и getAccounts() - должны быть тоже пустой
  //Хороший - открыть несколько счетов в разных валютах. Результат true.  взять юзера и getAccounts() - должны быть c этими счетами
  @Test
  void showAllAccountUserFake() {
    assertFalse(userService.showAllAccountsID());
    assertEquals(0, userService.getMapOfAccount().size());
  }

  @Test
  void showAllAccountUser() {
    userService.openNewAccount("EUR", 1000);
    userService.openNewAccount("USD", 2000);
    assertTrue(userService.showAllAccountsID());
    assertEquals(2, userService.getMapOfAccount().size());
    assertEquals("EUR", userService.getMapOfAccount().get(100000).getCurrency());
    assertEquals("USD", userService.getMapOfAccount().get(100001).getCurrency());
  }

  //getMapOFAccount
  // - счетов нет  - мапа пустая
  // добавить счет - размер мапы = 1
  @Test
  void getMapOfAccountEmpty() {
    assertTrue(userService.getMapOfAccount().isEmpty());
  }

  @Test
  void getMapOfAccount() {
    userService.openNewAccount("EUR", 1000);
    userService.openNewAccount("USD", 2000);
    assertEquals(2, userService.getMapOfAccount().size());
  }
  //getCurrencyAndRate
  //Получить ее - размер 3, в ней 3 валюты USD,RUB,EUR с заданными курсами.
  //добавить валюту - и в новой мапе размер 4 с заданными курсом.
  //удалить добавленную валюту - будет размер 3, с остальными валютами ничего не произойдет.

  @Test
  void testGetCurrencyAndRate() {
    Map<String, Double> map = userService.getAllCurrencyAndRate();
    assertEquals(3, map.size());
    assertTrue(map.containsKey("EUR"));
    assertTrue(map.containsKey("USD"));
    assertTrue(map.containsKey("RUB"));
    adminService.addTheCurrency("RSD", 0.84);
    map = userService.getAllCurrencyAndRate();
    assertEquals(4, map.size());
    assertTrue(map.containsKey("RSD"));
  }

  //getCurrency
  // Получить Set и проверить что все 3 валюты там есть.
  @Test
  void testGetCurrency() {
    Set<String> stringSet = userService.getCurrency();
    assertEquals(3, stringSet.size());
    assertTrue(stringSet.contains("EUR"));
    assertTrue(stringSet.contains("USD"));
    assertTrue(stringSet.contains("RUB"));
  }
  //getUsers

  /*
  получить список юзеров, проверить что есть все 3 юзера.
  Добавить сюда нового пользователя.
  Проверить, что пользователь добавле - пробежаться по всем его полям в новой мапе юзеров.
   */

  @Test
  void testGetUser() {
      Map<Integer,User> map = userService.getUsers();
    assertEquals(3, map.size());
    Optional<User> userOptional = userService.userRegistration("dmitsag@gmail.com", "QwerTY1$", "Dmitrii Sagaiko");
    User user = userOptional.get();
    map = userService.getUsers();
    assertEquals(4, map.size());
  }

  //isAdministrator

  @Test
  void testIsAdministrator() {
    Map<Integer,User> map = userService.getUsers();
    assertNotEquals(map.get(1).getRole(), Role.ADMINISTRATOR);
    map.get(2).setRole(Role.CASHOFFICER);
    assertNotEquals(map.get(2).getRole(), Role.ADMINISTRATOR);
    assertEquals(map.get(3).getRole(), Role.ADMINISTRATOR);

  }
  // Берется юзер1 и его роль.  - false
  // задается юзеру 2  кассир и берется его роль - false
  //у юзера 3 берется роль - true

  @Test
  void testIsActiveUser() {
    assertTrue(userService.isActiveUser());
    userService.logout();
    assertFalse(userService.isActiveUser());
  }

  //isActiveUser
  // вызываем этот метод - дает true
  //выходим - дает false.


}
