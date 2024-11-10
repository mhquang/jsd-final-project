package src.animation;

public class Explosion extends Animation {

    public Explosion(int x, int y) {
        super(x - 8, y - 8);
        loadImage("assets/image/animations/bullet_explosion_1.png");
        getImageDimensions();
    }

    @Override
    public void updateAnimation() {
        if ((System.currentTimeMillis() - initialTime) > 125) {
            loadImage("assets/image/animations/bullet_explosion_2.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 250)) {
            loadImage("assets/image/animations/bullet_explosion_3.png");
            getImageDimensions();
        }
        if ((System.currentTimeMillis() - initialTime > 300)) {
            super.setVisible(false);
        }

    }

}
