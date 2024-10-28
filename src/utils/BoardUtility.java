package src.utils;

import src.BlockType;
import src.Board;
import src.animation.*;
import src.environments.Block;
import src.powerups.*;
import src.tanks.NPCTank;
import src.tanks.PlayerTank;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class BoardUtility {

    private static ArrayList<NPCTank> enemy = new ArrayList<>();
    private static ArrayList<Block> blocks = new ArrayList<>();
    private static ArrayList<Animation> animations = new ArrayList<>();
    private static ArrayList<PowerUp> powerUps = new ArrayList<>();
    private static ArrayList<PlayerTank> playerTanks = new ArrayList<>();

    /**
     * Constructor for the BoardUtility class
     *
     * @param enemy1      an array list that stores enemy tanks
     * @param blocks1     an array list that stores blocks on the board
     * @param animations1 an array list that stores different animations
     * @param powerUps1   an array list that stores different power-ups
     * @param playerTank1 the Tank class that represents the player
     */
    public static void loadBoardUtility(ArrayList<NPCTank> enemy1,
                                        ArrayList<Block> blocks1,
                                        ArrayList<Animation> animations1,
                                        ArrayList<PowerUp> powerUps1, ArrayList<PlayerTank> playerTank1) {
        enemy = enemy1;
        blocks = blocks1;
        animations = animations1;
        powerUps = powerUps1;
        playerTanks = playerTank1;
    }

    /**
     * Update power ups on the board.
     */
    public static void updatePowerUps() {
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);
            p.updateAnimation();
            BlockType type = BlockType.getTypeFromInt(p.getType());

            if (System.currentTimeMillis() - p.getLoadTime() > 10000) {
                powerUps.remove(i);
            }

            for (PlayerTank playerTank : playerTanks) {
                Rectangle r1 = playerTank.getBounds();
                Rectangle r2 = p.getBounds();

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
                        for (NPCTank npcTank : enemy) {
                            npcTank.frozen = true;
                            npcTank.frozenStartTime = System.currentTimeMillis();
                        }
                    } else if (type.equals(BlockType.BOMB)) {
                        for (int x = 0; x < enemy.size(); x++) {
                            enemy.get(x).setVisible(false);
                            for (NPCTank ai : enemy) {
                                CollisionUtility.incrementNum(ai);
                            }
                            Board.decrementEnemies(enemy.size());
                            animations.add(new ExplodingTank(enemy.get(x).getX(),
                                    enemy.get(x).getY()));
                        }
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
     * @param powerUp    a boolean that represents if the tank AI carries powerUp
     */
    public static void spawnTankAI(String difficulty, boolean powerUp) {
        Random random = new Random();
        int randomPos = random.nextInt(3);
        int randomType = random.nextInt(20);
        String type;
        if (randomType < 2) {
            type = "armor";
        } else if (randomType < 7) {
            type = "power";
        } else if (randomType >= 8 && randomType < 13) {
            type = "fast";
        } else {
            type = "basic";
        }
        if (randomPos == 0) {
            animations.add(new TankSpawn(5 * 16, 2 * 16));
            NPCTank NPC = new NPCTank(5 * 16, 2 * 16, difficulty, type, powerUp);
            enemy.add(NPC);
        } else if (randomPos == 1) {
            animations.add(new TankSpawn(16 * 16, 2 * 16));
            NPCTank NPC = new NPCTank(16 * 16, 2 * 16, difficulty, type, powerUp);
            enemy.add(NPC);
        } else {
            animations.add(new TankSpawn(28 * 16, 2 * 16));
            NPCTank NPC = new NPCTank(28 * 16, 2 * 16, difficulty, type, powerUp);
            enemy.add(NPC);
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
                } else {
                    bullets.remove(i);
                }
            }
        }
    }

    /**
     * Update bullets and tank on the Board.
     */
    public static void updateBulletsTank() {
        for (PlayerTank playerTank : playerTanks) {
            ArrayList<Bullet> bullets = playerTank.getBullets();

            for (int i = 0; i < bullets.size(); i++) {
                Bullet b = bullets.get(i);
                if (b.isVisible()) {
                    b.move();
                } else {
                    bullets.remove(i);
                }
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
            if (!b.isVisible()) {
                blocks.remove(i);
            }
        }
    }

    /**
     * Update animations on the Board.
     */
    public static void updateAnimations() {
        for (int i = 0; i < animations.size(); i++) {
            if (!animations.get(i).isVisible()) {
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
        for (PlayerTank playerTank : playerTanks) {
            if (playerTank.isVisible()) {
                playerTank.move();
            }
        }

    }

    /**
     * Check for collisions on the board.
     */
    public static void checkCollisions() {
        for (PlayerTank playerTank : playerTanks) {
            ArrayList<Bullet> bullets = new ArrayList<>(playerTank.getBullets());
            for (NPCTank NPCTank : enemy) {
                bullets.addAll(NPCTank.getBullets());
            }
            CollisionUtility.checkCollisionBulletsBlocks(bullets, blocks);
            CollisionUtility.checkCollisionBulletsTank(bullets, playerTank);
            CollisionUtility.checkCollisionBulletsTankAI(bullets, enemy);
            CollisionUtility.checkCollisionTankTankAI(enemy, playerTank);
        }
    }
}
