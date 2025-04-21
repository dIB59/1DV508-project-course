package org.example.shared;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface EntityMapper<T> {
  T map(ResultSet rs) throws SQLException;

}

