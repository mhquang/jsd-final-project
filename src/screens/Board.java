package src.screens;

import src.BlockType;
import src.GameView;
import src.Map;
import src.animation.Animation;
import src.animation.Bullet;
import src.environments.*;
import src.powerups.PowerUp;
import src.tanks.NPCTank;
import src.tanks.PlayerTank;
import src.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static src.utils.CollisionUtility.loadCollisionUtility;
import static src.utils.CollisionUtility.resetTankPosition;

public class Board extends JPanel implements ActionListener, Runnable {
    private Thread gameThread;
    private boolean running = true;
    private boolean pause = false;
    private static boolean gameOver = false;
    private PlayerTank player1Tank, player2Tank;
    private CopyOnWriteArrayList<PlayerTank> playerTanks = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<NPCTank> enemy = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Block> blocks = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Animation> animations = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<PowerUp> powerUps = new CopyOnWriteArrayList<>();
    private static final ImageUtility imageInstance = ImageUtility.getInstance();
    private static final SoundUtility soundUtility = SoundUtility.getInstance();
    private static final FontUtility fontUtility = FontUtility.getInstance();
    private final int INIT_PLAYER_X = 13 * 16;
    private final int INIT_PLAYER_Y = Map.level0.length * 16;
    private final int initX = 31;
    private GameView theView;
    private static int stage = 1;
    private int numAI;
    private static final int goal = 10;
    public static int numEnemies = goal;
    private boolean isTwoPlayerMode;

    public Board(GameView theView, boolean isTwoPlayerMode) {
        this.theView = theView;
        this.isTwoPlayerMode = isTwoPlayerMode;
        initBoard();
        startGame();
    }

    private void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (running) {
            if (!pause) {
                if (gameOver) {
                    stopGame();
                    return;
                }
                updateSprites();
                checkCollisions();
                checkGameOver();

                nextLevel();
                repaint();
            }

            try {
                Thread.sleep(16); // Approx. 60 FPS (1000 ms / 60 ≈ 16 ms)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stopGame() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawObjects(g);
        drawEdge(g);
        if (pause) drawPauseText(g);
        endGame(g);
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public static void decrementEnemies(int num) {
        numEnemies -= num;
    }

    public static int getStage() {
        return stage;
    }

    public static void setEndGame() {
        soundUtility.gameOver();
        gameOver = true;
    }

    public void restart() {
        reset();

        running = true;
        initBoard();
        repaint();

        startGame();
    }

    public void reset() {
        enemy.clear();
        blocks.clear();
        animations.clear();
        powerUps.clear();
        playerTanks.clear();

        CollisionUtility.resetScore();

        gameOver = false;
        pause = false;
        numAI = 0;
        numEnemies = goal;
        stage = 1;
    }

    private void initBoard() {
        stage = 1;
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(Map.BOARD_WIDTH, Map.BOARD_HEIGHT));

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

    private void drawObjects(Graphics g) {
        for (NPCTank NPCTank : enemy) {
            if (NPCTank.isVisible()) {
                g.drawImage(NPCTank.getImage(), NPCTank.getX(), NPCTank.getY(), this);
            } else {
                enemy.remove(NPCTank); // Safely remove invisible NPCTank
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
            } else {
                blocks.remove(a);
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

    private void drawEdge(Graphics g) {
        Font font = fontUtility.getPrstart();
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

    private void drawPauseText(Graphics g) {
        String pauseText = "PAUSE";
        Font font = fontUtility.getPrstart().deriveFont(Font.BOLD, 30);
        g.setFont(font);
        g.setColor(Color.WHITE);

        g.drawString(pauseText, getWidth() / 2 - g.getFontMetrics().stringWidth(pauseText) / 2, getHeight() / 2);
    }

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

    private void updateSprites() {
        spawnTankAI();
        spawnPowerUp();
        updateTank();
        updateTankAI();
        updateBullets();
        updateBlocks();
        updateAnimations();
        updatePowerUps();
    }

    private void updateAnimations() {
        BoardUtility.updateAnimations();
    }

    private void updateBlocks() {
        BoardUtility.updateBlocks();
    }

    private void updateTank() {
        BoardUtility.updateTank();
    }

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

    private void updatePowerUps() {
        BoardUtility.updatePowerUps();
    }

    private void spawnPowerUp() {
        BoardUtility.spawnPowerUp();
    }

    private void updateBullets() {
        BoardUtility.updateBulletsTank();
        BoardUtility.updateBulletsTankAI();
    }

    private void checkCollisions() {
        BoardUtility.checkCollisions();
    }

    private void endGame(Graphics g) {
        if (gameOver) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());


            Image gameOverImage = imageInstance.getGameOver();

            int xPos = getWidth() / 2 - gameOverImage.getWidth(null) / 2;
            int yPos = getHeight() / 2 - gameOverImage.getHeight(null) / 2;

            g.drawImage(gameOverImage, xPos, yPos, this);

            Timer sorceBoardTimer = new Timer(3000, e -> loadScoreBoard(theView));
            sorceBoardTimer.setRepeats(false);
            sorceBoardTimer.start();
        }
    }

    private void loadScoreBoard(GameView theView) {
        theView.getGamePanel().removeAll();
        ScoreBoard scoreBoard = new ScoreBoard(theView, this);
        scoreBoard.setBackground(Color.BLACK);
        theView.getGamePanel().add(scoreBoard);
        scoreBoard.requestFocusInWindow();
        soundUtility.statistics();
        theView.setVisible(true);
    }

    private void clearBoard() {
        animations.clear();
        blocks.clear();
        powerUps.clear();
        enemy.clear();
        updateSprites();
        resetTankPosition(player1Tank, 2);
        if (isTwoPlayerMode) {
            resetTankPosition(player2Tank, 2);
        }
        loadCollisionUtility(blocks, animations);
    }

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
                repaint();
            }
        }
    }
}
