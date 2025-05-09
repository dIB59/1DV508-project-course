package org.example.members;

import org.example.database.Identifiable;

public class Member implements Identifiable<Integer> {
  private int personalNumber;

  public Member(int personalNumber) {
    this.personalNumber = personalNumber;
  }

  public int getPersonalNumber() {
    return personalNumber;
  }

  @Override
  public Integer getId() {
    return 0;
  }
}
