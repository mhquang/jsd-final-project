package src.environments;

/**
 * River is a block which represents the river blocks
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 *
 * @author Adrian Berg
 */
public class River extends Block {

    long lastImage = 0;
    boolean lastLoad = false;

    @Override
    public void updateAnimation() {
        if ((System.currentTimeMillis() - lastImage) > 500) {

            if (lastLoad) {
                loadImage("image/environments/water_1.png");
                lastImage = System.currentTimeMillis();
                lastLoad = false;
            } else {
                loadImage("image/environments/water_2.png");
                lastImage = System.currentTimeMillis();

                lastLoad = true;
            }
        }

    }

    public River(int x, int y) {
        super(x, y);
        loadImage("image/environments/water_1.png");
        getImageDimensions();
        setHealth(1);
        setType(4);
    }

}
