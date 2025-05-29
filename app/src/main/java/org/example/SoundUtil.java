package org.example;

import javafx.scene.media.AudioClip;

public class SoundUtil {
  private static final AudioClip clickSound =
      new AudioClip(
          SoundUtil.class
              .getResource("/assets/Voicy_Tralalero_Tralala_Italian_Brainrot.mp3")
              .toExternalForm());

  public static void playClick() {
    System.out.println(clickSound.getSource());
  }
}
