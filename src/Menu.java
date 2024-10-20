package src;

import src.utils.ImageUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Menu extends JPanel implements ActionListener, KeyListener {
    // Load the images from ImageUtility class
    private Image title, tank, tree;
    private final GameView theView;
    private int yPos = Map.BOARD_HEIGHT;
    private int direction = -1;
    private final int stopYPos = 50;
    private static boolean menuStatus = true;
    private final ImageUtility imageInstance = ImageUtility.getInstance();

    private int selectedItem = 0;  // Tracks which menu item is selected
    private final String[] menuItems = {"1 PLAYER", "2 PLAYERS", "SETTING", "HELP"}; // Menu items

    public Menu(GameView theView) {
        this.theView = theView;
        this.setBackground(Color.BLACK);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.setLayout(null);
        loadMenuImages();
        addTimer();
    }

    private void addTimer() {
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yPos += direction;
                if (yPos == stopYPos) {
                    direction = 0;
                } else if (yPos + title.getHeight(null) > getHeight()) {
                    yPos = getHeight() - title.getHeight(
                            null);
                } else if (yPos < 0) {
                    yPos = 0;
                    direction *= -1;
                }
                repaint();
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.start();
    }

    private void loadMenuImages() {
        title = imageInstance.getBackground();
        tank = imageInstance.getTank();
        tree = imageInstance.getTree2();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Font font = loadFont();
        g.setFont(font);
        g.setColor(Color.WHITE);

        // Draw the title image centered
        g.drawImage(title,
                Map.BOARD_WIDTH / 2 - title.getWidth(null) / 2 - 15,  // Center horizontally
                yPos, this);

        // Draw menu when yPos reaches stopYPos
        if (yPos == stopYPos) {
            // Calculate initial yPos for the first menu item
            int initialYPos = yPos + title.getHeight(null) + 30;
            int spacing = 35;

            // Draw the menu items centered
            // Vertical space between menu items
            for (int i = 0; i < menuItems.length; i++) {
                int xPosition = (Map.BOARD_WIDTH - g.getFontMetrics().stringWidth(menuItems[i])) / 2 - 10;
                g.drawString(menuItems[i], xPosition, initialYPos + i * spacing);
            }

            // Draw the tank above the selected menu item
            int tankYPos = initialYPos + selectedItem * spacing - 20; // Adjust tank's position
            g.drawImage(tank, Map.BOARD_WIDTH / 2 - tank.getWidth(null) / 2 - 110, tankYPos, this);

            g.drawString("PRESS ENTER",
                    Map.BOARD_WIDTH / 2 - 90,
                    Map.BOARD_HEIGHT * 4 / 5 + 50);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    /**
     * Load the game font to the program
     *
     * @return font of the game
     */
    public static Font loadFont() {
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT,
                    new File("prstart.ttf"));
            font = font.deriveFont(java.awt.Font.PLAIN, 15);
            GraphicsEnvironment ge
                    = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return font;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Handle menu navigation
        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyChar() == 's') {
            selectedItem++;
            if (selectedItem >= menuItems.length) {
                selectedItem = 0; // Loop back to the top
            }
            repaint(); // Refresh the menu to show the tank's new position
        } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyChar() == 'w') {
            selectedItem--;
            if (selectedItem < 0) {
                selectedItem = menuItems.length - 1; // Loop back to the bottom
            }
            repaint(); // Refresh the menu to show the tank's new position
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (menuStatus) {
                loadBoard();
            }
        }
    }

    /**
     * Load the board to the game panel on the JFrame of the game.
     */
    public void loadBoard() {
        // Change the menu status
        menuStatus = false;

        // Clear previous components
        theView.getGamePanel().removeAll();

        // Create and add a new Board panel
        Board panel = new Board(theView);
        theView.getGamePanel().add(panel);

        // Refresh and repaint the panel
        panel.revalidate();
        panel.repaint();

        // Request focus on the new Board panel
        panel.requestFocusInWindow();

        // Set the main view visible
        theView.setVisible(true);

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Return if the game is showing the menu
     *
     * @return a boolean
     */
    public static boolean getMenuStatus() {
        return menuStatus;
    }
}
