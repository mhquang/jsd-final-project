package src.utils;

import src.BlockType;
import src.Board;
import src.animation.*;
import src.environments.Base;
import src.environments.Block;
import src.tanks.NPCTank;
import src.tanks.PlayerTank;

import java.awt.*;
import java.util.ArrayList;

/**
 * Utility class for collision detection and handling
 *
 * @author Adrian Berg
 */
public class CollisionUtility {
    // Instance variable that indicated the x, y coordinates of the powerUp
    public static int powerUpX = 0;
    public static int powerUpY = 0;
    private static ArrayList<Block> blocks;
    private static ArrayList<Animation> explosions;
    // Instance variable that tracks the number of enemy tanks being destroyed
    private static int[] enemyTankNum = {0, 0, 0, 0};

    /**
     * Load blocks and explosion animation from the input array list
     *
     * @param inblocks    input blocks
     * @param inexplosion
     */
    public static void loadCollisionUtility(ArrayList<Block> inblocks,
                                            ArrayList<Animation> inexplosion) {
        blocks = inblocks;
        explosions = inexplosion;
    }

    /**
     * Reset the score of the game after game over.
     */
    public static void resetScore() {
        enemyTankNum = new int[]{0, 0, 0, 0};
    }

    /**
     * Helper method for collision between bullets and blocks
     *
     * @param bullet
     * @param block
     */
    public static void CollisionBulletsBlocksHelper(Bullet bullet, Block block) {
        BlockType type = BlockType.getTypeFromInt(block.getType());
        if (type.equals(BlockType.BRICK)) {
            block.lowerHealth();
            bullet.setVisible(false);
        }
        if (type.equals(BlockType.STEEL) && bullet.getUpgrade()) {
            block.lowerHealth();
            bullet.setVisible(false);
        }
        if (type.equals(BlockType.STEEL) && !bullet.getUpgrade()) {
            bullet.setVisible(false);
        }
        if (type.equals(BlockType.BASE) && block.health != 0) {
            bullet.setVisible(false);
            Base b = (Base) block;
            b.gameOver = true;
            explosions.add(new ExplodingTank(block.getX(), block.getY()));
            SoundUtility.explosion2();
            Board.setEndGame();
            SoundUtility.gameOver();

        }
        if (block.getHealth() == 0) {
            SoundUtility.explosion2();
            block.setVisible(false);
            explosions.add(new Explosion(block.getX(), block.getY()));

        }
        if (block.getHealth() == 0) {
            block.setVisible(false);

        }
    }

    /**
     * Check collision between tank and blocks
     *
     * @param r3 Rectangle
     * @return a boolean represents if there is a collision
     */
    public static boolean checkCollisionTankBlocks(Rectangle r3) {
        for (Block block : blocks) {
            Rectangle r2 = block.getBounds();
            BlockType type = BlockType.getTypeFromInt(block.getType());
            if (type.equals(BlockType.TREE)) {
            } else if (r3.intersects(r2)) {
                return true;
            }

        }
        return false;
    }

    /**
     * Check collision between bullets and blocks
     *
     * @param bullets array list for bullets
     * @param blocks  array list for blocks
     */
    public static void checkCollisionBulletsBlocks(ArrayList<Bullet> bullets,
                                                   ArrayList<Block> blocks) {

        for (Bullet b : bullets) {
            Rectangle r1 = b.getBounds();

            for (Block aBlock : blocks) {
                Rectangle r2 = aBlock.getBounds();

                if (r1.intersects(r2)) {
                    SoundUtility.BulletHitBrick();
                    CollisionBulletsBlocksHelper(b, aBlock);
                }
            }
        }
    }

