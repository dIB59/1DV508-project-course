package org.example.members;

import org.example.database.Identifiable;

public class Member implements Identifiable<Integer> {
  private int personalNumber;
  private int memberPoints;

  public Member(int personalNumber, int memberPoints) {
    this.personalNumber = personalNumber;
    this.memberPoints = memberPoints;
  }

  public int getPersonalNumber() {
    return personalNumber;
  }

  public int getMemberPoints() {
    return memberPoints;
  }

  @Override
  public Integer getId() {
    return 0;
  }
}
