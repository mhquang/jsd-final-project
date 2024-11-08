package src.animation;

import src.tanks.PlayerTank;

public class TankShield extends Animation {
    long initialTime = System.currentTimeMillis();
    private PlayerTank playerTank;
    private boolean flip = false;
    private int type;

    public TankShield(PlayerTank atank, int type) {
        super(atank.getX(), atank.getY());
        playerTank = atank;
        loadImage("assets/image/animations/shield_1.png");
        getImageDimensions();
        this.type = type;
    }

    @Override
    public void updateAnimation() {
        int shieldTime;
        if (type == 1) {
            shieldTime = 10000;
        } else {
            shieldTime = 3000;
        }
        super.setX(playerTank.getX());
        super.setY(playerTank.getY());
        long timeDifference = (System.currentTimeMillis() - initialTime);
        if (timeDifference % 10 == 0 && !flip) {
            loadImage("assets/image/animations/shield_1.png");
            getImageDimensions();
            flip = true;
        } else if (timeDifference % 10 == 0 && flip) {
            loadImage("assets/image/animations/shield_2.png");
            getImageDimensions();
            flip = false;
        }
        if ((System.currentTimeMillis() - initialTime > shieldTime)) {
            playerTank.setShield(false);
            super.setVisible(false);
        }
    }
}
