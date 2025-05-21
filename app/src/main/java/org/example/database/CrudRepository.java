package org.example.database;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * A generic interface for CRUD operations on a repository for a specific type.
 *
 * @param <T> the type of the entity
 *     <p>Example usage:
 *     <pre>
 *            class ProductRepository implements CrudRepository&lt;Product&gt; {
 *            <p></p>
 *                &#64;Override
 *
 *                public void save(Product product) throws SQLException {
 *                    // Implementation for saving a product
 *                }
 *            }
 *            </pre>
 */
public interface CrudRepository<T extends Identifiable<ID>, ID> {

  /**
   * Saves the given entity to the repository.
   *
   * @param entity the entity to save
   * @throws SQLException if an SQL error occurs
   */
  T save(T entity) throws SQLException;

  /**
   * Finds an entity by its ID.
   *
   * @param id the ID of the entity to find
   * @return an Optional containing the found entity, or an empty Optional if not found
   * @throws SQLException if an SQL error occurs
   */
  Optional<T> findById(ID id) throws SQLException;

  /**
   * Updates the given entity in the repository.
   *
   * @param entity the entity to update
   * @throws SQLException if an SQL error occurs
   */
  void update(T entity) throws SQLException;

  /**
   * Deletes the entity with the given ID from the repository.
   *
   * @param id the ID of the entity to delete
   * @throws SQLException if an SQL error occurs
   */
  void delete(ID id) throws SQLException;

  /**
   * Finds all entities in the repository.
   *
   * @return a list of all entities
   * @throws SQLException if an SQL error occurs
   */
  List<T> findAll() throws SQLException;

  default void printResultSet(ResultSet rs) throws SQLException {
    // Get metadata to retrieve column names
    ResultSetMetaData rsMetaData = rs.getMetaData();
    int columnCount = rsMetaData.getColumnCount();

    // Loop through each row in the ResultSet
    while (rs.next()) {
      // For each row, print column name and value
      for (int i = 1; i <= columnCount; i++) {
        String columnName = rsMetaData.getColumnName(i); // Get column name
        Object value = rs.getObject(i); // Get column value (handles all types)
        System.out.println(columnName + ": " + value);
      }
      System.out.println("------ End of Row ------");
    }
  }
}
