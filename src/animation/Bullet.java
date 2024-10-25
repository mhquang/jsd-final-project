package src.animation;

import src.Map;
import src.Sprite;

/**
 * Bullet is a Sprite. Direction refers to which way the bullet is facing North
 * East South and West refer to 0/1/2/3 respectively Bullets can move with a pre
 * defined speed. Additionally, bullets can be upgraded to destroy steel bricks.
 * Bullets can be spawned either by player tank or enemy tank, this boolean is
 * helpful to stop tanks from killing themselves on the bullets that they spawn
 * Furthermore for future
 *
 * @param int     x represents the starting x location in pixels
 * @param int     y represents the starting y location in pixels
 * @param int     direction represents the direction the bullet is facing
 *                North/East/South/West correspond to 0/1/2/3 respectively
 * @param boolean Enemy represents whether an enemy tank (= true) or player tank
 *                (=false) created a bullet
 * @author Adrian Berg
 */
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
            loadImage("image/bullets/bullet_up.png");
        }
        if (direction == 1) {
            loadImage("image/bullets/bullet_right.png");
        }
        if (direction == 2) {
            loadImage("image/bullets/bullet_down.png");
        }
        if (direction == 3) {
            loadImage("image/bullets/bullet_left.png");
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
