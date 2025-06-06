package org.example.features.product;

public class Tag {
  private final int id;
  private final String name;

  public Tag(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return "Tag{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}
