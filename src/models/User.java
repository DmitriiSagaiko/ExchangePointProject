package models;

import java.util.HashMap;
import java.util.Map;

public class User {

  private String name;

  private final Map<Integer, Account> accounts = new HashMap<>();

  private Role role = Role.USER;

  private final int id;

  private String email;
  private String password;


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

  public Map<Integer, Account> getAccounts() {
    Map<Integer, Account> result = new HashMap<>(accounts);
    return result;
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

  public Map<Integer, Account> getOneAccount(Integer accountID) {
    Map<Integer, Account> my = new HashMap<>(accounts);
    Map<Integer, Account> result = new HashMap<>();
    result.put(accountID, my.get(accountID));
    return result;
  }

  public Map<Integer, Account> addNewAccount(Integer accountID, Account newAccount) {
    accounts.put(accountID, newAccount);
    return new HashMap<>(accounts);
  }

  public Map<Integer, Account> deleteAccount(Integer accountID) {
    accounts.remove(accountID);
    return new HashMap<>(accounts);
  }
}
