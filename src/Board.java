package src;

import src.animation.Animation;
import src.animation.Bullet;
import src.environments.*;
import src.powerups.PowerUp;
import src.tanks.NPCTank;
import src.tanks.PlayerTank;
import src.utils.BoardUtility;
import src.utils.CollisionUtility;
import src.utils.ImageUtility;
import src.utils.SoundUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static src.utils.CollisionUtility.loadCollisionUtility;
import static src.utils.CollisionUtility.resetTankPosition;

public class Board extends JPanel implements ActionListener {
    // Instance variable for the timer of the tank
    private Timer timer;
    private PlayerTank player1Tank, player2Tank;
    private ArrayList<PlayerTank> playerTanks = new ArrayList<>();
    private ArrayList<NPCTank> enemy = new ArrayList<>();
    private ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<Animation> animations = new ArrayList<>();
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private static final ImageUtility imageInstance = ImageUtility.getInstance();
    private static final SoundUtility soundUtility = SoundUtility.getInstance();
    private final int INIT_PLAYER_X = 13 * 16;
    private final int INIT_PLAYER_Y = Map.level0.length * 16;
    private final int B_WIDTH = Map.BOARD_WIDTH;
    private final int B_HEIGHT = Map.BOARD_HEIGHT;
    private final int DELAY = 15;
    private final int initX = 31;
    private boolean pause = false;
    private static boolean gameOver = false;
    private int yPos = Map.BOARD_HEIGHT;
    private int direction = -1;
    private final int stopYPos = Map.BOARD_HEIGHT / 2 - 75;
    private GameView theView;
    private static int stage = 1;
    private int numAI;
    private static final int goal = 10;
    public static int numEnemies = goal;
    private boolean isTwoPlayerMode;

    /**
     * Constructor for the Board class
     *
     * @param theView GameView that represents the frame of the game
     */
    public Board(GameView theView, boolean isTwoPlayerMode) {
        this.theView = theView;
        this.isTwoPlayerMode = isTwoPlayerMode;
        timer = new Timer(DELAY, this);
        timer.start();
        initBoard();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawObjects(g);
        drawEdge(g);
        endGame(g);
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!Menu.getMenuStatus() && pause) {
            return;
        }
        if (gameOver) {
            timer.stop();
            return;
        }
        updateSprites();
        checkCollisions();
        checkGameOver();

