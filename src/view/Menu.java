package view;

import java.util.Optional;
import java.util.Scanner;
import models.Account;
import service.AdminService;
import service.UserService;

public class Menu {

  private AdminService adminService;
  private UserService userService;

  private Scanner scanner = new Scanner(System.in);

  public Menu(AdminService adminService, UserService userService) {
    this.adminService = adminService;
    this.userService = userService;
  }

  public void run() {
//    Scanner scanner = new Scanner(System.in);
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

      switch (command) {
        case 0: {
          exit();
        }
        break;
        case 1: {
          registration();
        }
        break;
        case 2: {
          login();
        }
        break;
        case 3: {
          showTheBalance();
        }
        break;
        case 4: {
          deposit();
        }
        break;
        case 5: {
          withdraw();
        }
        break;
        case 6: {
          openNewAccount();
        }
        break;
        case 7: {
          closeAccount();
        }
        break;
        case 8: {

        }
        break;
        case 9: {

        }
        break;

      }
    }
  }


  private void exit() {
    System.out.println("Завершаю программу");
    System.exit(0);
  }

  private void registration() {
    //TODO правильный формат и обработка ошибок
    System.out.println("Введите логин в формате");
    String name = scanner.nextLine();
    //TODO правильный формат и обработка ошибок
    System.out.println("Введите пароль в формате");
    String password = scanner.nextLine();
    userService.userRegistration(name, password);
  }

  private void login() {
    //TODO правильный формат и обработка ошибок
    System.out.println("Введите логин в формате");
    String name = scanner.nextLine();
    //TODO правильный формат и обработка ошибок
    System.out.println("Введите пароль в формате");
    String password = scanner.nextLine();
    userService.login(name, password);
  }

  private void showTheBalance() {
    userService.showAllAccountsID();
    System.out.println("Хотите посмотреть на конкретном счете - нажмите 1");
    System.out.println("Хотите посмотреть на всех счетах - нажмите 2");
    System.out.println("Назад - нажмите 3");
    int command = scanner.nextInt();
    scanner.nextLine();
    switch (command) {
      case 1: {
        System.out.println("Введите номер счета");
        Integer accountID = scanner.nextInt();
        System.out.println(userService.checkTheBalance(accountID));
      }
      break;
      case 2: {
        System.out.println(userService.checkTheBalance());
      }
      break;
      case 3: {
        break;
      }

    }

  }

  private void deposit() {
    userService.showAllAccountsID();
    System.out.println("Введите номер счета");
    Integer accountID = scanner.nextInt();
    System.out.println("Введите сумму пополнения");
    double sum = scanner.nextDouble();
    userService.deposit(accountID, sum);
  }

  private void withdraw() {
    userService.showAllAccountsID();
    System.out.println("Введите номер счета");
    Integer accountID = scanner.nextInt();
    System.out.println("Введите сумму снятия");
    double sum = scanner.nextDouble();
    userService.withdraw(accountID, sum);
  }

  private void openNewAccount() {
    System.out.println(userService.getCurrency());
    System.out.println("Введите тип желаемой валюты");
    String currency = scanner.nextLine();
    System.out.println("Введите сумму открытия счета");
    double sum = scanner.nextDouble();
    Optional<Account> account = userService.openNewAccount(currency, sum);
    System.out.println(account.isPresent() ? account : " Не удалось создать новый аккаунт");
  }

  private void closeAccount() {
    System.out.println("У тебя есть такие счета:");
    userService.showAllAccountsID();
    System.out.println("Введите номер закрываемого счета");
    Integer accountID = scanner.nextInt();
    Optional<Account> account = userService.closeAccount(accountID);
    System.out.println(
        account.isPresent() ? account : "Банковский счет не существует. Его нельзя закрыть");

  }

}
