import exception.EmailValidateException;
import exception.PasswordValidateExcepton;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reposiroty.DataRepository;
import reposiroty.UserRepository;
import service.UserService;

public class TestExceptions {

  UserRepository userRepository = new UserRepository();
  DataRepository dataRepository = new DataRepository();
  UserService userService = new UserService(userRepository, dataRepository);

  @Test
  @BeforeEach
  void setUp() {
    userService.logout();
    userService.login("user3@mail.ru", "User123$");
  }


  @Test
  void testUserRegistrationInvalidEmail() {
    EmailValidateException exception =
        Assertions.assertThrows(
            EmailValidateException.class,
            () -> userRepository.registerUser("user1234.com", "QWErty!2$", "fake Email"));
    Assertions.assertEquals("Email validate exception @ error", exception.getMessage());

    EmailValidateException exception1 =
        Assertions.assertThrows(
            EmailValidateException.class,
            () -> userRepository.registerUser("user1234@com", "QWErty!2$", "fake Email"));
    Assertions.assertEquals("Email validate exception . Нет точки после собаки",
        exception1.getMessage());

    EmailValidateException exception2 =
        Assertions.assertThrows(
            EmailValidateException.class,
            () -> userRepository.registerUser("user1234@.a", "QWErty!2$", "fake Email"));
    Assertions.assertEquals("Email validate exception need to have 2 symbol after DOT",
        exception2.getMessage());

    EmailValidateException exception3 =
        Assertions.assertThrows(
            EmailValidateException.class,
            () -> userRepository.registerUser("!!!!@mail.ru", "QWErty!2$", "fake Email"));
    Assertions.assertEquals("Email validate exception email does not contain ._-@",
        exception3.getMessage());

  }

  @Test
  void testIncorrectPasswordUserRegistration() {
    PasswordValidateExcepton exception =
        Assertions.assertThrows(
            PasswordValidateExcepton.class,
            () -> userRepository.registerUser("user1234@mail.com", "QWErty!r$", "fake password"));
    Assertions.assertEquals("no digit", exception.getMessage());

    PasswordValidateExcepton exception1 =
        Assertions.assertThrows(
            PasswordValidateExcepton.class,
            () -> userRepository.registerUser("user1234@mail.com", "QWEQWER1$", "fake password"));
    Assertions.assertEquals("no lower case", exception1.getMessage());

    PasswordValidateExcepton exception2 =
        Assertions.assertThrows(
            PasswordValidateExcepton.class,
            () -> userRepository.registerUser("user1234@mail.com", "QWEQWER1$".toLowerCase(),
                "fake password"));
    Assertions.assertEquals("no upper case", exception2.getMessage());

    PasswordValidateExcepton exception3 =
        Assertions.assertThrows(
            PasswordValidateExcepton.class,
            () -> userRepository.registerUser("user1234@mail.com", "QWEQwER1", "fake password"));
    Assertions.assertEquals("use special symbols", exception3.getMessage());
  }

  @Test
  void testUserRegistration() {
    try {
      User user = userService.userRegistration("dmitsag@gmail.com", "qweRTY12%", "Dmitrii Sagaiko");
      Assertions.assertEquals("dmitsag@gmail.com", user.getEmail());
      Assertions.assertEquals("qweRTY12%", user.getPassword());
      Assertions.assertEquals("Dmitrii Sagaiko", user.getName());
    } catch (EmailValidateException | PasswordValidateExcepton e) {
      e.printStackTrace();
    }
  }
}
