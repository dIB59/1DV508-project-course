package org.example.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The interface Entity mapper.
 *
 * @param <T> the type parameter
 */
@FunctionalInterface
public interface EntityMapper<T> {
  /**
   * Maps a ResultSet to an entity of type T.
   *
   * @param rs the ResultSet to map
   * @return the mapped entity
   * @throws SQLException if an SQL error occurs
   *     <p>Example usage:
   *     <p>OrderMapper orderMapper = new OrderMapper();
   *     <p>ResultSet rs = statement.executeQuery("SELECT * FROM orders WHERE id = 1");
   *     <p>Order order = orderMapper.map(rs);
   */
  T map(ResultSet rs) throws SQLException;

}
