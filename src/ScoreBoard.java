package src;

import src.utils.CollisionUtility;
import src.utils.ImageUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

import static src.Menu.loadFont;

public class ScoreBoard extends JPanel implements ActionListener, KeyListener {

    /**
     * Initialize instance variables for the ScoreBoard
     */
    private GameView theView;
    private int stage, totalTankNum;
    private int totalScore = 0;
    private final int SHIFT = 80;
    private JButton restartButton;
    private final ImageUtility imageInstance = ImageUtility.getInstance();
    private int[] tankScoreList = {0, 0, 0, 0};
    private int[] tankNumList = {0, 0, 0, 0};

    /**
     * Constructor for the ScoreBoard. A restart button is added for the player
     * to restart the game
     *
     * @param theView GameView that represents the frame of the game
     */
    public ScoreBoard(GameView theView) {
        this.theView = theView;
        this.setFocusable(true);
        theView.setForeground(Color.BLACK);
        this.setLayout(null);

        restartButton = new JButton();
        restartButton.setText("Restart");
        this.add(restartButton);
        restartButton.setBounds(400, 400,
                100, 30);
        restartButton.addActionListener(this);
    }

    /**
     * Draw the scoreBorad with different types of enemies on the screen.
     *
     * @param g Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        loadScore();
        stage = Board.getStage();
        super.paintComponent(g);
        Font font = loadFont();
        ArrayList<Image> tankList = new ArrayList<>(Arrays.asList(
                imageInstance.getTankBasic(),
                imageInstance.getTankFast(),
                imageInstance.getTankPower(),
                imageInstance.getTankArmor()));

        int panelWidth = getWidth();
        int xCenterShift = panelWidth / 2;

        // Stage and Player
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("STAGE   " + stage, xCenterShift - 60, 60);
        g.setColor(Color.RED);
        g.drawString("1-PLAYER", xCenterShift - 120, 95);

        // Total Score Display
        g.setColor(Color.ORANGE);
        g.drawString(String.valueOf(totalScore), xCenterShift - 20, 130);

        // Display Tank Images and Scores
        int baseY = 160; // Starting y position for the first tank
        int yIncrement = 45; // Space between each row

        for (int i = 0; i < 4; i++) {
            int yPosition = baseY + (i * yIncrement);

            // Draw each tank image centered
            g.drawImage(tankList.get(i), xCenterShift + 20, yPosition, this);

            // Draw arrow next to tank image
            g.drawImage(imageInstance.getArrow(), xCenterShift, yPosition + 8, this);

            // Display tank scores to the left, centered in column
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(tankScoreList[i]), xCenterShift - 150, yPosition + 20);
            g.drawString("PTS", xCenterShift - 90, yPosition + 20);

            // Display tank numbers to the right of images
            g.drawString(String.valueOf(tankNumList[i]), xCenterShift + 100, yPosition + 20);
        }

        // Draw underline and total score at the bottom
        int totalY = baseY + (4 * yIncrement) + 10;
        g.drawLine(xCenterShift - 80, totalY, xCenterShift + 70, totalY);
        g.drawString("TOTAL", xCenterShift - 150, totalY + 25);
        g.drawString(String.valueOf(totalTankNum), xCenterShift + 100, totalY + 25);
        g.setFont(font);
        g.setColor(Color.WHITE);
    }


    /**
     * Load the totalScore of the player from the CollisionUtility class.
     */
    public void loadScore() {
        for (int i = 0; i < 4; i++) {
            int[] enemyTankNum = CollisionUtility.getEnemyTankNum();
            tankNumList[i] = enemyTankNum[i];
        }
        for (int i = 0; i < 4; i++) {
            tankScoreList[i] = tankNumList[i] * 100 * (i + 1);
        }
        for (Integer score : tankScoreList) {
            totalScore += score;
        }
        for (Integer num : tankNumList) {
            totalTankNum += num;
        }
    }

    /**
     * Restart the game, load the menu and reset player's totalScore.
     */
    public void restart() {
        Board.restartGame();
        CollisionUtility.resetScore();
        loadMenu();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartButton) {
            restart();
        }
    }

    /**
     * Load the menu to the game panel if the player chooses to restart the
     * game.
     */
    private void loadMenu() {
        theView.getGamePanel().removeAll();
        Menu menu = new Menu(theView);
        menu.revalidate();
        menu.repaint();
        theView.getGamePanel().add(menu);
        menu.requestFocusInWindow();
        theView.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            loadMenu();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            loadMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            loadMenu();
        }
    }
}
