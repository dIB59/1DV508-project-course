package org.example.members;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.database.CrudRepository;
import org.example.database.EntityMapper;

public class MemberRepository implements CrudRepository<Member, Integer> {

  private final EntityMapper<Member> memberMapper;
  private final Connection connection;

  public MemberRepository(Connection connection, MemberMapper memberMapper) {
    this.connection = connection;
    this.memberMapper = memberMapper;
  }

  @Override
  public Member save(Member entity) throws SQLException {
    String sql = "INSERT INTO Members (personal_number) VALUES (?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, entity.getPersonalNumber());
      stmt.executeUpdate();
    }
    String sql2 = "SELECT LAST_INSERT_ID()";
    try (PreparedStatement stmt = connection.prepareStatement(sql2);
        ResultSet rs = stmt.executeQuery()) {
      if (rs.next()) {
        return new Member(entity.getPersonalNumber(), entity.getMemberPoints());
      }
    }
    throw new SQLException("Failed to retrieve the last inserted id.");
  }

  @Override
  public Optional<Member> findById(Integer personalnumber) throws SQLException {
    String sql = "SELECT * FROM Members WHERE personal_number = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, personalnumber);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return Optional.of(memberMapper.map(rs));
      }
    }
    return Optional.empty();
  }

  @Override
  public void update(Member entity) throws SQLException {
    String sql = "UPDATE Members SET personal_number = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, entity.getPersonalNumber());
      stmt.executeUpdate();
    }
  }

  public void addPoints(int personalNumber, int points) throws SQLException {
    String sql = "UPDATE Members SET points = points + ? WHERE personal_number = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, points);
      stmt.setInt(2, personalNumber);
      stmt.executeUpdate();
    }
  }

  @Override
  public void delete(Integer personalnumber) throws SQLException {
    String sql = "DELETE FROM Members WHERE personal_number = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, personalnumber);
      stmt.executeUpdate();
    }
  }

  @Override
  public List<Member> findAll() throws SQLException {
    String sql = "SELECT * FROM Members";
    try (PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
      List<Member> members = new ArrayList<>();
      while (rs.next()) {
        members.add(memberMapper.map(rs));
      }
      return members;
    }
  }
}
