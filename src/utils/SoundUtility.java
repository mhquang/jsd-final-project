package src.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for sound, implementing Singleton pattern.
 *
 * @author pvu001
 */
public class SoundUtility {
    private Clip bulletBrickSE, bulletTankSE;
    private Clip fireSE;
    private Clip explosion1SE, explosion2SE;
    private Clip startStageSE;
    private Clip pauseSE;
    private Clip powerupAppearSE;
    private Clip powerupPickSE;
    private Clip gameoverSE;
    private Clip statisticsSE;

    private boolean initialized = false;
    private static SoundUtility instance;

    // Private constructor to enforce Singleton
    private SoundUtility() {
    }

    /**
     * Returns the single instance of SoundUtility.
     * Initializes sound resources if not already initialized.
     */
    public static SoundUtility getInstance() {
        if (instance == null) {
            instance = new SoundUtility();
            instance.initialize();
        }
        return instance;
    }

    /**
     * Load different sound files
     */
    private void initialize() {
        try {
            bulletBrickSE = loadClip("sound/bullet_hit_2.wav");
            bulletTankSE = loadClip("sound/bullet_hit_1.wav");
            fireSE = loadClip("sound/bullet_shot.wav");
            explosion1SE = loadClip("sound/explosion_1.wav");
            explosion2SE = loadClip("sound/explosion_2.wav");
            startStageSE = loadClip("sound/stage_start.wav");
            pauseSE = loadClip("sound/pause.wav");
            powerupAppearSE = loadClip("sound/powerup_appear.wav");
            powerupPickSE = loadClip("sound/powerup_pick.wav");
            gameoverSE = loadClip("sound/game_over.wav");
            statisticsSE = loadClip("sound/statistics_1.wav");

            initialized = true;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(SoundUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Helper method to load a sound file into a Clip.
     */
    private Clip loadClip(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File soundFile = new File(filePath);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.setFramePosition(clip.getFrameLength());
        return clip;
    }

    // Methods to play each sound effect
    public void bulletHitBrick() {
        playClip(bulletBrickSE);
    }

    public void bulletHitTank() {
        playClip(bulletTankSE);
    }

    public void fireSound() {
        playClip(fireSE);
    }

    public void explosion1() {
        playClip(explosion1SE);
    }

    public void explosion2() {
        playClip(explosion2SE);
    }

    public void startStage() {
        playClip(startStageSE);
    }

    public void pause() {
        playClip(pauseSE);
    }

    public void powerupAppear() {
        playClip(powerupAppearSE);
    }

    public void powerupPick() {
        playClip(powerupPickSE);
    }

    public void gameOver() {
        playClip(gameoverSE);
    }

    public void statistics() {
        playClip(statisticsSE);
    }

    /**
     * Plays a sound clip if it is initialized.
     */
    private void playClip(Clip clip) {
        if (initialized && clip != null) {
            clip.loop(1);
        } else if (!initialized) {
            initialize();
            if (clip != null) {
                clip.loop(1);
            }
        }
    }
}
