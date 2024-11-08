package src.tanks;

import src.animation.Bullet;
import src.utils.CollisionUtility;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class NPCTank extends Tank {
    private boolean powerUp;
    private String difficulty;
    private String type;
    private int dirTimer = 0;
    private int dirUpdateInterval;
    private int fireTimer = 0;
    private int fireUpdateInterval;
    private double speedConst;
    public boolean frozen = false;
    public long frozenStartTime;
    private String imageUp;
    private String imageDown;
    private String imageLeft;
    private String imageRight;

    public NPCTank(int x, int y, String difficulty, String type, boolean powerUp) {
        super(x, y, getHealthBasedOnType(type));
        this.setVisible(true);
        this.powerUp = powerUp;
        this.difficulty = difficulty;
        this.type = type;
        this.setUp();
        this.imageSetUp();
    }

    public String getDifficulty() {
        return difficulty;
    }

    public boolean hasPowerUp() {
        return powerUp;
    }

    public void decreaseHP() {
        this.setHealth(this.getHealth() - 1);
    }

    public String getType() {
        return type;
    }

    public void fire() {
        Bullet aBullet = switch (this.getDirection()) {
            case 0 -> new Bullet(getX() + getWidth() / 3, getY(), 0, true);
            case 1 -> new Bullet(getX() + getWidth(), getY() + getHeight() / 3, 1, true);
            case 2 -> new Bullet(getX() + getWidth() / 3, getY() + getHeight(), 2, true);
            default -> new Bullet(getX(), getY() + getHeight() / 3, 3, true);
        };
        if (!frozen) {
            this.getBullets().add(aBullet);
        }
    }

    public void randomDir() {
        Random randomDir = new Random();
        this.setDirection(randomDir.nextInt(4));
        dirUpdate();
    }

    public void toTankDir(PlayerTank playerTank) {
        int tankX = playerTank.getX();
        int tankY = playerTank.getY();
        Random randomDir = new Random();
        if (randomDir.nextBoolean()) {
            if (this.getY() > tankY) {
                this.setDirection(0);
            } else {
                this.setDirection(2);
            }
        } else if (this.getX() > tankX) {
            this.setDirection(3);
        } else if (this.getX() < tankX) {
            this.setDirection(1);
        } else {
            this.setDirection(3);
        }
        dirUpdate();
    }

    public void toEagleDir() {
        if (this.getX() > 14 * 16) {
            this.setDirection(3);
        } else if (this.getX() < 14 * 16) {
            this.setDirection(1);
        } else {
            this.setDirection(2);
        }
        dirUpdate();
    }

    public void actionEasy() {
        if (this.dirTimer >= this.dirUpdateInterval) {
            randomDir();
            this.dirTimer = 0;
        } else {
            this.dirTimer++;
        }
        this.move();
        if (this.fireTimer >= this.fireUpdateInterval) {
            this.fire();
            this.fireTimer = 0;
        } else {
            this.fireTimer++;
        }
    }

    public void actionNormal(PlayerTank playerTank) {
        Random randomDir = new Random();
        if (this.dirTimer >= this.dirUpdateInterval) {
            int random = randomDir.nextInt(20);
            if (random % 8 == 1) {
                toEagleDir();
            } else if (random % 4 == 0) {
                randomDir();
            } else {
                toTankDir(playerTank);
            }
            this.dirTimer = 0;
        } else {
            this.dirTimer++;
        }
        this.move();
        Rectangle theTank = new Rectangle(getX() + this.getDx(), getY() + this.getDy(), getWidth(), getHeight());

        updateFire(randomDir, theTank);
    }

    public void actionHard(PlayerTank playerTank) {
        Random randomDir = new Random();
        if (this.dirTimer >= this.dirUpdateInterval) {
            int random = randomDir.nextInt(7);
            if (random % 5 == 0) {
                toEagleDir();
            } else if (random % 6 == 1) {
                randomDir();
            } else {
                toTankDir(playerTank);
            }
            this.dirTimer = 0;
        } else {
            this.dirTimer++;
        }
        Rectangle theTank = new Rectangle(getX() + this.getDx(), getY() + this.getDy(), getWidth(), getHeight());
        this.move();
        updateFire(randomDir, theTank);
    }

    private void setUp() {
        // set up speed
        switch (this.type) {
            case "basic", "armor", "power":
                this.speedConst = 1;
                break;
            case "fast":
                this.speedConst = 2;
                break;
        }

        // Set update intervals based on difficulty
        switch (this.type) {
            case "basic", "armor", "fast" -> setInterval();
            case "power" -> {
                switch (difficulty) {
                    case "easy" -> {
                        dirUpdateInterval = 30;
                        fireUpdateInterval = 40;
                    }
                    case "normal" -> {
                        dirUpdateInterval = 30;
                        fireUpdateInterval = 35;
                    }
                    case "hard" -> {
                        dirUpdateInterval = 30;
                        fireUpdateInterval = 30;
                    }
                }
            }
        }
    }

    private void setInterval() {
        switch (difficulty) {
            case "easy" -> {
                dirUpdateInterval = 30;
                fireUpdateInterval = 80;
            }
            case "normal" -> {
                dirUpdateInterval = 30;
                fireUpdateInterval = 75;
            }
            case "hard" -> {
                dirUpdateInterval = 30;
                fireUpdateInterval = 70;
            }
        }
    }

    private void imageSetUp() {
        String basePath = "assets/image/npc_tanks/";
        switch (this.type) {
            case "basic":
                this.imageUp = basePath + "tank_basic_up.png";
                this.imageDown = basePath + "tank_basic_down.png";
                this.imageLeft = basePath + "tank_basic_left.png";
                this.imageRight = basePath + "tank_basic_right.png";
                break;
            case "armor":
                this.imageUp = basePath + "tank_armor_up.png";
                this.imageDown = basePath + "tank_armor_down.png";
                this.imageLeft = basePath + "tank_armor_left.png";
                this.imageRight = basePath + "tank_armor_right.png";
                break;
            case "power":
                this.imageUp = basePath + "tank_power_up.png";
                this.imageDown = basePath + "tank_power_down.png";
                this.imageLeft = basePath + "tank_power_left.png";
                this.imageRight = basePath + "tank_power_right.png";
                break;
            case "fast":
                this.imageUp = basePath + "tank_fast_up.png";
                this.imageDown = basePath + "tank_fast_down.png";
                this.imageLeft = basePath + "tank_fast_left.png";
                this.imageRight = basePath + "tank_fast_right.png";
                break;
        }
        loadImage(this.imageUp);
        getImageDimensions();
    }

    private void dirUpdate() {
        ImageIcon ii;
        if (frozen) {
            this.setDx(0);
            this.setDy(0);
        } else {
            switch (this.getDirection()) {
                case 0:
                    ii = new ImageIcon(this.imageUp);
                    setImage(ii.getImage());
                    this.setDx((int) (0 * this.speedConst));
                    this.setDy((int) (-1 * this.speedConst));
                    break;
                case 1:
                    ii = new ImageIcon(this.imageRight);
                    setImage(ii.getImage());
                    this.setDx((int) (1 * this.speedConst));
                    this.setDy((int) (0 * this.speedConst));
                    break;
                case 2:
                    ii = new ImageIcon(this.imageDown);
                    setImage(ii.getImage());
                    this.setDx((int) (0 * this.speedConst));
                    this.setDy((int) (1 * this.speedConst));
                    break;
                case 3:
                    ii = new ImageIcon(this.imageLeft);
                    setImage(ii.getImage());
                    this.setDx((int) (-1 * this.speedConst));
                    this.setDy((int) (0 * this.speedConst));
                    break;
            }
        }
    }

    private void updateFire(Random randomDir, Rectangle theTank) {
        if (CollisionUtility.checkCollisionTankBlocks(theTank)) {
            if (randomDir.nextBoolean() && this.fireTimer < 3) {
                this.fire();
                this.fireTimer++;
            }
        }
        if (this.fireTimer >= this.fireUpdateInterval) {
            this.fire();
            this.fireTimer = 0;
        } else {
            this.fireTimer++;
        }
    }

    private static int getHealthBasedOnType(String type) {
        return switch (type) {
            case "armor" -> 4;  // Armor tanks have more health
            case "power" -> 2;
            default -> 1;  // Basic, fast tanks have 1 health
        };
    }
}
