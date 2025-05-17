package org.example.features.translation;

public enum Language {
  ENGLISH("en"),
  SWEDISH("sv"),
  FRENCH("fr"),
  PORTUGUESE("pt"),
  JAPANESE("ja");

  private final String code;

  Language(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return code;
  }
}
