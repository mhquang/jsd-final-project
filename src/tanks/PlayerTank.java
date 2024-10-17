package src.tanks;

import src.animation.Bullet;
import src.utils.SoundUtility;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Tank extends Sprite The Tank represents the player in the game. The tank has
 * an array of bullets and is capable of moving and firing bullets depending on
 * key events (arrow keys for movement and space bar for firing bullets) in four
 * different directions. The tank also can be upgraded in several ways
 * increasing the firing speed movement speed and the ability to break steel
 * blocks
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 * @param int lives is the lives which the tank posses
 * @author Adrian Berg
 */
public class PlayerTank extends Tank {

    private long lastFired = 0;
    private int lives;
    private boolean shield = false;
    private int starLevel = 0;

    public PlayerTank(int x, int y, int lives) {
        super(x, y, 2);
        loadImage("image/playerTank_up.png");
        getImageDimensions();
        this.lives = lives;
    }

    public boolean isShield() {
        return shield;
    }

    public void setShield(boolean shield) {
        this.shield = shield;
    }

    public int getLives() {
        return this.lives;
    }

    public int getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(int starLevel) {
        this.starLevel = starLevel;
    }

    public void upStarLevel() {
        starLevel += 1;
    }

    public void upLives() {
        this.lives += 1;
    }

    public void upHealth() {
        this.setHealth(this.getHealth() + 1);
    }

    public void downHealth() {
        if (!shield) {
            this.setHealth(this.getHealth() - 1);
        }
    }

    public void fire() {
        if (canFire()) {
            Bullet bullet = createBullet();
            if (starLevel == 3) bullet.upgrade();
            this.getBullets().add(bullet);
            SoundUtility.fireSound();
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int time = (starLevel == 0) ? 700 : 250;

        if (key == KeyEvent.VK_SPACE && (System.currentTimeMillis() - lastFired) > time) {
            fire();
            lastFired = System.currentTimeMillis();
        } else if (key == KeyEvent.VK_LEFT) {
            this.setDx(-1);
            this.setDy(0);
            if (starLevel > 1) {
                this.setDx(-2);
            }
            ImageIcon ii = new ImageIcon("image/playerTank_left.png");
            image = ii.getImage();
            this.setDirection(3);
        } else if (key == KeyEvent.VK_RIGHT) {
            this.setDx(1);
            this.setDy(0);
            if (starLevel > 1) {
                this.setDx(2);
            }
            ImageIcon ii = new ImageIcon("image/playerTank_right.png");
            image = ii.getImage();
            this.setDirection(1);
        } else if (key == KeyEvent.VK_UP) {
            ImageIcon ii = new ImageIcon("image/playerTank_up.png");
            image = ii.getImage();
            this.setDx(0);
            this.setDy(-1);
            if (starLevel > 1) {
                this.setDy(-2);
            }
            this.setDirection(0);
        } else if (key == KeyEvent.VK_DOWN) {
            ImageIcon ii = new ImageIcon("image/playerTank_down.png");
            image = ii.getImage();
            this.setDx(0);
            this.setDy(1);
            if (starLevel > 1) {
                this.setDy(2);
            }
            this.setDirection(2);
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            this.setDx(0);
        }

        if (key == KeyEvent.VK_RIGHT) {
            this.setDx(0);
        }

        if (key == KeyEvent.VK_UP) {
            this.setDy(0);
        }

        if (key == KeyEvent.VK_DOWN) {
            this.setDy(0);
        }
    }

    private boolean canFire() {
        return (System.currentTimeMillis() - lastFired) > ((starLevel == 0) ? 700 : 250);
    }

    private Bullet createBullet() {
        return switch (this.getDirection()) {
            case 0 -> new Bullet(x + width / 3, y, 0, false);
            case 1 -> new Bullet(x + width - 3, y + height / 3, 1, false);
            case 2 -> new Bullet(x + width / 3, y + height - 3, 2, false);
            case 3 -> new Bullet(x, y + height / 3, 3, false);
            default -> null;
        };
    }
}
