package src.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class SwitchButton extends Component {
    private final Timer timer;
    private float location;
    private boolean selected = true;
    private boolean mouseOver;
    private final float speed = 1f;

    public SwitchButton() {
        setBackground(new Color(0, 174, 255));
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        location = -1;
        timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int endLocation = getWidth() - getHeight() + 2;
                if (selected) {
                    if (location < endLocation) {
                        location += speed;
                        repaint();
                    } else {
                        timer.stop();
                        location = endLocation;
                        repaint();
                    }
                } else {
                    int startLocation = 2;
                    if (location > startLocation) {
                        location -= speed;
                        repaint();
                    } else {
                        timer.stop();
                        location = startLocation;
                        repaint();
                    }
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (mouseOver) {
                        selected = !selected;
                        timer.start();
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
}
