package view;

import java.util.Scanner;
import service.AdminService;
import service.UserService;

public class Menu {

  private AdminService adminService;
  private UserService userService;

  public Menu(AdminService adminService, UserService userService) {
    this.adminService = adminService;
    this.userService = userService;
  }

  public void run() {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("1. Регистрация пользователя");
      System.out.println("2. Вход в личный кабинет");
      System.out.println("3. Просмотр баланса");
      System.out.println("4. Пополнение счета");
      System.out.println("5. Снятие средств со счета");
      System.out.println("6. Открытие нового счета");
      System.out.println("7. Закрытие счета");
      System.out.println("8. Просмотр истории операций");
      System.out.println("9. Обмен Валюты");
      System.out.println("0. Выход из программмы");
      int command = scanner.nextInt();
      scanner.nextLine();
    }

  }
}
