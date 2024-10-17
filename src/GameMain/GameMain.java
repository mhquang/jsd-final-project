package src.GameMain;

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

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                GameView theView = new GameView();
                Menu menu = new Menu(theView);
                theView.getGamePanel().add(menu);
                theView.setVisible(true);
            }
        });
    }

}
