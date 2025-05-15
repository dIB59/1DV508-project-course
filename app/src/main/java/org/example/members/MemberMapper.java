package org.example.members;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.database.EntityMapper;

public class MemberMapper implements  EntityMapper<Member> {
  @Override
  public Member map(ResultSet rs) throws SQLException {
    int personalNumber = rs.getInt("personal_number");
    int memberPoints = rs.getInt("points");
    return new Member(personalNumber, memberPoints);
  }

}
