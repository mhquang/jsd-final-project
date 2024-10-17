package src.SpriteClasses;

/**
 * Explosion extends Animation. This is the explosion used for bullets and
 * various types of blocks.
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 * @author Adrian Berg
 */
public class Explosion extends Animation {

    public Explosion(int x, int y) {
        super(x - 8, y - 8);
        loadImage("image/bullet_explosion_1.png");
        getImageDimensions();
    }

    @Override
    public void updateAnimation() {
        if ((System.currentTimeMillis() - initialTime) > 125) {
            loadImage("image/bullet_explosion_2.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 250)) {
            loadImage("image/bullet_explosion_3.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 300)) {
            super.visible = false;
        }

    }

}
