import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Apple {
    private int redX;
    private int redY;

    private int tempX;
    private int tempY;
    private Color tempColor;

    private long tempTime;
    private boolean tempActive;

    private static final int SIZE = 30;
    private static final long VISIBLE_TIME = 10000; // 10 seconds visible ,1000 is 1 sec
    private static final long HIDDEN_TIME = 2000; // 2 seconds hidden
    private Random random;

    public Apple() {
        random = new Random();
    }

    public void spawnRed(ArrayList<Point> snake) {
        do {
            redX = random.nextInt(20) * SIZE;
            redY = random.nextInt(20) * SIZE;
        } while (isOnSnake(redX, redY, snake));
    }

    public void spawnTemp(ArrayList<Point> snake) {
        do {
            tempX = random.nextInt(20) * SIZE;
            tempY = random.nextInt(20) * SIZE;
        } while (isOnSnake(tempX, tempY, snake) || (tempX == redX && tempY == redY));


        int choice = random.nextInt(3);
        if (choice == 0) {
            tempColor = Color.GREEN;// Poison
        } else if (choice == 1) {
            tempColor = Color.BLUE;// Speed boost
        } else if (choice == 2) {
            tempColor = Color.PINK;//Slow Down
        }

        tempTime = System.currentTimeMillis();
        tempActive = true;
    }

    private boolean isOnSnake(int x, int y, ArrayList<Point> snake) {
        for (Point p : snake) {
            if (p.x == x && p.y == y) {
                return true;
            }
        }
        return false;
    }

    public void update(ArrayList<Point> snake) {
        long currentTime = System.currentTimeMillis();

        if (tempActive && currentTime - tempTime > VISIBLE_TIME) {
            tempActive = false;
            tempTime = currentTime;
        } else if (!tempActive && currentTime - tempTime > HIDDEN_TIME) {
            spawnTemp(snake);
        }
    }

    // apple types => 1 = red, 2 = green, 3 = blue
    public int checkCollision(int headX, int headY, ArrayList<Point> snake) {

        if (headX == redX && headY == redY) {
            spawnRed(snake);
            return 1; // Red
        }


        if (tempActive && headX == tempX && headY == tempY) {
            int appleType;
            if (tempColor.equals(Color.GREEN)) {
                appleType = 2; // Green
            } else if (tempColor.equals(Color.BLUE)) {
                appleType = 3; // Blue
            } else {
                appleType = 4; // Pink
            }
            spawnTemp(snake);
            return appleType;
        }

        return 0;
    }

    public void draw(Graphics g) {

        g.setColor(Color.RED);
        g.fillOval(redX, redY, SIZE, SIZE);

        if (tempActive) {
            g.setColor(tempColor);
            g.fillOval(tempX, tempY, SIZE, SIZE);
        }
    }
}