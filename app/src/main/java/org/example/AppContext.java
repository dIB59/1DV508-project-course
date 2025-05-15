package org.example;

import org.example.features.translation.Language;

public class AppContext {
  private static AppContext instance;

  private Language language;

  private AppContext() {
    this.language = Language.ENGLISH; // Default language
  }

  public static AppContext getInstance() {
    if (instance == null) {
      instance = new AppContext();
    }
    return instance;
  }

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }
}
