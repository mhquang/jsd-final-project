package src.powerups;

public class BombPowerUp extends PowerUp {
    public BombPowerUp(int x, int y) {
        super(x, y);
        loadImage("assets/image/power_ups/powerup_grenade.png");
        getImageDimensions();
        setType(9);
        s = "assets/image/power_ups/powerup_grenade.png";
    }

}
