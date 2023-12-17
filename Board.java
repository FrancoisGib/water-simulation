
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;

public class Board extends JPanel implements ActionListener, KeyListener {

    private static final int FPS = 60;
    public static final int DELTA_TIME = 1000 / FPS;
    public static final float GRAVITATION = 0.981f / 1000;
    public static final int WIDTH = 540;
    public static final int HEIGHT = 540;
    private static final int COLUMNS = (int)(WIDTH / (Ball.SIZE * 2));
    private static final int ROWS = (int)(HEIGHT / (Ball.SIZE * 2));
    private int cpt = 0;
    private boolean showGrid = false;
    public static boolean gravity = true;
    private boolean generate = false;

    private List<Ball> balls = new ArrayList<>();

    private Timer timer;
    public Board() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        for (int i = 0; i < 10000; i++) {
            this.balls.add(new Ball((float) Math.random() * 100 + 200, (float) Math.random() * 100 + 200,
                    (float) Math.random() > 0.5 ? .5f : -.5f, .0f, 10)); // (float) Math.random() > 0.5 ? .5f : -.5f
        }
        timer = new Timer(DELTA_TIME, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.cpt++;
        if (this.cpt > 2 * FPS && this.generate) {
            this.cpt = 0;
            int defaultX = (int)(Math.random() * (WIDTH * .9f) + (.1f * WIDTH));
            for (int i = 0; i < 1000; i++) {
            this.balls.add(new Ball((float)(defaultX + Math.random() * (WIDTH * .1f) - (.2f * WIDTH)), (float) Math.random() * 100 + 200,
                    .0f, .0f, 0));
            }
        }
        HashMap<Integer, List<Ball>> partitionedBalls = new HashMap<>();
        for (Ball ball : balls) {
            ball.tick();
            int x = (int) (Math.floor(ball.getX() / WIDTH * COLUMNS));
            int y = (int) (Math.floor(ball.getY() / HEIGHT * ROWS));
            int block = x + y * ROWS;
            if (!partitionedBalls.containsKey(block))
                partitionedBalls.put(block, new ArrayList<>());
            partitionedBalls.get(block).add(ball);
        }
        for (int i = 1; i < COLUMNS + 1; i++) {
            for (int j = 1; j < ROWS + 1; j++) {
                List<Ball> box = partitionedBalls.get(i + j * ROWS);
                if (box != null) {
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            List<Ball> neighbors = partitionedBalls.get(k + l * ROWS);
                            if (neighbors != null) {
                                for (Ball b1 : box) {
                                    for (Ball b2 : neighbors) {
                                        if (b1.isColliding(b2) && b1 != b2)
                                            b1.collision(b2);
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        if (this.showGrid) {
            for (int i = 0; i < COLUMNS; i++) {
                g.drawLine(0, i * HEIGHT / COLUMNS, WIDTH, i * HEIGHT / COLUMNS);
                g.drawLine(i * WIDTH / ROWS, 0, i * WIDTH / ROWS, HEIGHT);
            }
        }
        for (Ball ball : balls) {
            ball.draw(g, this);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S)
            this.showGrid = !this.showGrid;
        else if (e.getKeyCode() == KeyEvent.VK_G)
            Board.gravity = !Board.gravity;
        else if (e.getKeyCode() == KeyEvent.VK_N)
            this.generate = !this.generate;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
