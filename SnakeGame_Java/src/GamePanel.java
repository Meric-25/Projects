import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GamePanel extends JPanel {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int SIZE = 30;

    private ArrayList<Point> snake = new ArrayList<>();
    private Apple apple;
    private Enemy enemy;
    private char direction = 'R';
    private boolean running = true;
    private int score = 0;


    private Timer timer;
    private int normalSpeed = 100;
    private int boostSpeed = 50; // Twice as fast
    private int slowSpeed = 200; // Twics as slow
    private long speedBoostEndTime = 0;
    private boolean speedBoostActive = false;
    private long slowDownEndTime = 0;
    private boolean slowDownActive = false;


    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {

                if (running) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT && direction != 'R') direction = 'L';
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT && direction != 'L') direction = 'R';
                    if (e.getKeyCode() == KeyEvent.VK_UP && direction != 'D') direction = 'U';
                    if (e.getKeyCode() == KeyEvent.VK_DOWN && direction != 'U') direction = 'D';
                }

                if (!running && (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)) {
                    restartGame();
                }
            }
        });


        snake.add(new Point(300, 300));
        snake.add(new Point(270, 300));
        snake.add(new Point(240, 300));


        apple = new Apple();
        apple.spawnRed(snake);
        apple.spawnTemp(snake);

        enemy = new Enemy();
        enemy.spawn();


        timer = new Timer(normalSpeed, e -> {
            if (running) {
                apple.update(snake);
                checkSpeedEffects();
                enemy.move();
                move();
                checkApple();
                checkEnemy();
                checkDeath();
                repaint();
            }
        });
        timer.start();
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head);

        if (direction == 'U') newHead.y -= SIZE;
        if (direction == 'D') newHead.y += SIZE;
        if (direction == 'L') newHead.x -= SIZE;
        if (direction == 'R') newHead.x += SIZE;

        snake.add(0, newHead);
        snake.remove(snake.size() - 1);
    }

    private void checkApple() {
        Point head = snake.get(0);
        int appleType = apple.checkCollision(head.x, head.y, snake);

        if (appleType == 1) {
            // RED APPLE: +1 point
            score++;
            snake.add(new Point(-100, -100));
        }
        else if (appleType == 2) {
            // GREEN APPLE: POISON!
            running = false;
        }
        else if (appleType == 3) {
            // BLUE APPLE: Speed boost
            score++;
            snake.add(new Point(-100, -100));
            activateSpeedBoost();
        }
        else if (appleType == 4) {
            // PINK APPLE: Slow down
            score += 5;
            snake.add(new Point(-100, -100));
            activateSlowDown();
        }

    }

    private void activateSpeedBoost() {
        slowDownActive = false;

        speedBoostActive = true;
        speedBoostEndTime = System.currentTimeMillis() + 5000; // 5 seconds
        timer.setDelay(boostSpeed);
    }

    private void activateSlowDown() {
        // Cancel speed boost if active
        speedBoostActive = false;

        slowDownActive = true;
        slowDownEndTime = System.currentTimeMillis() + 5000; // 5 seconds
        timer.setDelay(slowSpeed);
    }

    private void checkSpeedEffects() {
        if (speedBoostActive && System.currentTimeMillis() >= speedBoostEndTime) {
            speedBoostActive = false;
            timer.setDelay(normalSpeed);
        }

        if (slowDownActive && System.currentTimeMillis() >= slowDownEndTime) {
            slowDownActive = false;
            timer.setDelay(normalSpeed);
        }
    }

    private void checkDeath() {
        Point head = snake.get(0);

        // Wall collision
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            running = false;
        }

        // Self collision
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
            }
        }
    }

    private void checkEnemy() {
        int collision = enemy.checkCollision(snake);

        if (collision == 1) {
            // Hit head - game over
            running = false;
        } else if (collision > 1) {
            // Hit body - cut snake in half at that position
            // Remove all segments after the collision point
            while (snake.size() > collision) {
                snake.remove(snake.size() - 1);
            }

        }
    }

    private void restartGame() {
        // Reset snake
        snake.clear();
        snake.add(new Point(300, 300));
        snake.add(new Point(270, 300));
        snake.add(new Point(240, 300));

        direction = 'R';
        score = 0;
        speedBoostActive = false;
        slowDownActive = false;
        timer.setDelay(normalSpeed);

        apple.spawnRed(snake);
        apple.spawnTemp(snake);

        running = true;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        apple.draw(g);

        enemy.draw(g);

        // Draw snake
        for (int i = 0; i < snake.size(); i++) {
            Point p = snake.get(i);
            if (i == 0) {
                // Change head color based on active effect
                if (speedBoostActive) {
                    g.setColor(Color.BLUE);
                } else if (slowDownActive) {
                    g.setColor(Color.PINK);
                } else {
                    g.setColor(Color.GREEN);
                }
            } else {
                g.setColor(Color.YELLOW);
            }
            g.fillRect(p.x, p.y, SIZE, SIZE);
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);

        // Show speed boost timer
        if (speedBoostActive) {
            g.setColor(Color.CYAN);
            long timeLeft = (speedBoostEndTime - System.currentTimeMillis()) / 1000 + 1;
            g.drawString("SPEED BOOST: " + timeLeft + "s", 10, 40);
        }

        if (slowDownActive) {
            g.setColor(Color.PINK);
            long timeLeft = (slowDownEndTime - System.currentTimeMillis()) / 1000 + 1;
            g.drawString("SLOW DOWN: " + timeLeft + "s", 10, 40);
        }

        // Draw apple legend
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Red: +1 point", 450, 20);
        g.setColor(Color.GREEN);
        g.drawString("Green: POISON!", 450, 40);
        g.setColor(Color.BLUE);
        g.drawString("Blue: Speed Boost 5s", 450, 60);
        g.setColor(Color.PINK);
        g.drawString("Pink: Slow Down 5s", 450, 80);

        // Game over
        if (!running) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", 180, 300);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Final Score: " + score, 230, 340);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Press SPACE or ENTER to restart", 180, 380);
        }
    }
}