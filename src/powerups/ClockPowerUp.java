package src.powerups;

public class ClockPowerUp extends PowerUp {
    public ClockPowerUp(int x, int y) {
        super(x, y);
        loadImage("image/power_ups/powerup_timer.png");
        getImageDimensions();
        setType(10);
        s = "image/powerup_timer.png";
    }

}
