package src.powerups;

/**
 * StarPowerUp extends PowerUp and sets the type as 8
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 * @author Adrian Berg
 */
public class StarPowerUp extends PowerUp {
    public StarPowerUp(int x, int y) {
        super(x, y);
        loadImage("image/power_ups/powerup_star.png");
        getImageDimensions();
        setType(8);
        s = "image/power_ups/powerup_star.png";
    }

}
