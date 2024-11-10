package src.powerups;

public class ShieldPowerUp extends PowerUp {
    public ShieldPowerUp(int x, int y) {
        super(x, y);
        loadImage("assets/image/power_ups/powerup_helmet.png");
        getImageDimensions();
        setType(12);
        s = "assets/image/power_ups/powerup_helmet.png";
    }

}
