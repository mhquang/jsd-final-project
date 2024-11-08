package src.animation;

public class ExplodingTank extends Explosion {

    private long initialTime = System.currentTimeMillis();

    public ExplodingTank(int x, int y) {
        super(x - 8, y - 8);
        loadImage("assets/image/animations/big_explosion_1.png");
        getImageDimensions();
    }

    @Override
    public void updateAnimation() {
        if ((System.currentTimeMillis() - initialTime) > 125) {
            loadImage("assets/image/animations/big_explosion_2.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 500)) {
            loadImage("assets/image/animations/big_explosion_3.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 700)) {
            loadImage("assets/image/animations/big_explosion_4.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 900)) {
            loadImage("assets/image/animations/big_explosion_5.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 1100)) {
            super.setVisible(false);
        }
    }

}
