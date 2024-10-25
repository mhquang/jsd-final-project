package src.animation;

/**
 * Class that handles the exploding animations of the tanks Exploding tank is
 * extended from Explosion which is extended from Animation This class is
 * responsible for loading images of an exploding tank
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 *
 * @author Adrian Berg
 */
public class ExplodingTank extends Explosion {

    private long initialTime = System.currentTimeMillis();

    /**
     * Constructor for the ExlodingTank class
     *
     * @param x coordinate of the tank
     * @param y coordinate of the tank
     */
    public ExplodingTank(int x, int y) {
        super(x - 8, y - 8);
        loadImage("image/animations/big_explosion_1.png");
        getImageDimensions();
    }

    @Override
    public void updateAnimation() {
        if ((System.currentTimeMillis() - initialTime) > 125) {
            loadImage("image/animations/big_explosion_2.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 500)) {
            loadImage("image/animations/big_explosion_3.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 700)) {
            loadImage("image/animations/big_explosion_4.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 900)) {
            loadImage("image/animations/big_explosion_5.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 1100)) {
            super.setVisible(false);
        }
    }

}
