package src.screens;

import src.GameView;
import src.Map;
import src.utils.FontUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Help extends JPanel implements ActionListener, KeyListener {
    private static Help instance;
    private final GameView gameView;

    private static final FontUtility fontUtility = FontUtility.getInstance();

    private Help(GameView gameView) {
        this.gameView = gameView;
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setLayout(null);
    }

    public static Help getInstance(GameView theView) {
        if (instance == null) {
            instance = new Help(theView);
        }
        return instance;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Title font
        Font titleFont = fontUtility.getPrstart().deriveFont(Font.BOLD, 28f);
        g.setFont(titleFont);
        g.setColor(Color.WHITE);

        // Draw title
        String title = "HELP";
        g.drawString(title, gameView.getWidth() / 2 - g.getFontMetrics().stringWidth(title) / 2, 50);

        // Smaller font for instructions
        Font instructionFont = fontUtility.getArialbd(); // Smaller font size for instructions
        g.setFont(instructionFont);

        // Instructions
        String gameModes = "GAME MODES:";
        String mode1 = "1 Player - Control a tank to battle AI tanks.";
        String mode2 = "2 Players - Team up with a friend!";
        String controlsTitle = "CONTROLS:";
        String player1Controls = "Player 1 - Move: WASD | Shoot: G";
        String player2Controls = "Player 2 - Move: Arrow Keys | Shoot: L";
        String objectiveTitle = "OBJECTIVE:";
        String objective = "Destroy enemy tanks and protect your base.";
        String backMsg = "PRESS ESC TO RETURN TO MENU";

        // Vertical spacing and starting Y position
        int y = 100;
        int lineHeight = 30;

        // Draw each section with spacing
        g.drawString(gameModes, 50, y);
        g.drawString(mode1, 70, y + lineHeight);
        g.drawString(mode2, 70, y + 2 * lineHeight);

        g.drawString(controlsTitle, 50, y + 4 * lineHeight);
        g.drawString(player1Controls, 70, y + 5 * lineHeight);
        g.drawString(player2Controls, 70, y + 6 * lineHeight);

        g.drawString(objectiveTitle, 50, y + 8 * lineHeight);
        g.drawString(objective, 70, y + 9 * lineHeight);

        Font backFont = fontUtility.getArialbd().deriveFont(Font.ITALIC, 15); // Smaller font size for instructions
        g.setFont(backFont);
        g.setColor(Color.YELLOW);

        // Draw back message centered at the bottom
        g.drawString(backMsg, gameView.getWidth() / 2 - g.getFontMetrics().stringWidth(backMsg) / 2,
                Map.BOARD_HEIGHT * 4 / 5 + 90);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gameView.showMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
