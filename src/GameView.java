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
        getGamePanel().removeAll();

        Menu menu = Menu.getInstance(this, 50);
        getGamePanel().add(menu);
        menu.setMenuStatus(true);

        menu.revalidate();
        menu.repaint();

        menu.requestFocusInWindow();

        setVisible(true);
    }

    private void addMenu() {
        Menu menu = Menu.getInstance(this, Map.BOARD_HEIGHT);
        gamePanel.add(menu);
        menu.setVisible(true);
    }
}
