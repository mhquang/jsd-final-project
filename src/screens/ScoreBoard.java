package src.screens;

import src.GameView;
import src.utils.CollisionUtility;
import src.utils.FontUtility;
import src.utils.ImageUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

public class ScoreBoard extends JPanel implements ActionListener, KeyListener {
    private GameView theView;
    private int stage, totalTankNum;
    private int totalScore = 0;
    private final ImageUtility imageInstance = ImageUtility.getInstance();
    private static final FontUtility fontUtility = FontUtility.getInstance();
    private int[] tankScoreList = {0, 0, 0, 0};
    private int[] tankNumList = {0, 0, 0, 0};
    private int selectedItem = 0;
    private final String[] menuItems = {"RESTART", "MAIN MENU", "EXIT"};
    private Board board;

    public ScoreBoard(GameView theView, Board board) {
        this.theView = theView;
        this.board = board;
        this.setFocusable(true);
        theView.setForeground(Color.BLACK);
        this.setLayout(null);
        this.addKeyListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        loadScore();

        Font font = fontUtility.getPrstart();
        g.setFont(font);
        g.setColor(Color.WHITE);

        stage = Board.getStage();
        ArrayList<Image> tankList = new ArrayList<>(Arrays.asList(
                imageInstance.getTankBasic(),
                imageInstance.getTankFast(),
                imageInstance.getTankPower(),
                imageInstance.getTankArmor()));

        int panelWidth = getWidth();
        int xCenterShift = panelWidth / 2;

        g.drawString("STAGE   " + stage, xCenterShift - 60, 60);

        int baseY = 95;
        int yIncrement = 45;

        for (int i = 0; i < 4; i++) {
            int yPosition = baseY + (i * yIncrement);
            g.setColor(Color.WHITE);

            g.drawImage(tankList.get(i), xCenterShift - 150, yPosition, this);
            g.drawString(String.valueOf(tankNumList[i]), xCenterShift - 80, yPosition + 20);
            g.drawImage(imageInstance.getArrow(), xCenterShift - 20, yPosition + 8, this);
            g.drawString(String.valueOf(tankScoreList[i]), xCenterShift + 20, yPosition + 20);
            g.drawString("PTS", xCenterShift + 120, yPosition + 20);
        }

        int totalY = baseY + (4 * yIncrement);
        g.drawLine(xCenterShift - 80, totalY, xCenterShift + 70, totalY);
        g.drawString("TOTAL", xCenterShift - 200, totalY + 30);
        g.drawString(String.valueOf(totalTankNum), xCenterShift - 80, totalY + 30);
        g.drawString("PTS", xCenterShift + 120, totalY + 30);
        g.setColor(Color.ORANGE);
        g.drawString(String.valueOf(totalScore), xCenterShift + 20, totalY + 30);


        int menuY = totalY + 150;
        int spacing = 50;

        int menuWidth = 0;
        for (String item : menuItems) {
            menuWidth += g.getFontMetrics().stringWidth(item) + spacing;
        }
        menuWidth -= spacing;

        int xPosition = xCenterShift - menuWidth / 2;
        for (int i = 0; i < menuItems.length; i++) {
            g.setColor(i == selectedItem ? Color.RED : Color.WHITE);
            g.drawString(menuItems[i], xPosition, menuY);
            xPosition += g.getFontMetrics().stringWidth(menuItems[i]) + spacing;
        }
    }

    public void loadScore() {
        totalScore = 0;
        totalTankNum = 0;

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

    public void restart() {
        board.restart();

        theView.getGamePanel().removeAll();

        CollisionUtility.resetScore();
        theView.getGamePanel().add(board);

        SwingUtilities.invokeLater(() -> {
            board.requestFocusInWindow();
            theView.revalidate();
            theView.repaint();
        });
    }

    private void loadMenu() {
        theView.getGamePanel().removeAll();
        Menu menu = Menu.getInstance(theView, 50);
        menu.setBackFromScoreBoard(true);
        theView.getGamePanel().add(menu);
        menu.requestFocusInWindow();
        theView.revalidate();
        theView.repaint();

        board.reset();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            selectedItem = (selectedItem + 1) % menuItems.length;
            repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            selectedItem = (selectedItem - 1 + menuItems.length) % menuItems.length;
            repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
            switch (selectedItem) {
                case 0 -> restart();
                case 1 -> loadMenu();
                case 2 -> System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
