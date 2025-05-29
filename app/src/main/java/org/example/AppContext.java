package org.example;

import org.example.features.translation.Language;

public class AppContext {
  private static AppContext instance;

  private Language language;

  // 🔹 Added: Track dark mode status
  private boolean darkMode = false;

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
  
  public boolean isDarkMode() {
    return darkMode;
  }

  public void setDarkMode(boolean darkMode) {
    this.darkMode = darkMode;
  }
}
