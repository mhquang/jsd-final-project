package src.screens;

import src.GameView;
import src.Map;
import src.utils.FontUtility;
import src.utils.SoundUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Setting extends JPanel implements ActionListener, KeyListener {
    private static Setting instance;
    private final GameView gameView;
    private static final FontUtility fontUtility = FontUtility.getInstance();
    private static final SoundUtility soundUtility = SoundUtility.getInstance();
    private final int y = 150;

    private Setting(GameView gameView) {
        this.gameView = gameView;
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setLayout(null);

        SwitchButton soundSwitchButton = getSwitchButton();
        this.add(soundSwitchButton);
    }

    public static Setting getInstance(GameView theView) {
        if (instance == null) {
            instance = new Setting(theView);
        }
        return instance;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Font titleFont = fontUtility.getPrstart().deriveFont(Font.BOLD, 28f);
        g.setFont(titleFont);
        g.setColor(Color.WHITE);

        String title = "SETTINGS";
        g.drawString(title, gameView.getWidth() / 2 - g.getFontMetrics().stringWidth(title) / 2, 50);

        Font instructionFont = fontUtility.getArialbd().deriveFont(26f);
        g.setFont(instructionFont);

        String sound = "Sound";
        String on = "ON";
        String off = "OFF";
        g.drawString(sound, gameView.getWidth() / 2 - g.getFontMetrics().stringWidth(sound) / 2, y);
        g.drawString(on, gameView.getWidth() / 2 + 70, y + 60);
        g.drawString(off, gameView.getWidth() / 2 - g.getFontMetrics().stringWidth(off) - 70, y + 60);

        Font backFont = fontUtility.getArialbd().deriveFont(Font.ITALIC, 15);
        g.setFont(backFont);
        g.setColor(Color.YELLOW);

        String backMsg = "PRESS ESC TO RETURN TO MENU";

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

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    private SwitchButton getSwitchButton() {
        SwitchButton soundSwitchButton = new SwitchButton();
        soundSwitchButton.setBounds(gameView.getWidth() / 2 - 50, y + 25, 100, 50);
        soundSwitchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                boolean soundOn = soundSwitchButton.isSelected();
                if (soundOn) {
                    soundUtility.unmute();
                } else {
                    soundUtility.mute();
                }
            }
        });
        return soundSwitchButton;
    }
}
