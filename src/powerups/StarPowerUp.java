package src.powerups;

public class StarPowerUp extends PowerUp {
    public StarPowerUp(int x, int y) {
        super(x, y);
        loadImage("image/power_ups/powerup_star.png");
        getImageDimensions();
        setType(8);
        s = "image/power_ups/powerup_star.png";
    }

}
