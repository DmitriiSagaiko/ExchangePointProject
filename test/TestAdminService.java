import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reposiroty.DataRepository;
import reposiroty.UserRepository;
import service.*;

public class TestAdminService {
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

  //Изменение курса валюты.
  /*
  Плохие:
  Пытаемся изменить рубль - ожидаем false
  Пытаемся задать курс рубля отрицательный - false
  Хорошие:
  Задаем курс Евро, Доллара и ожидаем тру. Потом берем текущие курсы и проверяем, что они задались.
   */

  //Добавить валюту
  /*
    Плохие:
    Пытаемся добавить существующую валюту - получаем false
    Пытаемся добавить отрицательный курс к валюте - получаем false

    Хорошие:
    Пытаемся добавить SRD с нормальный курсом - получаем true. Потом берем текущий курс и проверяем,
    что в нем есть сербский динар и что его курс равен заданному

   */


}