    /**
     * Check collision between bullets and the player tank
     *
     * @param bullets    array list for bullets
     * @param playerTank
     */
    public static void checkCollisionBulletsTank(ArrayList<Bullet> bullets,
                                                 PlayerTank playerTank) {
        Rectangle r2 = playerTank.getBounds();
        for (Bullet b : bullets) {
            Rectangle r1 = b.getBounds();
            if (r1.intersects(r2) && b.isEnemy) {
                b.setVisible(false);
                if (!playerTank.isShield()) {
                    SoundUtility.explosion1();
                    explosions.add(new ExplodingTank(playerTank.getX(), playerTank.getY()));
                    playerTank.downHealth();
                    resetTankPosition(playerTank, 1);
                } else {
                    SoundUtility.BulletHitTank();
                }
            }
        }
    }

    /**
     * Check collision between bullets and enemy tanks
     *
     * @param bullets  array list for bullets
     * @param NPCTanks array list for Tank AIs
     */
    public static void checkCollisionBulletsTankAI(ArrayList<Bullet> bullets,
                                                   ArrayList<NPCTank> NPCTanks) {
        for (int x = 0; x < bullets.size(); x++) {
            Bullet b = bullets.get(x);
            Rectangle r1 = b.getBounds();

            for (int i = 0; i < NPCTanks.size(); i++) {
                NPCTank NPCTank = NPCTanks.get(i);
                Rectangle r2 = NPCTank.getBounds();

                if (r1.intersects(r2) && !b.isEnemy) {
                    NPCTank.decreaseHP();
                    b.setVisible(false);
                    SoundUtility.BulletHitTank();
                    if (NPCTank.getHealth() < 1) {
                        incrementNum(NPCTank);
                        if (NPCTank.hasPowerUp()) {
                            powerUpX = NPCTank.getX();
                            powerUpY = NPCTank.getY();
                        }
                        NPCTank.setVisible(false);
                        Board.decrementEnemies(1);
                        explosions.add(new ExplodingTank(NPCTank.getX(), NPCTank.getY()));
                        SoundUtility.explosion1();
                    }
                }
            }
        }
    }

    /**
     * Increment number of the tankAI being destroyed
     *
     * @param NPCTank a given tankAI
     */
    public static void incrementNum(NPCTank NPCTank) {
        String type = NPCTank.getType();
        switch (type) {
            case "basic":
                enemyTankNum[0] += 1;
                break;
            case "fast":
                enemyTankNum[1] += 1;
                break;
            case "power":
                enemyTankNum[2] += 1;
                break;
            case "armor":
                enemyTankNum[3] += 1;
                break;
            default:
                break;
        }
    }

    /**
     * Get the array that stores the number of each enemy tank being destroyed
     *
     * @return enemyTankNum the array that stores the number of each enemy tank
     * being destroyed
     */
    public static int[] getEnemyTankNum() {
        return enemyTankNum;
    }

    /**
     * Reset the position of the tank
     *
     * @param atank
     * @param type
     */
    public static void resetTankPosition(PlayerTank atank, int type) {
        atank.setX(atank.getInitialX());
        atank.setY(atank.getInitialY());
        atank.setShield(true);
        explosions.add(new TankShield(atank, 2));
        if (type == 1) {
            atank.setStarLevel(0);
        } else {
            atank.setShield(false);
        }
    }

    /**
     * Check collision between the player and enemy tanks
     *
     * @param NPCTanks array list for Tank AIs
     * @param atank    the player tank
     */
    public static void checkCollisionTankTankAI(ArrayList<NPCTank> NPCTanks,
                                                PlayerTank atank) {
        Rectangle r1 = atank.getBounds();
        for (int i = 0; i < NPCTanks.size(); i++) {
            NPCTank NPCTank = NPCTanks.get(i);
            Rectangle r2 = NPCTank.getBounds();
            if (r1.intersects(r2)) {
                if (!atank.isShield()) {
                    explosions.add(new ExplodingTank(atank.getX(), atank.getY()));
                    atank.downHealth();
                    resetTankPosition(atank, 1);
                } else {
                    incrementNum(NPCTank);
                    Board.decrementEnemies(1);
                    NPCTank.setVisible(false);
                    explosions.add(new ExplodingTank(atank.getX(), atank.getY()));
                }

            }
        }
    }

}
