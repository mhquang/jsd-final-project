package src;

import javax.swing.*;
import java.awt.*;


public class GameMain {

    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameView.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> {
            GameView theView = new GameView();
            theView.setVisible(true);
        });
    }

}
