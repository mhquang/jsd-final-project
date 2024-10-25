package src.utils;

import java.awt.Image;
import javax.swing.ImageIcon;

public class ImageUtility {
    // Instance variables for the images
    private final Image lives, flagIcon, enemyIcon;
    private final Image arrow, tankBasic, tankFast, tankPower, tankArmor;
    private final Image background, tank;
    private static ImageUtility instance;

    public static ImageUtility getInstance() {
        if (instance == null) {
            return new ImageUtility();
        }
        return instance;
    }

    private ImageUtility() {
        lives = loadImage("image/lives.png");
        flagIcon = loadImage("image/flag.png");
        enemyIcon = loadImage("image/enemy.png");
        arrow = loadImage("image/arrow.png");
        tankBasic = loadImage("image/npc_tanks/tank_basic_up.png");
        tankFast = loadImage("image/npc_tanks/tank_fast_up.png");
        tankPower = loadImage("image/npc_tanks/tank_power_up.png");
        tankArmor = loadImage("image/npc_tanks/tank_armor_up.png");
        background = loadImage("image/title.png");
        tank = loadImage("image/player_tank/playerTank_right.png");
    }

    public Image loadImage(String imageAddress) {
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

}
