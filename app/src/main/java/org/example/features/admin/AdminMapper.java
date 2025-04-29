package org.example.features.admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.database.EntityMapper;

public class AdminMapper implements EntityMapper<Admin> {

  @Override
  public Admin map(ResultSet rs) throws SQLException {
    int id = rs.getInt("id");
    String username = rs.getString("username");
    String password = rs.getString("password");
    return new Admin(id, username, password);
  }
}
