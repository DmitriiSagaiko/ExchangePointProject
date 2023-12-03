package view;

import reposiroty.DataRepository;
import reposiroty.UserRepository;
import service.AdminService;
import service.UserService;

public class Main {

  public static void main(String[] args) {

    DataRepository dataRepository = new DataRepository();
    UserRepository userRepository = new UserRepository();
    AdminService adminService = new AdminService(userRepository,dataRepository);
    UserService userService = new UserService(userRepository, dataRepository);

    //Для наладки
//    userService.login("user3@mail.ru", "User123$");
//    userService.openNewAccount("EUR",1000);
//    userService.openNewAccount("RUB",100000);
//    userService.openNewAccount("USD",500);
//    userService.openNewAccount("USD",1000);
//    userService.withdraw(200003, 500);
//    userService.withdraw(100003, 200);
//    userService.deposit(100003, 1000);
//    userService.deposit(100001, 200);
//    userService.deposit(100001, 200);
//    userService.withdraw(100001, 100);
//    userService.transfer(100000,100001,50);
//    userService.transfer(100000,100001,50);
//    userService.logout();
//
//    userService.login("user1@mail.ru", "User123$");
//    userService.openNewAccount("EUR",1000);
//    userService.openNewAccount("RUB",100000);
//    userService.openNewAccount("USD",500);
//    userService.openNewAccount("USD",1000);
//    userService.withdraw(100004, 500);
//    userService.withdraw(100004, 200);
//    userService.deposit(100007, 1000);
//    userService.deposit(100007, 200);
//    userService.deposit(100007, 200);
//    userService.transfer(100001, 100004,100);
//    userService.transfer(100005, 100009,100);
//    userService.logout();
//
//    userService.login("user3@mail.ru", "User123$");



    Menu menu = new Menu(adminService,userService);
    menu.run();
  }

}
