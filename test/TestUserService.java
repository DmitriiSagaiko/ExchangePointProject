import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import exception.EmailValidateException;
import exception.PasswordValidateExcepton;
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

  public TestUserService() {
  }


  @Test
  @BeforeEach
  void setUp() {
    userService.logout();
    userService.login("user3@mail.ru", "User123$");
  }

  ///////////////////////////////////////////////////////////////////////////////////
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

  ///////////////////////////////////////////////////////////////////////////////////
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

  ///////////////////////////////////////////////////////////////////////////////////
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
  ///////////////////////////////////////////////////////////////////////////////////

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
  ///////////////////////////////////////////////////////////////////////////////////

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
  ///////////////////////////////////////////////////////////////////////////////////

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

  ///////////////////////////////////////////////////////////////////////////////////
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
  ///////////////////////////////////////////////////////////////////////////////////

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

  ///////////////////////////////////////////////////////////////////////////////////

  @Test
  void showTheHistoryWrongButton() {
    assertTrue(userService.showTheHistory(3).isEmpty());
  }

  @Test
  void showTheHistoryWrongCurrency() {
    assertTrue(userService.showTheHistory(1, "RSD").isEmpty());
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
    assertEquals(8, userService.showTheHistory(2).size());
  }

  ///////////////////////////////////////////////////////////////////////////////////

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
  ///////////////////////////////////////////////////////////////////////////////////

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

  ///////////////////////////////////////////////////////////////////////////////////


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
  ///////////////////////////////////////////////////////////////////////////////////

  @Test
  void testGetCurrency() {
    Set<String> stringSet = userService.getCurrency();
    assertEquals(3, stringSet.size());
    assertTrue(stringSet.contains("EUR"));
    assertTrue(stringSet.contains("USD"));
    assertTrue(stringSet.contains("RUB"));
  }
  ///////////////////////////////////////////////////////////////////////////////////

  @Test
  void testGetUser() throws EmailValidateException, PasswordValidateExcepton {
    Map<Integer, User> map = userService.getUsers();
    assertEquals(3, map.size());
    User user = userService.userRegistration("dmitsag@gmail.com", "QwerTY1$", "Dmitrii Sagaiko");
    map = userService.getUsers();
    assertEquals(4, map.size());
  }
  ///////////////////////////////////////////////////////////////////////////////////

  @Test
  void testIsAdministrator() {
    Map<Integer, User> map = userService.getUsers();
    assertNotEquals(map.get(1).getRole(), Role.ADMINISTRATOR);
    map.get(2).setRole(Role.CASHOFFICER);
    assertNotEquals(map.get(2).getRole(), Role.ADMINISTRATOR);
    assertEquals(map.get(3).getRole(), Role.ADMINISTRATOR);
  }

  @Test
  void testIsActiveUser() {
    assertTrue(userService.isActiveUser());
    userService.logout();
    assertFalse(userService.isActiveUser());
  }

}
