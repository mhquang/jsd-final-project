package src.environments;

public class Tree extends Block {
    public Tree(int x, int y) {
        super(x, y);
        loadImage("assets/image/environments/trees.png");
        getImageDimensions();
        setType(5);
        setHealth(1);
    }

}
