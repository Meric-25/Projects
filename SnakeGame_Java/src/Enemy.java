import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Enemy {
    private int x;
    private int y;
    private int dx;
    private int dy;
    private static final int SIZE = 30;
    private Random random;
    private boolean active;
    private int moveCounter = 0;
    private static final int MOVE_SPEED = 3;
    private long lastDirectionChange;
    private static final long DIRECTION_CHANGE_INTERVAL = 10000; // 10 seconds

    public Enemy() {
        random = new Random();
        active = false;
    }

    public void spawn() {
        x = random.nextInt(20) * SIZE;
        y = random.nextInt(20) * SIZE;

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
        if (moveCounter < MOVE_SPEED) return;
        moveCounter = 0;

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


        x += dx * SIZE;
        y += dy * SIZE;


        if (x < 0 || x >= 600) {
            dx = -dx;
            x = Math.max(0, Math.min(x, 600 - SIZE));
        }
        if (y < 0 || y >= 600) {
            dy = -dy;
            y = Math.max(0, Math.min(y, 600 - SIZE));
        }
    }


    public int checkCollision(ArrayList<Point> snake) {
        if (!active || snake.isEmpty()) return 0;


        Point head = snake.get(0);
        if (head.x == x && head.y == y) {
            return 1;
        }

        for (int i = 1; i < snake.size(); i++) {
            Point segment = snake.get(i);
            if (segment.x == x && segment.y == y) {
                return i;
            }
        }

        return 0; // No collision
    }

    public void draw(Graphics g) {
        if (!active) return;


        g.setColor(Color.ORANGE);
        g.fillOval(x, y, SIZE, SIZE);


        g.setColor(Color.DARK_GRAY);
        g.fillOval(x + 8, y + 8, SIZE - 16, SIZE - 16);


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