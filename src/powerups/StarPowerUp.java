package src.powerups;

public class StarPowerUp extends PowerUp {
    public StarPowerUp(int x, int y) {
        super(x, y);
        loadImage("assets/image/power_ups/powerup_star.png");
        getImageDimensions();
        setType(8);
        s = "assets/image/power_ups/powerup_star.png";
    }

}
