package model;

import javax.sound.sampled.*;
import java.io.File;
import java.util.Random;

/**
 * Manages audio playback, randomized playlist functionality,
 * and volume control for the Trivia Maze application.
 *
 * This class handles loading audio clips, automatically selecting
 * and playing random songs from a playlist, stopping currently
 * playing audio, and adjusting playback volume using Java Sound.
 *
 * Songs are automatically switched when a track finishes playing,
 * while preserving the user's currently selected volume level.
 */
public class SoundManager {
    private Clip myClip;
    private FloatControl myVolumeControl;

    private String[] myPlaylist;
    private int myCurrAudio = 0;
    private int myCurrVolume = 50;

    private final Random myRandom = new Random();

    private float myTargetGain;

    /**
     * Loads a playlist of audio file paths and begins playback
     * starting from a randomly selected track.
     *
     * @param theAudio array containing file paths for audio tracks
     */
    public void loadPlaylist(final String[] theAudio) {
        this.myPlaylist = theAudio;
        myCurrAudio = getRandomAudioIndex();

        playSound(myCurrAudio);
    }

    /**
     * Loads and plays an audio track from the playlist.
     *
     * This method stops any currently playing audio clip,
     * initializes a new audio clip, applies the current volume,
     * and automatically queues another random song when playback ends.
     *
     * @param theIndex index of the audio track within the playlist
     */
    private void playSound(final int theIndex) {
        try {
            stop();

            AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(new File(myPlaylist[theIndex]));

            myClip = AudioSystem.getClip();
            myClip.open(audioStream);

            myVolumeControl =
                    (FloatControl) myClip.getControl(FloatControl.Type.MASTER_GAIN);

            myTargetGain = convertVolumeToGain(myCurrVolume);
            myVolumeControl.setValue(myTargetGain);

            myClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP &&
                        myClip.getFramePosition() >= myClip.getFrameLength()) {

                    myCurrAudio = getRandomAudioIndex();
                    playSound(myCurrAudio);
                }
            });

            myClip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Selects a random index from the current playlist.
     *
     * Prevents the same track from playing twice consecutively
     * when multiple tracks are available.
     *
     * @return randomly selected audio index
     */
    private int getRandomAudioIndex() {
        if (myPlaylist == null || myPlaylist.length == 0) {
            return 0;
        }

        if (myPlaylist.length == 1) {
            return 0;
        }

        int nextSong;

        do {
            nextSong = myRandom.nextInt(myPlaylist.length);
        } while (nextSong == myCurrAudio);

        return nextSong;
    }

    /**
     * Stops and closes the currently playing audio clip.
     *
     * This method releases audio system resources and prevents
     * overlapping playback between tracks.
     */
    public void stop() {
        if (myClip != null) {
            myClip.stop();
            myClip.close();
            myClip = null;
        }
    }

    /**
     * Updates the playback volume based on the specified slider value.
     *
     * The volume value is converted into a decibel gain level
     * compatible with Java Sound's MASTER_GAIN control.
     *
     * @param theVolume desired volume level from 0 to 100
     */
    public void setVolume(final int theVolume) {
        myCurrVolume = theVolume;

        if (myVolumeControl == null) return;

        myTargetGain = convertVolumeToGain(theVolume);
        myVolumeControl.setValue(myTargetGain);
    }

    /**
     * Converts a slider-based volume value into a decibel gain value.
     *
     * Lower slider values are scaled logarithmically to produce
     * more natural perceived audio volume changes.
     *
     * @param theVolume slider volume value from 0 to 100
     * @return corresponding decibel gain value
     */
    private float convertVolumeToGain(final int theVolume) {
        if (theVolume <= 0) {
            return myVolumeControl.getMinimum();
        }

        float normalized = theVolume / 100f;

        // Makes low slider values much quieter
        normalized = normalized * normalized;

        float gain = (float) (20.0 * Math.log10(normalized));

        gain = Math.max(gain, myVolumeControl.getMinimum());
        gain = Math.min(gain, myVolumeControl.getMaximum());

        return gain;
    }
}