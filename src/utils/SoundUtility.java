package src.utils;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoundUtility {
    private Clip bulletBrickSE, bulletTankSE, fireSE, explosion1SE, explosion2SE, startStageSE, pauseSE, powerupAppearSE, powerupPickSE, gameoverSE, statisticsSE;
    private boolean soundOn = true;
    private boolean initialized = false;
    private static SoundUtility instance;

    private SoundUtility() {
    }

    public static SoundUtility getInstance() {
        if (instance == null) {
            instance = new SoundUtility();
            instance.initialize();
        }
        return instance;
    }

    private void initialize() {
        try {
            bulletBrickSE = loadClip("assets/sound/bullet_hit_2.wav");
            bulletTankSE = loadClip("assets/sound/bullet_hit_1.wav");
            fireSE = loadClip("assets/sound/bullet_shot.wav");
            explosion1SE = loadClip("assets/sound/explosion_1.wav");
            explosion2SE = loadClip("assets/sound/explosion_2.wav");
            startStageSE = loadClip("assets/sound/stage_start.wav");
            pauseSE = loadClip("assets/sound/pause.wav");
            powerupAppearSE = loadClip("assets/sound/powerup_appear.wav");
            powerupPickSE = loadClip("assets/sound/powerup_pick.wav");
            gameoverSE = loadClip("assets/sound/game_over.wav");
            statisticsSE = loadClip("assets/sound/statistics_1.wav");
            initialized = true;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(SoundUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void mute() {
        soundOn = false;
    }

    public void unmute() {
        soundOn = true;
    }

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
        playClipWithPause(statisticsSE);
    }

    private Clip loadClip(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File soundFile = new File(filePath);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.setFramePosition(clip.getFrameLength());
        return clip;
    }

    private void playClip(Clip clip) {
        if (initialized && soundOn && clip != null) {
            clip.loop(1);
        }
    }

    private void playClipWithPause(Clip clip) {
        if (initialized && soundOn && clip != null) {
            Timer playTimer = new Timer((int) (clip.getMicrosecondLength() / 1000 + 2), e -> {
                clip.setFramePosition(0);
                clip.start();
            });

            playTimer.setRepeats(true);
            playTimer.start();

            new Timer(5000, e -> {
                playTimer.stop();
                clip.stop();
            }).start();
        }
    }

}
