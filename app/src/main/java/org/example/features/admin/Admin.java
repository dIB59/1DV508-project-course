package org.example.features.admin;

public class Admin {
  private final int id;
  private String username;
  private String password;

  public Admin(String username, String password) {
    this.id = 0;
    this.username = username;
    this.password = password;
  }

  public Admin(int id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
  }

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  String getPassword() {
    return password;
  }
}
