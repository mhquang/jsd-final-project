package src.screens;

import src.GameView;
import src.Map;
import src.utils.FontUtility;
import src.utils.ImageUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Menu extends JPanel implements ActionListener, KeyListener {
    private static Menu instance;
    private Image title, tank;
    private final GameView theView;
    private Board board;
    private int yPos;
    private int direction = -1;
    private final int stopYPos = 50;
    private static boolean menuStatus = true;
    private final ImageUtility imageInstance = ImageUtility.getInstance();
    private static final FontUtility fontUtility = FontUtility.getInstance();
    private int selectedItem = 0;  // Tracks which menu item is selected
    private final String[] menuItems = {"1 PLAYER", "2 PLAYERS", "SETTING", "HELP"}; // Menu items
    private boolean isBackFromScoreBoard;
    private boolean isTwoPlayerMode;

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

    public static Menu getInstance(GameView theView, int yPos) {
        if (instance == null) {
            instance = new Menu(theView, yPos);
        }
        return instance;
    }

    public void setMenuStatus(boolean menuStatus) {
        Menu.menuStatus = menuStatus;
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

        Font font = fontUtility.getPrstart();
        g.setFont(font);
        g.setColor(Color.WHITE);

        g.drawImage(title,
                theView.getWidth() / 2 - title.getWidth(null) / 2 - 10,  // Center horizontally
                yPos, this);

        if (yPos == stopYPos) {
            int initialYPos = yPos + title.getHeight(null) + 30;
            int spacing = 35;

            for (int i = 0; i < menuItems.length; i++) {
                int xPosition = (theView.getWidth() - g.getFontMetrics().stringWidth(menuItems[i])) / 2;
                g.drawString(menuItems[i], xPosition, initialYPos + i * spacing);
            }

            int tankYPos = initialYPos + selectedItem * spacing - 20;
            g.drawImage(tank, theView.getWidth() / 2 - tank.getWidth(null) / 2 - 100, tankYPos, this);

            g.drawString("PRESS ENTER",
                    theView.getWidth() / 2 - g.getFontMetrics().stringWidth("PRESS ENTER") / 2,
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
                    isTwoPlayerMode = false;
                    loadBoard();
                } else if (selectedItem == 1) {
                    // 2 players mode
                    isTwoPlayerMode = true;
                    loadBoard();
                } else if (selectedItem == 2) {
                    showSetting();
                } else if (selectedItem == 3) {
                    showHelp();
                }

            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void loadBoard() {
        menuStatus = false;

        theView.getGamePanel().removeAll();
        board = new Board(theView, isTwoPlayerMode);

        theView.getGamePanel().add(board);

        board.revalidate();
        board.repaint();

        board.requestFocusInWindow();

        theView.setVisible(true);
    }

    private void showHelp() {
        menuStatus = false;

        theView.getGamePanel().removeAll();

        Help helpPanel = Help.getInstance(theView);
        theView.getGamePanel().add(helpPanel);

        helpPanel.revalidate();
        helpPanel.repaint();

        helpPanel.requestFocusInWindow();

        theView.setVisible(true);
    }

    private void showSetting() {
        menuStatus = false;

        theView.getGamePanel().removeAll();

        Setting settingPanel = Setting.getInstance(theView);
        theView.getGamePanel().add(settingPanel);

        settingPanel.revalidate();
        settingPanel.repaint();

        settingPanel.requestFocusInWindow();

        theView.setVisible(true);
    }

}
