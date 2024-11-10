package src.tanks;

import src.Map;
import src.Sprite;
import src.animation.Bullet;
import src.utils.CollisionUtility;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Tank extends Sprite {

    private final int BOARD_WIDTH = Map.BOARD_WIDTH;
    private final int BOARD_HEIGHT = Map.BOARD_HEIGHT;
    private int dx;
    private int dy;
    private int direction;
    private CopyOnWriteArrayList<Bullet> bullets;
    private int health;

    public Tank(int x, int y, int health) {
        super(x, y);
        this.health = health;
        bullets = new CopyOnWriteArrayList<>();
        direction = 0;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public CopyOnWriteArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void move() {
        Rectangle theTank = new Rectangle(getX() + dx, getY() + dy, getWidth(), getHeight());

        if (!CollisionUtility.checkCollisionTankBlocks(theTank)) {
            setX(clamp(getX() + dx, 1, BOARD_WIDTH - getWidth()));
            setY(clamp(getY() + dy, 1, BOARD_HEIGHT - getHeight()));
        }
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
