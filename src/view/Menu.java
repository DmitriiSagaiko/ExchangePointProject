package view;

import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;
import models.Account;
import models.Transaction;
import models.User;
import service.AdminService;
import service.UserService;

public class Menu {

  private final AdminService adminService;
  private final UserService userService;

  private final Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

  public Menu(AdminService adminService, UserService userService) {
    this.adminService = adminService;
    this.userService = userService;
  }

  public void run() {
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
      System.out.println("10. Выход из личного кабинета пользователя");
      System.out.println("11 Команды администратора");
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
          showTheHistory();
        }
        break;
        case 9: {
          transfer();
        }
        break;
        case 10: {
          logout();
        }
        break;
        case 11: {
          showOptionsForAdmin();
        }
        break;

      }
    }
  }


  private void exit() { // 0
    System.out.println("Завершаю программу");
    System.exit(0);
  }

  private void registration() {
    System.out.println("Введите email в формате: dmitsag@gmail.com");
    System.out.println(
        "email должен содержать @, точку после @ и длина домена должна быть не короче 2 символов. \n Длина email должна составлять не менее 8 символов");
    String email = scanner.nextLine();

    System.out.println("Введите пароль в формате '123wqeRTY!'");
    System.out.println(
        "Пароль должен быть длинее 7 символов, содержать цифры, нижний и верхний регистр и как минимум 1 спец символ из '!%$@&'");
    String password = scanner.nextLine();

    System.out.println("Введите ваше Имя и Фамилию");
    String name = scanner.nextLine();
    userService.userRegistration(email, password, name);
  } //1

  private void login() {
    System.out.println("Введите email");
    String email = scanner.nextLine();
    System.out.println("Введите пароль");
    String password = scanner.nextLine();
    userService.login(email, password);
  } //2

  private void showTheBalance() {
    if(!userService.isActiveUser()) {
      return;
    }
    if(userService.showAllAccountsID()) {
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

  } //3

  private void deposit() {
    if(!userService.isActiveUser()) {
      return;
    }
    if (userService.showAllAccountsID()) {
      System.out.println("Введите номер счета");
      Integer accountID = scanner.nextInt();
      System.out.println("Введите сумму пополнения");
      double sum = scanner.nextDouble();
      userService.deposit(accountID, sum);
    }
  } //4

  private void withdraw() {
    if(!userService.isActiveUser()) {
      return;
    }
    if(userService.showAllAccountsID()) {
      System.out.println("Введите номер счета");
      Integer accountID = scanner.nextInt();
      System.out.println("Введите сумму снятия");
      double sum = scanner.nextDouble();
      userService.withdraw(accountID, sum);
    }
  } //5

  private void openNewAccount() {
    if(!userService.isActiveUser()) {
      return;
    }
    System.out.println("Доступные валюты: " + userService.getCurrency());
    System.out.println("Введите тип желаемой валюты");
    String currency = scanner.nextLine();
    System.out.println("Введите сумму открытия счета");
    double sum = scanner.nextDouble();
    Optional<Account> account = userService.openNewAccount(currency, sum);
    System.out.println(account.isPresent() ? "Открытие счета прошло успешно. Номер вашего нового счета: " + account.get().getAccountNumber() : " Не удалось создать новый счет");
  } //6

  private void closeAccount() {
    if(!userService.isActiveUser()) {
      return;
    }
    System.out.println("У тебя есть такие счета:");
    if (userService.showAllAccountsID()) {
      System.out.println("Введите номер закрываемого счета");
      Integer accountID = scanner.nextInt();
      Optional<Account> account = userService.closeAccount(accountID);
      if (account.isEmpty()) return;
      System.out.println(
          account.isPresent() ? "Счет " +account.get().getAccountNumber() + " был успешно закрыт!" : "Банковский счет не существует. Его нельзя закрыть");
    }
  } //7

  private void showTheHistory() {
    if(!userService.isActiveUser()) {
      return;
    }
    System.out.println("1 - просмотр по валюте");
    System.out.println("2 - просмотр всех операций на счете");
    int type = scanner.nextInt();
    scanner.nextLine();
    if (type == 1) {
      System.out.println("Введите тип желаемой валюты");
      String currency = scanner.nextLine();
      System.out.println(userService.showTheHistory(type, currency));
    } else if (type == 2) {
      userService.showTheHistory(type).forEach(System.out::println);
    }
  } //8


  private void transfer() {
    if(userService.checkTheBalance().isEmpty()) {
      System.out.println("У вас нет открытых счетов");
      return;
    }
    System.out.println(userService.checkTheBalance());
    System.out.println("Введите номер счета списания:");
    Integer from = scanner.nextInt();
    scanner.nextLine();
    System.out.println("Введите номер счета пополнения:");
    Integer to = scanner.nextInt();
    System.out.println("Введите сумму перевода");
    scanner.nextLine();
    double amount = scanner.nextDouble();
    System.out.println(userService.transfer(from, to, amount));
  } //9

  private void logout() {
    userService.logout();
  } //10

  private void showOptionsForAdmin() {
    if(!userService.isActiveUser()) {
      return;
    }
    if (userService.isAdministrator()) {
      inner:
      while (true) {
        System.out.println("1 - Изменить курс валюты");
        System.out.println("2 - Добавить валюту");
        System.out.println("3 - Удалить валюту");
        System.out.println("4 - Просмотреть операции пользователя");
        System.out.println("5 - Просмотреть операции по валюте");
        System.out.println("6 - Назначить другого пользователя кассиром");
        System.out.println("7 - выйти в предыдущее меню");
        int command = scanner.nextInt();
        scanner.nextLine();
        switch (command) {

          case 1: {
            System.out.println(userService.getCurrency());
            System.out.println("Введите желаемую валюту. Изменить курс рубля нельзя");
            String currency = scanner.nextLine();
            System.out.println("Введите желаемый курс выбранной валюты");
            Double rate = scanner.nextDouble();
            System.out.println(
                "Изменения прошли успешно: " + adminService.changeCurrencyExchange(currency, rate));
          }
          break;
          case 2: {
            System.out.println(userService.getCurrency());
            System.out.println("Введите желаемую валюту, которой нет в списке");
            String currency = scanner.nextLine();
            System.out.println(
                "Введите желаемый курс выбранной валюты к рублю. Например 1 USD = 100 RUB те желаемый курс 100");
            Double rate = scanner.nextDouble();
            System.out.println(
                "Валюта " + currency + " была добавлена :" + adminService.addTheCurrency(currency,
                    rate));
          }
          break;
          case 3: {
            System.out.println(userService.getCurrency());
            System.out.println("Введите желаемую валюту из списка для удаления ");
            String currency = scanner.nextLine();
            System.out.println(
                "удалил валюту " + currency + " :" + adminService.deleteTheCurrency(currency));
          }
          break;
          case 4: {
            System.out.println(userService.getUsers());
            System.out.println("Введите id желаемого юзера для просмотра его операций");
            int id = scanner.nextInt();
            System.out.println(adminService.showUsersOperations(id));
          }
          break;
          case 5: {
            System.out.println(userService.getCurrency());
            System.out.println("Введите желаемую валюту для просмотра истории операций");
            String currency = scanner.nextLine();
            System.out.println(adminService.showCurrencyOperations(currency));
          }
          break;
          case 6: {
            System.out.println(userService.getUsers());
            System.out.println("Введите id юзера, который будет кассиром");
            Integer id = scanner.nextInt();
            Optional<User> userForCash = Optional.ofNullable(userService.getUsers().get(id));
            adminService.assignCashier(userForCash);
          }
          break;
          case 7: {
            break inner;
          }
        }
      }
    } else {
      System.out.println("Вы не Администратор. Для вас этот функционал закрыт");
    }
  }
}

