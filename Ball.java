import java.awt.Graphics;
import java.awt.image.ImageObserver;

public class Ball {
    public static final int SIZE = 5;
    private float x;
    private float y;
    private float vy;
    private float vx;
    private int directionX;
    public static float e = 10f/20f;

    public Ball(float x, float y, float vx, float vy, int angle) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.directionX = angle;
    }

    public void tick() {
        this.vy += Board.GRAVITATION * Board.DELTA_TIME;
        this.vx *= Math.cos(Math.toRadians(directionX));
        float newY = this.y + this.vy * Board.DELTA_TIME;
        float newX = this.x + this.vx * Board.DELTA_TIME;
        if (newX < Board.WIDTH - SIZE / 2 && newX > SIZE / 2)
            this.x = newX;
        else {
            this.vx = -this.vx * e;
            this.directionX = -this.directionX;
        }
        if (newY < (Board.HEIGHT - SIZE / 2) && newY > SIZE / 2) 
            this.y = newY;
        else 
            this.vy = -this.vy * e;  
    }

    public float getY() {
        return this.y;
    }

    public float getX() {
        return this.x;
    }

    public float getVx() {
        return this.vx;
    }

    public float getVy() {
        return this.vy;
    }

    public void collision(Ball ball) {
        float distX = Math.abs((this.x - ball.getX())) / 2;
        float distY = Math.abs((this.y - ball.getY())) / 2;
        int coefX = this.x > ball.getX() ? 1 : -1;
        float newB1X = this.x + coefX * distX / 2 + ball.getVx() * Board.DELTA_TIME;
        float newB2X = ball.getX() - coefX * distX / 2 + this.vx * Board.DELTA_TIME;
        if (newB1X < Board.WIDTH - SIZE / 2 && newB1X > 0 + SIZE / 2)
            this.x = newB1X;
        if (newB2X < Board.WIDTH - SIZE / 2 && newB2X > 0 + SIZE / 2)
            ball.setX(newB2X);

        int coefY = this.y > ball.getY() ? 1 : -1;
        float newB1Y = this.y + coefY * distY / 2 + ball.getVy() * Board.DELTA_TIME;
        float newB2Y = ball.getY() - coefY * distY / 2 + this.vy * Board.DELTA_TIME;
        if (newB1Y < Board.HEIGHT - SIZE / 2 && newB1Y > 0 + SIZE / 2)
            this.y = newB1Y;
        if (newB2Y < Board.HEIGHT - SIZE / 2 && newB2Y > 0 + SIZE / 2)
            ball.setY(newB2Y);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isColliding(Ball ball) {
        return Math.abs(this.x - ball.getX()) < SIZE && Math.abs(this.y - ball.getY()) < SIZE;
    } 
    

    public void draw(Graphics g, ImageObserver observer) {
        g.fillOval((int)(this.getX() - SIZE / 2), (int)(this.getY() - SIZE / 2), SIZE, SIZE);
    }

}
