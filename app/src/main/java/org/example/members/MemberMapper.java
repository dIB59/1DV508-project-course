package org.example.members;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.database.EntityMapper;

public class MemberMapper implements  EntityMapper<Member> {
  @Override
  public Member map(ResultSet rs) throws SQLException {
    int personalNumber = rs.getInt("personalNumber");
    return new Member(personalNumber);
  }

}
