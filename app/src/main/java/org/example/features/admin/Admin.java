package org.example.features.admin;

import org.example.database.Identifiable;

public class Admin implements Identifiable<Integer> {
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

  public Integer getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  String getPassword() {
    return password;
  }
}
