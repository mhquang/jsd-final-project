package src;

import src.screens.Menu;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {

    private JPanel gamePanel;

    public GameView() {
        initComponents();
        setLocationRelativeTo(null);
        addMenu();
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

    private void initComponents() {

        gamePanel = new JPanel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tank 1990");
        setName("mainFrame"); // NOI18N
        setPreferredSize(new Dimension(560, 520));
        setResizable(false);

        gamePanel.setLayout(new GridLayout(1, 0));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(gamePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(gamePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    public void showMenu() {
        // Clear previous components
        getGamePanel().removeAll();

        // Get the Menu singleton instance and add it to the GameView
        Menu menu = Menu.getInstance(this, 50); // Adjust yPos if needed
        getGamePanel().add(menu);
        menu.setMenuStatus(true);
        // Refresh and repaint the panel
        menu.revalidate();
        menu.repaint();

        // Request focus on the Menu panel
        menu.requestFocusInWindow();

        // Set the main view visible
        setVisible(true);
    }

    private void addMenu() {
        Menu menu = Menu.getInstance(this, Map.BOARD_HEIGHT);
        gamePanel.add(menu);
        menu.setVisible(true);
    }
}
