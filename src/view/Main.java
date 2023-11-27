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

    Menu menu = new Menu(adminService,userService);
    menu.run();
  }

}
