package src;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {

    private JPanel gamePanel;

    public GameView() {
        initComponents();
        setLocationRelativeTo(null);
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

    private void initComponents() {

        gamePanel = new JPanel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Battle City");
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
}
