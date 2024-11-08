package src.environments;

public class Edge extends Block {
    public Edge(int x, int y) {
        super(x, y);
        loadImage("assets/image/environments/edge.png");
        getImageDimensions();
        setType(6);
        setHealth(1);
    }
}
