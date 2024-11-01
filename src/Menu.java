package src;

import src.utils.BoardUtility;
import src.utils.ImageUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Menu extends JPanel implements ActionListener, KeyListener {
    // Load the images from ImageUtility class
    private Image title, tank;
    private final GameView theView;
    private int yPos;
    private int direction = -1;
    private final int stopYPos = 50;
    private static boolean menuStatus = true;
    private final ImageUtility imageInstance = ImageUtility.getInstance();
    private int selectedItem = 0;  // Tracks which menu item is selected
    private final String[] menuItems = {"1 PLAYER", "2 PLAYERS", "SETTING", "HELP"}; // Menu items
    private boolean isBackFromScoreBoard;

    public Menu(GameView theView, int yPos) {
        this.theView = theView;
        this.yPos = yPos;
        this.isBackFromScoreBoard = false;
        this.setBackground(Color.BLACK);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.setLayout(null);
        loadMenuImages();
        addTimer();
    }

    /**
     * Return if the game is showing the menu
     *
     * @return a boolean
     */
    public static boolean getMenuStatus() {
        return menuStatus;
    }

    public void setBackFromScoreBoard(boolean isBackFromScoreBoard) {
        this.isBackFromScoreBoard = isBackFromScoreBoard;
        if (isBackFromScoreBoard) {
            this.isBackFromScoreBoard = false;
            menuStatus = true;
            this.requestFocusInWindow();
            this.revalidate();
            this.repaint();
        }
    }

    private void addTimer() {
        if (!isBackFromScoreBoard && yPos != stopYPos) {
            Timer timer = new Timer(10, e -> {
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
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.start();
        }

    }

    private void loadMenuImages() {
        title = imageInstance.getBackground();
        tank = imageInstance.getTank();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Font font = BoardUtility.loadFont();
        g.setFont(font);
        g.setColor(Color.WHITE);

        // Draw the title image centered
        g.drawImage(title,
                Map.BOARD_WIDTH / 2 - title.getWidth(null) / 2 + 10,  // Center horizontally
                yPos, this);

        // Draw menu when yPos reaches stopYPos
        if (yPos == stopYPos) {
            // Calculate initial yPos for the first menu item
            int initialYPos = yPos + title.getHeight(null) + 30;
            int spacing = 35;

            // Draw the menu items centered
            // Vertical space between menu items
            for (int i = 0; i < menuItems.length; i++) {
                int xPosition = (Map.BOARD_WIDTH - g.getFontMetrics().stringWidth(menuItems[i])) / 2 + 10;
                g.drawString(menuItems[i], xPosition, initialYPos + i * spacing);
            }

            // Draw the tank above the selected menu item
            int tankYPos = initialYPos + selectedItem * spacing - 20; // Adjust tank's position
            g.drawImage(tank, Map.BOARD_WIDTH / 2 - tank.getWidth(null) / 2 - 90, tankYPos, this);

            g.drawString("PRESS ENTER",
                    Map.BOARD_WIDTH / 2 - 70,
                    Map.BOARD_HEIGHT * 4 / 5 + 90);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Handle menu navigation
        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            selectedItem++;
            if (selectedItem >= menuItems.length) {
                selectedItem = 0; // Loop back to the top
            }
            repaint(); // Refresh the menu to show the tank's new position
        } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            selectedItem--;
            if (selectedItem < 0) {
                selectedItem = menuItems.length - 1; // Loop back to the bottom
            }
            repaint(); // Refresh the menu to show the tank's new position
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (menuStatus) {
                if (selectedItem == 0) {
                    loadBoard(false);
                } else if (selectedItem == 1) {
                    // 2 players mode
                    loadBoard(true);
                } else if (selectedItem == 2) {
                    System.out.println(menuItems[2]);
                } else if (selectedItem == 3) {
                    System.out.println(menuItems[3]);
                }

            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Load the board to the game panel on the JFrame of the game.
     */
    private void loadBoard(boolean isTwoPlayerMode) {
        // Change the menu status
        menuStatus = false;

        // Clear previous components
        theView.getGamePanel().removeAll();

        // Create and add a new Board panel
        Board panel = new Board(theView, isTwoPlayerMode);
        theView.getGamePanel().add(panel);

        // Refresh and repaint the panel
        panel.revalidate();
        panel.repaint();

        // Request focus on the new Board panel
        panel.requestFocusInWindow();

        // Set the main view visible
        theView.setVisible(true);
    }
}
