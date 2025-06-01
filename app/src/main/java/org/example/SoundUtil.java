package org.example;

import javafx.scene.media.AudioClip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundUtil {
  private static final AudioClip clickSound = new AudioClip(
          SoundUtil.class.getResource("/assets/clicktest.wav").toExternalForm()
  );

  static {
    clickSound.play(0.0); // preload silently
  }

  public static void playClick() {
    if (clickSound.isPlaying()) {
      clickSound.stop();
    }
    clickSound.play();
  }
}


