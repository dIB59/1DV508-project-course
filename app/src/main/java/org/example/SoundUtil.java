package org.example;

import javafx.scene.media.AudioClip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundUtil {
  private static final Logger log = LoggerFactory.getLogger(SoundUtil.class);
  private static final AudioClip clickSound =
      new AudioClip(
          SoundUtil.class
              .getResource("/assets/Voicy_Tralalero_Tralala_Italian_Brainrot.mp3")
              .toExternalForm());

  public static void playClick() {
    log.debug(clickSound.getSource());
    if (clickSound.isPlaying()) {
      clickSound.stop();
    }
    clickSound.play();
  }
}