        nextLevel();
        repaint();
    }

    /**
     * Decrease the number of enemies shown on the sidebar of the board
     *
     * @param num the number of enemies that needs to be decreased
     */
    public static void decrementEnemies(int num) {
        numEnemies -= num;
    }

    /**
     * Get the number of current stage
     *
     * @return stage an integer that represents the number of current stage
     */
    public static int getStage() {
        return stage;
    }

    /**
     * Set the gameOver variable to true.
     */
    public static void setEndGame() {
        System.out.println("Game Over Played");
        soundUtility.gameOver();
        gameOver = true;
    }

    /**
     * Restart the game and set gameOver to be false.
     */
    public static void restartGame() {
        gameOver = false;
    }

    /**
     * Initialize the board.
     */
    private void initBoard() {
        stage = 1;
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        numAI = 0;
        player1Tank = new PlayerTank(INIT_PLAYER_X, INIT_PLAYER_Y, 5, true);
        playerTanks.add(player1Tank);

        if (isTwoPlayerMode) {
            player2Tank = new PlayerTank(INIT_PLAYER_X + 7 * 16, INIT_PLAYER_Y, 5, false);
            playerTanks.add(player2Tank);
        }

        initBlocks();
        CollisionUtility.loadCollisionUtility(blocks, animations);
        BoardUtility.loadBoardUtility(enemy, blocks, animations, powerUps, playerTanks);
    }

    /**
     * Initialize blocks according to the map.
     */
    private void initBlocks() {
        int[][] map = Map.getMap(stage);
        soundUtility.startStage();
        int type;
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                type = map[x][y];
                BlockType bType = BlockType.getTypeFromInt(type);
                switch (bType) {
                    case BRICK:
                        blocks.add(new Brick(y * 16, x * 16));
                        break;
                    case STEEL:
                        blocks.add(new Steel(y * 16, x * 16));
                        break;
                    case BASE:
                        blocks.add(new Base(y * 16, x * 16));
                        break;
                    case RIVER:
                        blocks.add(new River(y * 16, x * 16));
                        break;
                    case TREE:
                        blocks.add(new Tree(y * 16, x * 16));
                        break;
                    case EDGE:
                        blocks.add(new Edge(y * 16, x * 16));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Check if the player's health is lower than 0. If lower than 0, then end
     * the game
     */
    private void checkGameOver() {
        if (player1Tank.getHealth() == 0) {
            setEndGame();
        }
        if (isTwoPlayerMode) {
            if (player1Tank.getHealth() == 0 && player2Tank.getHealth() == 0) {
                setEndGame();
            }
        }
    }

    /**
     * Draw objects on the board.
     */
    private void drawObjects(Graphics g) {
        for (NPCTank NPCTank : enemy) {
            if (NPCTank.isVisible()) {
                g.drawImage(NPCTank.getImage(), NPCTank.getX(), NPCTank.getY(),
                        this);
            }
        }
        if (player1Tank.isVisible()) {
            g.drawImage(player1Tank.getImage(), player1Tank.getX(), player1Tank.getY(), this);
        }

        if (isTwoPlayerMode) {
            if (player2Tank.isVisible()) {
                g.drawImage(player2Tank.getImage(), player2Tank.getX(), player2Tank.getY(), this);
            }
            ArrayList<Bullet> bulletsFromPlayer2 = new ArrayList<>(player2Tank.getBullets());

            for (Bullet b : bulletsFromPlayer2) {
                if (b.isVisible()) {
                    g.drawImage(b.getImage(), b.getX(), b.getY(), this);
                }
            }

        }

        ArrayList<Bullet> bullets = new ArrayList<>(player1Tank.getBullets());

        for (NPCTank NPCTank : enemy) {
            bullets.addAll(NPCTank.getBullets());
        }

        for (Bullet b : bullets) {
            if (b.isVisible()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
        for (Block a : blocks) {
            if (a.isVisible()) {
                g.drawImage(a.getImage(), a.getX(), a.getY(), this);
            }
        }
        for (Animation e : animations) {
            if (e.isVisible()) {
                g.drawImage(e.getImage(), e.getX(), e.getY(), this);
            }
        }
        for (PowerUp p : powerUps) {
            if (p.isVisible()) {
                g.drawImage(p.getImage(), p.getX(), p.getY(), this);
            }
        }
    }

    /**
     * Draw the edge of the game
     *
     * @param g Graphics
     */
    private void drawEdge(Graphics g) {
        Font font = BoardUtility.loadFont();
        g.setFont(font);

        // Draw enemies
        drawEnemies(g, numEnemies);

        // Draw lives
        int player1Health = player1Tank.getHealth();

        Image player1TankImage = imageInstance.getPlayer1TankIcon();
        g.drawImage(player1TankImage, initX * 16 + 1, 13 * 16, this);

        g.drawString(String.valueOf(Math.max(player1Health, 0)), (initX + 1) * 16 + 3, 14 * 16);

        if (isTwoPlayerMode) {
            int player2Health = player2Tank.getHealth();

            Image player2TankImage = imageInstance.getPlayer2TankIcon();
            g.drawImage(player2TankImage, initX * 16 + 1, 15 * 16, this);

            g.drawString(String.valueOf(Math.max(player2Health, 0)), (initX + 1) * 16 + 3, 16 * 16);
        }

        // Draw stages
        Image flagIcon = imageInstance.getFlagIcon();
        g.drawImage(flagIcon, initX * 16, 22 * 16, this);

        g.drawString(String.valueOf(stage), (initX + 1) * 16, 25 * 16);
    }

    /**
     * Draw the part that shows how many enemies left in the game on the edge of
     * the game board
     *
     * @param g          Graphics
     * @param numEnemies number of enemies left in the game
     */
    private void drawEnemies(Graphics g, int numEnemies) {
        Image enemyIcon = imageInstance.getEnemyIcon();
        int count = 1;
        int initY = 4;
        for (int i = 0; i < numEnemies; i++) {
            switch (count) {
                case 0:
                    count = 1;
                    g.drawImage(enemyIcon, (initX + 1) * 16, initY * 16, this);
                    initY++;
                    break;
                case 1:
                    count--;
                    g.drawImage(enemyIcon, initX * 16, initY * 16, this);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Call initBoard to enter next Level when no enemy in the list.
     */
    private void nextLevel() {
        if (enemy.isEmpty()) {
            if (stage == 35) {
                System.out.println("You win!");
                loadScoreBoard(theView);
            } else {
                stage += 1;
                numAI = 0;
                numEnemies = goal;
                clearBoard();

                initBlocks();
                CollisionUtility.loadCollisionUtility(blocks, animations);
                BoardUtility.loadBoardUtility(enemy, blocks, animations, powerUps, playerTanks);
            }
        }
    }

    /**
     * UpdatesSprites is used to call the various update calls.
     */
    private void updateSprites() {
        spawnTankAI();
        spawnPowerUp();
        updateTank();
        updateTankAI();
        updateBullets();
        updateBlocks();
        updateAnimations();
        updateBlocks();
        updatePowerUps();
    }

    /**
     * Update animations on the board this includes
     * TankShield/Explosion/ExplodingTank/TankSpawn
     * <p>
     * Animations are removed if vis is false. Otherwise, animations are updated
     * via the updateAnimation method
     */
    private void updateAnimations() {
        BoardUtility.updateAnimations();
    }

    /**
     * Update blocks on the board this includes Base/Brick/Edge/River/Steel/Tree
     * <p>
     * Blocks are removed if vis is false. Blocks that are types RIVER and BASE
     * they will be updated via the updateAnimation method
     */
    private void updateBlocks() {
        BoardUtility.updateBlocks();
    }

    /**
     * Updates the player tank.
     * If the tank is visible it is moved
     */
    private void updateTank() {
        BoardUtility.updateTank();
    }

    /**
     * Updates the tank AI.
     */
    private void updateTankAI() {
        for (NPCTank NPCTank : enemy) {
            if (NPCTank.isVisible()) {
                if (System.currentTimeMillis() - NPCTank.frozenStartTime > 5000 && NPCTank.frozen) {
                    NPCTank.frozen = false;
                }
                if ("easy".equals(NPCTank.getDifficulty())) {
                    NPCTank.actionEasy();
                } else if ("normal".equals(NPCTank.getDifficulty())) {
                    NPCTank.actionNormal(this.player1Tank);
                } else if ("hard".equals(NPCTank.getDifficulty())) {
                    NPCTank.actionHard(this.player1Tank);
                }

                if (isTwoPlayerMode) {
                    if ("normal".equals(NPCTank.getDifficulty())) {
                        NPCTank.actionNormal(this.player2Tank);
                    } else if ("hard".equals(NPCTank.getDifficulty())) {
                        NPCTank.actionHard(this.player2Tank);
                    }
                }
            }
        }
        for (int i = 0; i < enemy.size(); i++) {
            if (!enemy.get(i).isVisible()) {
                enemy.remove(i);
            }
        }
    }

    /**
     * Spawn tank AI to reach the goal.
     */
    private void spawnTankAI() {
        while (numAI < goal) {
            if (enemy.size() < 4) {
                boolean powerUp;
                powerUp = (numAI % 4 == 1);
                if (numAI < 2) {
                    BoardUtility.spawnTankAI("easy", powerUp);
                } else if (numAI < 6) {
                    BoardUtility.spawnTankAI("normal", powerUp);
                } else {
                    BoardUtility.spawnTankAI("hard", powerUp);
                }
                numAI++;
            } else {
                return;
            }
        }
    }

    /**
     * Updates the powerUps on the board
     * <p>
     * Unlike the other updateMethods, update for powerUps handles the collision
     * of a player tank and a powerUp.
     * PowerUps are removed if vis = false otherwise they are updated via updateAnimations.
     */
    private void updatePowerUps() {
        BoardUtility.updatePowerUps();
    }

    private void spawnPowerUp() {
        BoardUtility.spawnPowerUp();
    }

    /**
     * updates the bullets for both the player tank and enemyIcon Tanks
     */
    private void updateBullets() {
        BoardUtility.updateBulletsTank();
        BoardUtility.updateBulletsTankAI();
    }

    /**
     * Check collisions between different sprite classes
     */
    private void checkCollisions() {
        BoardUtility.checkCollisions();
    }

    /**
     * Create end game information on the screen. After the "GAME OVER" image
     * shows on the screen, a score board of the entire game will be displayed
     *
     * @param g Graphics
     */
    private void endGame(Graphics g) {
        if (gameOver) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            Timer gameOverTimer = getGameOverTimer();

            Image gameOver = imageInstance.getGameOver();
            g.drawImage(gameOver, Map.BOARD_WIDTH / 2 - gameOver.getWidth(null) / 2 + 10,  // Center horizontally
                    yPos, this);


            if (yPos == stopYPos) {
                gameOverTimer.stop();
                Timer sorceBoardTimer = new Timer(3000, e -> loadScoreBoard(theView));
                sorceBoardTimer.setRepeats(false);
                sorceBoardTimer.start();
            }
        }
    }

    private Timer getGameOverTimer() {
        Timer gameOverTimer = new Timer(80, e -> {
            yPos += direction;
            if (yPos == stopYPos) {
                direction = 0;
            } else if (yPos > getHeight()) {
                yPos = getHeight();
            } else if (yPos < 0) {
                yPos = 0;
                direction *= -1;
            }
            repaint();
        });
        gameOverTimer.setRepeats(true);
        gameOverTimer.setCoalesce(true);
        gameOverTimer.start();
        return gameOverTimer;
    }

    /**
     * Load the score board to the game board
     *
     * @param theView GameView that represents the frame of the game
     */
    private void loadScoreBoard(GameView theView) {
        theView.getGamePanel().removeAll();
        ScoreBoard scoreBoard = new ScoreBoard(theView);
        scoreBoard.setBackground(Color.BLACK);
        theView.getGamePanel().add(scoreBoard);
        scoreBoard.requestFocusInWindow();
        soundUtility.statistics();
        theView.setVisible(true);
    }

    /**
     * Clear the initialized variables on the board.
     */
    private void clearBoard() {
        animations = new ArrayList<>();
        blocks = new ArrayList<>();
        powerUps = new ArrayList<>();

        updateSprites();
        resetTankPosition(player1Tank, 2);
        if (isTwoPlayerMode) {
            resetTankPosition(player2Tank, 2);
        }
        loadCollisionUtility(blocks, animations);
    }

    /**
     * Tank key adapter. Override the methods in KeyAdapter to add events
     * handlers for the tanks
     */
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            player1Tank.keyReleased(e);
            if (isTwoPlayerMode) {
                player2Tank.keyReleased(e);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player1Tank.keyPressed(e);
            if (isTwoPlayerMode) {
                player2Tank.keyPressed(e);
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (!pause) {
                    soundUtility.pause();
                }
                pause = !pause;
            }
        }
    }
}
