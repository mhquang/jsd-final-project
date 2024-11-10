package src;

import javax.swing.*;
import java.awt.*;

public class Sprite {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean visible;
    private Image image;

    public Sprite(int x, int y) {
        this.x = x;
        this.y = y;
        visible = true;
    }

    public void getImageDimensions() {
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public void loadImage(String imageName) {
        ImageIcon i = new ImageIcon(imageName);
        image = i.getImage();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    // for collision detect
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
