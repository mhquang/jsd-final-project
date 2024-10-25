package src.environments;

/**
 * Tree is a block with type 5 and health 1
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 * @author Adrian Berg
 */
public class Tree extends Block {
    public Tree(int x, int y) {
        super(x, y);
        loadImage("image/environments/trees.png");
        getImageDimensions();
        setType(5);
        setHealth(1);
    }

}
