package src.powerups;

public class TankPowerUp extends PowerUp {
    public TankPowerUp(int x, int y) {
        super(x, y);
        loadImage("assets/image/power_ups/powerup_tank.png");
        getImageDimensions();
        setType(7);
        s = "assets/image/power_ups/powerup_tank.png";
    }

}
