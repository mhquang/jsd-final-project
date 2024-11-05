package src.powerups;

public class ShovelPowerUp extends PowerUp {
    public ShovelPowerUp(int x, int y) {
        super(x, y);
        loadImage("image/power_ups/powerup_shovel.png");
        getImageDimensions();
        setType(11);
        s = "image/power_ups/powerup_shovel.png";
    }

}
