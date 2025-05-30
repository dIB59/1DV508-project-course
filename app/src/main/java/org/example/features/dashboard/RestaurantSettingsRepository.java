package org.example.features.dashboard;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.image.Image;

public class RestaurantSettingsRepository {
    

  private final Connection connection;

  public RestaurantSettingsRepository(Connection connection) {
    this.connection = connection;
  }

  public RestaurantSettings loadSettings() {
    try (PreparedStatement stmt = connection.prepareStatement(
            "SELECT name, address, contact, logo FROM restaurant_settings WHERE id = 1")) {

      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        RestaurantSettings settings = new RestaurantSettings();
        settings.setName(rs.getString("name"));
        settings.setaddress(rs.getString("address"));
        settings.setContact(rs.getString("contact"));
        settings.setLogoBlob(rs.getBytes("logo"));
        return settings;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return new RestaurantSettings();
  }

  public Image getLogo() {
    try (PreparedStatement stmt = connection.prepareStatement(
        "SELECT logo FROM restaurant_settings WHERE id = 1")) {

      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        byte[] imgBytes = rs.getBytes("logo");
        if (imgBytes != null) {
          ByteArrayInputStream bis = new ByteArrayInputStream(imgBytes);
          Image logoImage = new Image(bis);
          return logoImage;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("No logo found");
  }

  public void saveSettings(RestaurantSettings settings) {
    try (PreparedStatement stmt = connection.prepareStatement(
            "UPDATE restaurant_settings SET name = ?, address = ?, contact = ?,logo = ? WHERE id = 1")) {

      stmt.setString(1, settings.getName());
      stmt.setString(2, settings.getAddress());
      stmt.setString(3, settings.getContact());
      stmt.setBytes(4, settings.getLogoBlob());
      stmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


}
