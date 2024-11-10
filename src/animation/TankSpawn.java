package src.animation;

public class TankSpawn extends Animation {

    public TankSpawn(int x, int y) {
        super(x, y);
        loadImage("assets/image/animations/appear_1.png");
        getImageDimensions();
    }

    @Override
    public void updateAnimation() {
        if ((System.currentTimeMillis() - initialTime) > 100) {
            loadImage("assets/image/animations/appear_2.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 200)) {
            loadImage("assets/image/animations/appear_3.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 300)) {
            loadImage("assets/image/animations/appear_4.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 400)) {
            loadImage("assets/image/animations/appear_1.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 500)) {
            loadImage("assets/image/animations/appear_2.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 600)) {
            loadImage("assets/image/animations/appear_3.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 700)) {
            loadImage("assets/image/animations/appear_4.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 800)) {
            super.setVisible(false);
        }
    }
}
