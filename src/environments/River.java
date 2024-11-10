package src.environments;

public class River extends Block {

    long lastImage = 0;
    boolean lastLoad = false;

    @Override
    public void updateAnimation() {
        if ((System.currentTimeMillis() - lastImage) > 500) {

            if (lastLoad) {
                loadImage("assets/image/environments/water_1.png");
                lastImage = System.currentTimeMillis();
                lastLoad = false;
            } else {
                loadImage("assets/image/environments/water_2.png");
                lastImage = System.currentTimeMillis();

                lastLoad = true;
            }
        }

    }

    public River(int x, int y) {
        super(x, y);
        loadImage("assets/image/environments/water_1.png");
        getImageDimensions();
        setHealth(1);
        setType(4);
    }

}
