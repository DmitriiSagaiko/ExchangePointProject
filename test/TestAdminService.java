import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import models.Role;
import models.Transaction;
import models.TypeOfOperation;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import reposiroty.DataRepository;
import reposiroty.UserRepository;
import service.AdminService;
import service.UserService;

public class TestAdminService {

  UserRepository userRepository = new UserRepository();
  DataRepository dataRepository = new DataRepository();
  AdminService adminService = new AdminService(userRepository, dataRepository);
  UserService userService = new UserService(userRepository, dataRepository);

  public TestAdminService() {
  }


  @Test
  @BeforeEach
  void setUp() {
    userService.logout();
    userService.login("user3@mail.ru", "User123$");
  }

  ///////////////////////////////////////////////////////////////////////////////////

  @Test
  void testChangeTheCurrencyRUB() {
    boolean result = adminService.changeCurrencyExchange("RUB", 2.0);
    assertFalse(result);
  }

  @Test
  void testChangeTheCurrencyEURnegative() {
    boolean result = adminService.changeCurrencyExchange("EUR", -20.0);
    assertFalse(result);
  }

  @Test
  void testChangeTheCurrency() {
    boolean result1 = adminService.changeCurrencyExchange("EUR", 200.0);
    boolean result2 = adminService.changeCurrencyExchange("USD", 150.0);
    Map<String, Double> after = userService.getAllCurrencyAndRate();
    assertEquals(200.0, after.get("EUR"));
    assertEquals(150.0, after.get("USD"));
    assertTrue(result1);
    assertTrue(result2);
  }
  ///////////////////////////////////////////////////////////////////////////////////


  @Test
  void testAddExistingCurrency() {
    assertFalse(adminService.addTheCurrency("EUR", 1000.));
    assertEquals(100., userService.getAllCurrencyAndRate().get("EUR"));
  }

  @Test
  void testAddNegativeCurrency() {
    assertFalse(adminService.addTheCurrency("RSD", -0.84));
    assertNotEquals(-0.84, userService.getAllCurrencyAndRate().get("RSD"));
  }

  @Test
  void testAddCurrency() {
    assertTrue(adminService.addTheCurrency("RSD", 0.84));
    assertEquals(0.84, userService.getAllCurrencyAndRate().get("RSD"));
  }

  ///////////////////////////////////////////////////////////////////////////////////

  @Test
  void testDeleteUnexistingCurrency() {
    assertFalse(adminService.deleteTheCurrency("LOL"));
    assertFalse(userService.getAllCurrencyAndRate().containsKey("LOL"));
  }

  @Test
  void testDeleteNotEmptyCurrency() {
    userService.openNewAccount("USD", 10000.);
    assertFalse(adminService.deleteTheCurrency("USD"));
    assertTrue(userService.getAllCurrencyAndRate().containsKey("USD"));
  }

  @Test
  void testDeleteCurrency() {
    userService.openNewAccount("USD", 10000.);
    userService.withdraw(100000, 10000.);
    userService.closeAccount(100000);
    adminService.deleteTheCurrency("USD");
    assertFalse(userService.getAllCurrencyAndRate().containsKey("USD"));
    assertNull(userService.getAllCurrencyAndRate().get("USD"));
  }

  ///////////////////////////////////////////////////////////////////////////////////

  public static Stream<Integer> GenerateID() {
    return Stream.of(-1, 0, 1000, 10001, 100000);
  }

  @ParameterizedTest
  @MethodSource("GenerateID")
  void showTheHistoryBad(int id) {
    assertTrue(adminService.showUsersOperations(id).isEmpty());
  }

  @Test
  void showTheHistory() {
    userService.openNewAccount("USD", 10000.);
    userService.withdraw(100000, 5000.);
    userService.deposit(100000, 2000.);
    List<Transaction> result = adminService.showUsersOperations(3);
    assertEquals("USD", result.get(0).getCurrency());
    assertEquals(TypeOfOperation.WITHDRAW, result.get(1).getType());
  }

  ///////////////////////////////////////////////////////////////////////////////////


  @Test
  void showWrongCurrencyOperations() {
    userService.openNewAccount("USD", 10000.);
    userService.withdraw(100000, 5000.);
    userService.deposit(100000, 2000.);
    assertTrue(adminService.showCurrencyOperations("LOL").isEmpty());
    ;
  }

  @Test
  void showCurrencyOperations() {
    userService.openNewAccount("EUR", 10000.);
    userService.withdraw(100000, 5000.);
    userService.deposit(100000, 2000.);

    List<Transaction> result = adminService.showCurrencyOperations("EUR");
    assertFalse(result.isEmpty());
    assertEquals("EUR", result.get(0).getCurrency());
    assertEquals(TypeOfOperation.WITHDRAW, result.get(1).getType());
  }

  ///////////////////////////////////////////////////////////////////////////////////


  @Test
  void assignBadCashier() {
    assertFalse(adminService.assignCashier(Optional.empty()));
  }

  @Test
  void assignBadCashierAdmin() {
    User admin = userRepository.getUsers().get(3);
    assertFalse(adminService.assignCashier(Optional.ofNullable(admin)));
  }

  @Test
  void assignCashier() {
    User user1 = userRepository.getUsers().get(1);
    User user2 = userRepository.getUsers().get(2);
    assertTrue(adminService.assignCashier(Optional.ofNullable(user1)));
    assertTrue(adminService.assignCashier(Optional.ofNullable(user2)));
    assertEquals(Role.CASHOFFICER, user1.getRole());
    assertEquals(Role.CASHOFFICER, user2.getRole());
  }

}
