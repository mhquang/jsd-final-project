package src.powerups;

import src.Sprite;

/**
 * PowerUp class has an updateAnimation method which is used to make powerUps
 * flash before being removed
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 * @author Adrian Berg
 */
public class PowerUp extends Sprite {
    long loadTime;
    private int type;
    boolean flip = false;
    String s;

    public PowerUp(int x, int y) {
        super(x, y);
        loadTime = System.currentTimeMillis();
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public long getLoadTime() {
        return loadTime;
    }

    public void updateAnimation() {
        long timeDifference = (System.currentTimeMillis() - loadTime);
        if (timeDifference > 5000) {

            if (timeDifference % 10 == 0 && flip == false) {
                loadImage("");
                getImageDimensions();
                flip = true;
            } else if (timeDifference % 10 == 0 && flip == true) {
                loadImage(s);
                getImageDimensions();
                flip = false;
            }
        }
    }

}
