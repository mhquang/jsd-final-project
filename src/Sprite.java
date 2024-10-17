package src;

import javax.swing.*;
import java.awt.*;

public class Sprite {
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean visible;
    public Image image;

    public Sprite(int x, int y) {
        this.x = x;
        this.y = y;
        visible = true;
    }

    protected void getImageDimensions() {
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    protected void loadImage(String imageName) {
        ImageIcon i = new ImageIcon(imageName);
        image = i.getImage();
    }

    public Image getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    // for collision detect
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
