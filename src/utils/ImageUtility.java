package src.utils;

import javax.swing.*;
import java.awt.*;

public class ImageUtility {
    // Instance variables for the images
    private final Image lives, flagIcon, enemyIcon;
    private final Image arrow, tankBasic, tankFast, tankPower, tankArmor;
    private final Image background, gameOver, tank, player1TankIcon, player2TankIcon;
    private static ImageUtility instance;

    public static ImageUtility getInstance() {
        if (instance == null) {
            return new ImageUtility();
        }
        return instance;
    }

    private ImageUtility() {
        lives = loadImage("assets/image/lives.png");
        flagIcon = loadImage("assets/image/flag.png");
        enemyIcon = loadImage("assets/image/enemy.png");
        arrow = loadImage("assets/image/arrow.png");
        tankBasic = loadImage("assets/image/npc_tanks/tank_basic_up.png");
        tankFast = loadImage("assets/image/npc_tanks/tank_fast_up.png");
        tankPower = loadImage("assets/image/npc_tanks/tank_power_up.png");
        tankArmor = loadImage("assets/image/npc_tanks/tank_armor_up.png");
        background = loadImage("assets/image/title.png");
        tank = loadImage("assets/image/player_tank/playerTank_right.png");
        player1TankIcon = loadImage("assets/image/player_tank/playerTank.png");
        player2TankIcon = loadImage("assets/image/player_tank/player2Tank.png");
        gameOver = loadImage("assets/image/game_over.png");
    }

    private Image loadImage(String imageAddress) {
        ImageIcon icon = new ImageIcon(imageAddress);
        return icon.getImage();
    }

    // Getter for different images
    public Image getLives() {
        return lives;
    }

    public Image getFlagIcon() {
        return flagIcon;
    }

    public Image getEnemyIcon() {
        return enemyIcon;
    }

    public Image getArrow() {
        return arrow;
    }

    public Image getTankBasic() {
        return tankBasic;
    }

    public Image getTankFast() {
        return tankFast;
    }

    public Image getTankPower() {
        return tankPower;
    }

    public Image getTankArmor() {
        return tankArmor;
    }

    public Image getBackground() {
        return background;
    }

    public Image getTank() {
        return tank;
    }

    public Image getPlayer1TankIcon() {
        return player1TankIcon;
    }

    public Image getPlayer2TankIcon() {
        return player2TankIcon;
    }

    public Image getGameOver() {
        return gameOver;
    }
}
