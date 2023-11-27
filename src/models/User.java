package models;

import java.util.HashMap;
import java.util.Map;

public class User {

  private String name;

  private final Map<Integer,Integer> accounts = new HashMap<>();

  private Role role = Role.USER;

  private final int id;

  private String email;
  private String password;

  public User(String name, int id, String email, String password) {
    this.name = name;
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public User(String name, Role role, int id, String email, String password) {
    this.name = name;
    this.role = role;
    this.id = id;
    this.email = email;
    this.password = password;
  }

  @Override
  public String toString() {
    return "User{" +
        "name='" + name + '\'' +
        ", role=" + role +
        ", id=" + id +
        ", email='" + email + '\'' +
        '}';
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<Integer, Integer> getAccounts() {
    return accounts;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public int getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
