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

    private final int initialX;
    private final int initialY;
    private long lastFired = 0;
    private boolean shield = false;
    private int starLevel = 0;
    private boolean isPlayerOne;

    public PlayerTank(int x, int y, int health, boolean isPlayerOne) {
        super(x, y, health);
        initialX = x;
        initialY = y;
        this.isPlayerOne = isPlayerOne;
        loadImage(isPlayerOne ? "image/player_tank/playerTank_up.png" : "image/player_tank/player2Tank_up.png");
        getImageDimensions();
    }

    public boolean isShield() {
        return shield;
    }

    public void setShield(boolean shield) {
        this.shield = shield;
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

    public void upHealth() {
        this.setHealth(this.getHealth() + 1);
    }

    public void downHealth() {
        if (!shield) {
            this.setHealth(this.getHealth() - 1);
        }
    }

    public int getInitialX() {
        return initialX;
    }

    public int getInitialY() {
        return initialY;
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

        if (isPlayerOne) {
            handlePlayerOneControls(key, time);
        } else {
            handlePlayerTwoControls(key, time);
        }
    }

    private void handlePlayerTwoControls(int key, int time) {
        if (key == KeyEvent.VK_G && (System.currentTimeMillis() - lastFired) > time) {
            fire();
            lastFired = System.currentTimeMillis();
        } else if (key == KeyEvent.VK_A) {
            moveLeft();
        } else if (key == KeyEvent.VK_D) {
            moveRight();
        } else if (key == KeyEvent.VK_W) {
            moveUp();
        } else if (key == KeyEvent.VK_S) {
            moveDown();
        }
    }

    private void handlePlayerOneControls(int key, int time) {
        if (key == KeyEvent.VK_L && (System.currentTimeMillis() - lastFired) > time) {
            fire();
            lastFired = System.currentTimeMillis();
        } else if (key == KeyEvent.VK_LEFT) {
            moveLeft();
        } else if (key == KeyEvent.VK_RIGHT) {
            moveRight();
        } else if (key == KeyEvent.VK_UP) {
            moveUp();
        } else if (key == KeyEvent.VK_DOWN) {
            moveDown();
        }
    }

    private void moveLeft() {
        this.setDx(-1);
        this.setDy(0);
        if (starLevel > 1) this.setDx(-2);
        setImage(new ImageIcon(isPlayerOne ? "image/player_tank/playerTank_left.png" : "image/player_tank/player2Tank_left.png").getImage());
        this.setDirection(3);
    }

    private void moveRight() {
        this.setDx(1);
        this.setDy(0);
        if (starLevel > 1) this.setDx(2);
        setImage(new ImageIcon(isPlayerOne ? "image/player_tank/playerTank_right.png" : "image/player_tank/player2Tank_right.png").getImage());
        this.setDirection(1);
    }

    private void moveUp() {
        this.setDx(0);
        this.setDy(-1);
        if (starLevel > 1) this.setDy(-2);
        setImage(new ImageIcon(isPlayerOne ? "image/player_tank/playerTank_up.png" : "image/player_tank/player2Tank_up.png").getImage());
        this.setDirection(0);
    }

    private void moveDown() {
        this.setDx(0);
        this.setDy(1);
        if (starLevel > 1) this.setDy(2);
        setImage(new ImageIcon(isPlayerOne ? "image/player_tank/playerTank_down.png" : "image/player_tank/player2Tank_down.png").getImage());
        this.setDirection(2);
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (isPlayerOne) {
            handlePlayerOneKeyReleased(key);
        } else {
            handlePlayerTwoKeyReleased(key);
        }
    }

    private void handlePlayerOneKeyReleased(int key) {
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) this.setDx(0);
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) this.setDy(0);
    }

    private void handlePlayerTwoKeyReleased(int key) {
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_D) this.setDx(0);
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_S) this.setDy(0);
    }

    private boolean canFire() {
        return (System.currentTimeMillis() - lastFired) > ((starLevel == 0) ? 700 : 250);
    }

    private Bullet createBullet() {
        return switch (this.getDirection()) {
            case 0 -> new Bullet(getX() + getWidth() / 3, getY(), 0, false);
            case 1 -> new Bullet(getX() + getWidth() - 3, getY() + getHeight() / 3, 1, false);
            case 2 -> new Bullet(getX() + getWidth() / 3, getY() + getHeight() - 3, 2, false);
            case 3 -> new Bullet(getX(), getY() + getHeight() / 3, 3, false);
            default -> null;
        };
    }
}
