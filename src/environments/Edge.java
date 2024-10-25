package src.environments;

/**
 * Edge class
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 * @author Tongyu
 */
public class Edge extends Block {
    public Edge(int x, int y) {
        super(x, y);
        loadImage("image/environments/edge.png");
        getImageDimensions();
        setType(6);
        setHealth(1);
    }
}
