package src.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SwitchButton extends Component {
    private float location;
    private boolean selected = true;
    private boolean mouseOver;
    private final float speed = 1f;

    public SwitchButton() {
        setBackground(new Color(0, 174, 255));
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        location = -1;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (mouseOver) {
                        selected = !selected;
                        new Thread(() -> animateSwitch()).start();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                mouseOver = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseOver = false;
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        int endLocation = width - height + 2;
        if (location < 0) {
            location = selected ? endLocation : 2;
        }

        float alpha = getAlpha();
        if (alpha < 1) {
            graphics2D.setColor(Color.GRAY);
            graphics2D.fillRoundRect(0, 0, width, height, 25, 25);
        }
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        graphics2D.setColor(getBackground());
        graphics2D.fillRoundRect(0, 0, width, height, 25, 25);
        graphics2D.setColor(getForeground());
        graphics2D.setComposite(AlphaComposite.SrcOver);
        graphics2D.fillRoundRect((int) location, 2, height - 4, height - 4, 21, 21);

        super.paint(g);
    }

    public boolean isSelected() {
        return selected;
    }

    private float getAlpha() {
        float width = getWidth() - getHeight();
        float alpha = (location - 2) / width;

        if (alpha < 0) {
            alpha = 0;
        }
        if (alpha > 1) {
            alpha = 1;
        }
        return alpha;
    }

    private void animateSwitch() {
        int endLocation = getWidth() - getHeight() + 2;
        int startLocation = 2;

        while (selected ? location < endLocation : location > startLocation) {
            location += selected ? speed : -speed;
            location = Math.min(Math.max(location, startLocation), endLocation); // Ensure bounds

            repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        location = selected ? endLocation : startLocation;
        repaint();
    }
}
