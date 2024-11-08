package src.environments;

public class Brick extends Block {
    public Brick(int x, int y) {
        super(x, y);
        loadImage("assets/image/environments/wall_brick.png");
        getImageDimensions();
        setType(1);
        setHealth(1);
    }

}
