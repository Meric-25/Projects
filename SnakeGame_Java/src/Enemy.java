import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Enemy {
    private int x;
    private int y;
    private int dx; // direction x (-1, 0, or 1)
    private int dy; // direction y (-1, 0, or 1)
    private static final int SIZE = 30;
    private Random random;
    private boolean active;
    private int moveCounter = 0;
    private static final int MOVE_SPEED = 3; // Moves every 3 frames
    private long lastDirectionChange;
    private static final long DIRECTION_CHANGE_INTERVAL = 10000; // 10 seconds

    public Enemy() {
        random = new Random();
        active = false;
    }

    public void spawn() {
        // Spawn at random grid position
        x = random.nextInt(20) * SIZE;
        y = random.nextInt(20) * SIZE;

        // Random direction (4 main directions only for simplicity)
        int dir = random.nextInt(4);
        switch (dir) {
            case 0: dx = 1; dy = 0; break;   // Right
            case 1: dx = -1; dy = 0; break;  // Left
            case 2: dx = 0; dy = 1; break;   // Down
            case 3: dx = 0; dy = -1; break;  // Up
        }

        lastDirectionChange = System.currentTimeMillis();
        active = true;
    }

    public void move() {
        if (!active) return;

        moveCounter++;
        if (moveCounter < MOVE_SPEED) return; // Only move every few frames
        moveCounter = 0;

        // Check if 10 seconds have passed, then change direction
        if (System.currentTimeMillis() - lastDirectionChange >= DIRECTION_CHANGE_INTERVAL) {
            int dir = random.nextInt(4);
            switch (dir) {
                case 0: dx = 1; dy = 0; break;   // Right
                case 1: dx = -1; dy = 0; break;  // Left
                case 2: dx = 0; dy = 1; break;   // Down
                case 3: dx = 0; dy = -1; break;  // Up
            }
            lastDirectionChange = System.currentTimeMillis();
        }

        // Move by one grid unit
        x += dx * SIZE;
        y += dy * SIZE;

        // Bounce off walls
        if (x < 0 || x >= 600) {
            dx = -dx;
            x = Math.max(0, Math.min(x, 600 - SIZE));
        }
        if (y < 0 || y >= 600) {
            dy = -dy;
            y = Math.max(0, Math.min(y, 600 - SIZE));
        }
    }

    // Returns: 0 = no collision, 1 = hit head (game over), 2+ = hit body (cut at index)
    public int checkCollision(ArrayList<Point> snake) {
        if (!active || snake.isEmpty()) return 0;

        // Check head collision (game over)
        Point head = snake.get(0);
        if (head.x == x && head.y == y) {
            return 1; // Hit head - game over
        }

        // Check body collision (cut snake)
        for (int i = 1; i < snake.size(); i++) {
            Point segment = snake.get(i);
            if (segment.x == x && segment.y == y) {
                return i; // Return the index where snake should be cut
            }
        }

        return 0; // No collision
    }

    public void draw(Graphics g) {
        if (!active) return;

        // Draw a simple spinning sawblade - orange circle with dark center
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, SIZE, SIZE);

        // Dark center
        g.setColor(Color.DARK_GRAY);
        g.fillOval(x + 8, y + 8, SIZE - 16, SIZE - 16);

        // Simple blade marks
        g.setColor(Color.RED);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        int center = SIZE / 2;
        g2d.drawLine(x + center, y + 2, x + center, y + SIZE - 2); // Vertical
        g2d.drawLine(x + 2, y + center, x + SIZE - 2, y + center); // Horizontal
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}