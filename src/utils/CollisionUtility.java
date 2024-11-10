package src.utils;

import src.BlockType;
import src.animation.*;
import src.environments.Base;
import src.environments.Block;
import src.screens.Board;
import src.tanks.NPCTank;
import src.tanks.PlayerTank;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollisionUtility {
    public static int powerUpX = 0;
    public static int powerUpY = 0;
    private static CopyOnWriteArrayList<Block> blocks;
    private static CopyOnWriteArrayList<Animation> explosions;
    private static int[] enemyTankNum = {0, 0, 0, 0};
    private static final SoundUtility soundUtility = SoundUtility.getInstance();

    public static void loadCollisionUtility(CopyOnWriteArrayList<Block> inblocks,
                                            CopyOnWriteArrayList<Animation> inexplosion) {
        blocks = inblocks;
        explosions = inexplosion;
    }

    public static void resetScore() {
        enemyTankNum = new int[]{0, 0, 0, 0};
    }

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
            soundUtility.explosion2();
            Board.setEndGame();
            soundUtility.gameOver();

        }
        if (block.getHealth() == 0) {
            soundUtility.explosion2();
            block.setVisible(false);
            explosions.add(new Explosion(block.getX(), block.getY()));

        }
        if (block.getHealth() == 0) {
            block.setVisible(false);

        }
    }

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

    public static void checkCollisionBulletsBlocks(CopyOnWriteArrayList<Bullet> bullets,
                                                   CopyOnWriteArrayList<Block> blocks) {

        for (Bullet b : bullets) {
            Rectangle r1 = b.getBounds();

            for (Block aBlock : blocks) {
                Rectangle r2 = aBlock.getBounds();

                if (r1.intersects(r2)) {
                    soundUtility.bulletHitBrick();
                    CollisionBulletsBlocksHelper(b, aBlock);
                }
            }
        }
    }

    public static void checkCollisionBulletsTank(CopyOnWriteArrayList<Bullet> bullets,
                                                 PlayerTank playerTank) {
        Rectangle r2 = playerTank.getBounds();
        for (Bullet b : bullets) {
            Rectangle r1 = b.getBounds();
            if (r1.intersects(r2) && b.isEnemy) {
                b.setVisible(false);
                if (!playerTank.isShield()) {
                    soundUtility.explosion1();
                    explosions.add(new ExplodingTank(playerTank.getX(), playerTank.getY()));
                    playerTank.downHealth();
                    resetTankPosition(playerTank, 1);
                } else {
                    soundUtility.bulletHitTank();
                }
            }
        }
    }

    public static void checkCollisionBulletsTankAI(CopyOnWriteArrayList<Bullet> bullets,
                                                   CopyOnWriteArrayList<NPCTank> NPCTanks) {
        for (Bullet b : bullets) {
            Rectangle r1 = b.getBounds();

            for (src.tanks.NPCTank NPCTank : NPCTanks) {
                Rectangle r2 = NPCTank.getBounds();

                if (r1.intersects(r2) && !b.isEnemy) {
                    NPCTank.decreaseHP();
                    b.setVisible(false);
                    soundUtility.bulletHitTank();
                    if (NPCTank.getHealth() < 1) {
                        incrementNum(NPCTank);
                        if (NPCTank.hasPowerUp()) {
                            powerUpX = NPCTank.getX();
                            powerUpY = NPCTank.getY();
                        }
                        NPCTank.setVisible(false);
                        Board.decrementEnemies(1);
                        explosions.add(new ExplodingTank(NPCTank.getX(), NPCTank.getY()));
                        soundUtility.explosion1();
                    }
                }
            }
        }
    }

    public static int[] getEnemyTankNum() {
        return enemyTankNum;
    }

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

    public static void checkCollisionTankTankAI(CopyOnWriteArrayList<NPCTank> NPCTanks,
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
