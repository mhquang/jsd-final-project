package src.animation;

public class TankSpawn extends Animation {

    public TankSpawn(int x, int y) {
        super(x, y);
        loadImage("image/animations/appear_1.png");
        getImageDimensions();
    }

    @Override
    public void updateAnimation() {
        if ((System.currentTimeMillis() - initialTime) > 100) {
            loadImage("image/animations/appear_2.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 200)) {
            loadImage("image/animations/appear_3.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 300)) {
            loadImage("image/animations/appear_4.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 400)) {
            loadImage("image/animations/appear_1.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 500)) {
            loadImage("image/animations/appear_2.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 600)) {
            loadImage("image/animations/appear_3.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 700)) {
            loadImage("image/animations/appear_4.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 800)) {
            super.setVisible(false);
        }
    }
}
