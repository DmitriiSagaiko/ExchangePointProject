import exception.EmailValidateException;
import java.util.Optional;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reposiroty.DataRepository;
import reposiroty.UserRepository;
import service.AdminService;
import service.UserService;

public class TestExceptions {

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

  // UserRegistration  - уйдет в exception

  @Test
  void testUserRegistrationInvalidEmail() {
    //Optional<User> userOptional = userService.userRegistration("user1234.com", "QWErty!2$", "fake Email");
    EmailValidateException exception = Assertions.assertThrows(
        EmailValidateException.class,
        () -> userRepository.registerUser("user1234.com", "QWErty!2$", "fake Email"));

//    String expectedMessage = "@ error";
//    String actualMessage = exception.getMessage();
//    System.out.println(actualMessage);
//    Assertions.assertTrue(actualMessage.contains(expectedMessage));
  }


}
