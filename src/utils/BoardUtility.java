package src.utils;

import src.GameMain.BlockType;
import src.GameMain.Board;
import src.SpriteClasses.Animation;
import src.environments.Block;
import src.SpriteClasses.Bullet;
import src.SpriteClasses.ExplodingTank;
import src.powerups.BombPowerUp;
import src.powerups.ClockPowerUp;
import src.powerups.PowerUp;
import src.powerups.ShieldPowerUp;
import src.powerups.StarPowerUp;
import src.powerups.TankPowerUp;
import src.tanks.PlayerTank;
import src.tanks.NPCTank;
import src.SpriteClasses.TankShield;
import src.SpriteClasses.TankSpawn;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class BoardUtility {

    private static ArrayList<NPCTank> enemy = new ArrayList<>();
    private static ArrayList<Block> blocks = new ArrayList<>();
    private static ArrayList<Animation> animations = new ArrayList<>();
    private static ArrayList<PowerUp> powerUps = new ArrayList<>();
    private static PlayerTank playerTank;

    /**
     * Constructor for the BoardUtility class
     *
     * @param enemy1 an array list that stores enemy tanks
     * @param blocks1 an array list that stores blocks on the board
     * @param animations1 an array list that stores different animations
     * @param powerUps1 an array list that stores different power-ups
     * @param playerTank1 the Tank class that represents the player
     */
    public static void loadBoardUtility(ArrayList<NPCTank> enemy1,
                                        ArrayList<Block> blocks1,
                                        ArrayList<Animation> animations1,
                                        ArrayList<PowerUp> powerUps1, PlayerTank playerTank1) {
        enemy = enemy1;
        blocks = blocks1;
        animations = animations1;
        powerUps = powerUps1;
        playerTank = playerTank1;
    }

    /**
     * Update power ups on the board.
     */
    public static void updatePowerUps() {
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);
            p.updateAnimation();
            BlockType type = BlockType.getTypeFromInt(p.getType());
            Rectangle r1 = playerTank.getBounds();
            Rectangle r2 = p.getBounds();

            if (System.currentTimeMillis() - p.getLoadTime() > 10000) {
                powerUps.remove(i);
            }

            if (r1.intersects(r2)) {
                powerUps.remove(i);
                SoundUtility.powerupPick();
                if (type.equals(BlockType.TANK)) {
                    playerTank.upHealth();
                } else if (type.equals(BlockType.SHIELD)) {
                    playerTank.setShield(true);
                    animations.add(new TankShield(playerTank, 1));
                } else if (type.equals(BlockType.SHOVEL)) {

                } else if (type.equals(BlockType.STAR)) {
                    playerTank.upStarLevel();
                } else if (type.equals(BlockType.CLOCK)) {
                    for (int x = 0; x < enemy.size(); x++) {
                        enemy.get(x).frozen = true;
                        enemy.get(x).frozenStartTime = System.currentTimeMillis();
                    }
                } else if (type.equals(BlockType.BOMB)) {
                    for (int x = 0; x < enemy.size(); x++) {
                        enemy.get(x).visible = false;
                        for (NPCTank ai : enemy) {
                            CollisionUtility.incrementNum(ai);
                        }
                        Board.decrementEnemies(enemy.size());
                        animations.add(new ExplodingTank(enemy.get(x).x,
                                                         enemy.get(x).y));
                    }
                }
            }
        }

    }

    /**
     * Spawn random PowerUp when the given tank AI carries powerUp and is
     * destroyed.
     */
    public static void spawnPowerUp() {
        Random random = new Random();
        int randomPow = random.nextInt(5);
        if (CollisionUtility.powerUpX != 0 || CollisionUtility.powerUpY != 0) {
            switch (randomPow) {
                case 0:
                    powerUps.add(new BombPowerUp(CollisionUtility.powerUpX,
                                                 CollisionUtility.powerUpY));
                    break;
                case 1:
                    powerUps.add(new ClockPowerUp(CollisionUtility.powerUpX,
                                                  CollisionUtility.powerUpY));
                    break;
                case 2:
                    powerUps.add(new ShieldPowerUp(CollisionUtility.powerUpX,
                                                   CollisionUtility.powerUpY));
                    break;
                case 3:
                    powerUps.add(new StarPowerUp(CollisionUtility.powerUpX,
                                                 CollisionUtility.powerUpY));
                    break;
                case 4:
                    powerUps.add(new TankPowerUp(CollisionUtility.powerUpX,
                                                 CollisionUtility.powerUpY));
                    break;
                default:
                    break;
            }
            CollisionUtility.powerUpX = 0;
            CollisionUtility.powerUpY = 0;
        }
    }

    /**
     * Spawn Tank AI on the board.
     *
     * @param difficulty a string that represents the difficulty of the tank AI
     * @param powerUp a boolean that represents if the tank AI carries powerUp
     */
    public static void spawnTankAI(String difficulty, boolean powerUp) {
        Random random = new Random();
        int randomPos = random.nextInt(3);
        int randomType = random.nextInt(20);
        String type;
        if (randomType < 2) {
            type = "armor";
        } else if (randomType >= 2 && randomType < 7) {
            type = "power";
        } else if (randomType >= 8 && randomType < 13) {
            type = "fast";
        } else {
            type = "basic";
        }
        if (randomPos == 0) {
            animations.add(new TankSpawn(2 * 16, 1 * 16));
            NPCTank AI = new NPCTank(2 * 16, 1 * 16, difficulty, type, powerUp);
            enemy.add(AI);
        } else if (randomPos == 1) {
            animations.add(new TankSpawn(14 * 16, 1 * 16));
            NPCTank AI = new NPCTank(14 * 16, 1 * 16, difficulty, type, powerUp);
            enemy.add(AI);
        } else {
            animations.add(new TankSpawn(26 * 16, 1 * 16));
            NPCTank AI = new NPCTank(26 * 16, 1 * 16, difficulty, type, powerUp);
            enemy.add(AI);
        }
    }

    /**
     * Update bullets and tank AI on the board.
     */
    public static void updateBulletsTankAI() {
        for (NPCTank NPCTank : enemy) {
            ArrayList<Bullet> bullets = NPCTank.getBullets();
            for (int i = 0; i < bullets.size(); i++) {
                Bullet b = bullets.get(i);
                if (b.isVisible()) {
                    b.move();
                } else if (b.isVisible() == false) {
                    bullets.remove(i);
                }
            }
        }
    }

    /**
     * Update bullets and tank on the Board.
     */
    public static void updateBulletsTank() {
        ArrayList<Bullet> bullets = playerTank.getBullets();

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            if (b.isVisible()) {
                b.move();
            } else if (b.isVisible() == false) {
                bullets.remove(i);
            }
        }
    }

    /**
     * Update blocks on the Board.
     */
    public static void updateBlocks() {
        for (int i = 0; i < blocks.size(); i++) {
            Block b = blocks.get(i);
            BlockType type = BlockType.getTypeFromInt(b.getType());
            if (type.equals(BlockType.RIVER)) {
                b.updateAnimation();
            } else if (type.equals(BlockType.BASE)) {
                b.updateAnimation();
            }
            if (b.isVisible() == false) {
                blocks.remove(i);
            }
        }
    }

    /**
     * Update animations on the Board.
     */
    public static void updateAnimations() {
        for (int i = 0; i < animations.size(); i++) {
            if (animations.get(i).visible == false) {
                animations.remove(i);
            } else {
                animations.get(i).updateAnimation();
            }
        }
    }

    /**
     * Update tank on the Board.
     */
    public static void updateTank() {
        if (playerTank.isVisible()) {
            playerTank.move();
        }
    }

    /**
     * Check for collisions on the board.
     */
    public static void checkCollisions() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.addAll(playerTank.getBullets());
        for (NPCTank NPCTank : enemy) {
            bullets.addAll(NPCTank.getBullets());
        }
        CollisionUtility.checkCollisionBulletsBlocks(bullets, blocks);
        CollisionUtility.checkCollisionBulletsTank(bullets, playerTank);
        CollisionUtility.checkCollisionBulletsTankAI(bullets, enemy);
        CollisionUtility.checkCollisionTankTankAI(enemy, playerTank);

    }

}
