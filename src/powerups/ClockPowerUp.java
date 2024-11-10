package src.powerups;

public class ClockPowerUp extends PowerUp {
    public ClockPowerUp(int x, int y) {
        super(x, y);
        loadImage("assets/image/power_ups/powerup_timer.png");
        getImageDimensions();
        setType(10);
        s = "assets/image/powerup_timer.png";
    }

}
