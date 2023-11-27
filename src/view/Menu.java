package view;

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
    //TODO
  }
}
