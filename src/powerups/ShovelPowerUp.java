package src.powerups;

public class ShovelPowerUp extends PowerUp {
    public ShovelPowerUp(int x, int y) {
        super(x, y);
        loadImage("assets/image/power_ups/powerup_shovel.png");
        getImageDimensions();
        setType(11);
        s = "assets/image/power_ups/powerup_shovel.png";
    }

}
