package src.animation;

import src.Map;
import src.Sprite;

public class Bullet extends Sprite {
    private final int BULLET_SPEED = 3;
    private final int BOARD_WIDTH = Map.BOARD_WIDTH;
    private final int BOARD_HEIGHT = Map.BOARD_HEIGHT;
    private int direction;
    private boolean upgrade = false;
    public boolean isEnemy;

    public Bullet(int x, int y, int direction, boolean Enemy) {
        super(x, y);
        this.direction = direction;
        if (direction == 0) {
            loadImage("assets/image/bullets/bullet_up.png");
        }
        if (direction == 1) {
            loadImage("assets/image/bullets/bullet_right.png");
        }
        if (direction == 2) {
            loadImage("assets/image/bullets/bullet_down.png");
        }
        if (direction == 3) {
            loadImage("assets/image/bullets/bullet_left.png");
        }
        isEnemy = Enemy;
        getImageDimensions();
    }

    public void move() {
        if (direction == 0) {
            setY(getY() - BULLET_SPEED);
        } else if (direction == 1) {
            setX(getX() + BULLET_SPEED);
        } else if (direction == 2) {
            setY(getY() + BULLET_SPEED);
        } else if (direction == 3) {
            setX(getX() - BULLET_SPEED);
        }
        if (getX() > BOARD_WIDTH + 100 + getWidth()) {
            setVisible(false);
        }
        if (getX() < -getWidth() - 100) {
            setVisible(false);
        }
        if (getY() > BOARD_HEIGHT + getHeight() + 100) {
            setVisible(false);
        }
        if (getY() < -getHeight() - 100) {
            setVisible(false);
        }
    }

    public void upgrade() {
        this.upgrade = true;
    }

    public boolean getUpgrade() {
        return this.upgrade;
    }

}
