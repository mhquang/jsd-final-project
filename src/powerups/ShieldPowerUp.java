package src.powerups;

/**
 * ShieldPowerUp extends PowerUp and sets the type at 12
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 * @author Adrian Berg
 */
public class ShieldPowerUp extends PowerUp {
    public ShieldPowerUp(int x, int y) {
        super(x, y);
        loadImage("image/powerup_helmet.png");
        getImageDimensions();
        setType(12);
        s = "image/powerup_helmet.png";
    }

}
