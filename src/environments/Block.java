package src.environments;

import src.Sprite;

public class Block extends Sprite {
    public int health = 1;
    private int type;

    public Block(int x, int y) {
        super(x, y);
    }

    public void lowerHealth() {
        health -= 1;
    }

    public int currentHealth() {
        return health;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void updateAnimation() {

    }

}
