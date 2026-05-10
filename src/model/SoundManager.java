package model;

import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {
    private Clip clip;
    private FloatControl volumeControl;

    public void loadSound(String filePath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playLoop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void setVolume(int volume) {
        if (volumeControl == null) return;

        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();

        float gain = min + (max - min) * (volume / 100f);
        volumeControl.setValue(gain);
    }
}