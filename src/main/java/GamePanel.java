import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 150;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;


    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    private void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (running) {
            //Show grid lines
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                g.setColor(Color.WHITE);
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
            showScore(g);
        }else{
            gameOver(g);
        }
    }

    private void showScore(Graphics g){
        g.setColor(Color.RED);
        g.setFont(new Font("Tim", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score " + applesEaten))/2, g.getFont().getSize());
    }

    private void showGameOver(Graphics g){
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over !", (SCREEN_WIDTH - metrics.stringWidth("Game Over !"))/2, SCREEN_HEIGHT / 2);
    }

    private void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    private void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U': {
                y[0] = y[0] - UNIT_SIZE;
                break;
            }
            case 'D': {
                y[0] = y[0] + UNIT_SIZE;
                break;
            }
            case 'L': {
                x[0] = x[0] - UNIT_SIZE;
                break;
            }
            case 'R': {
                x[0] = x[0] + UNIT_SIZE;
                break;
            }
        }
    }

    private void checkApples() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
            timer.setDelay(DELAY - applesEaten + 5);
            timer.restart();
        }
    }

    private void checkCollisions() {
        //Checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        //Check if head touches left border
        if (x[0] < 0) running = false;

        //Check if head touches right border
        if (x[0] > SCREEN_WIDTH) running = false;

        //Check if head touches top border
        if (y[0] < 0) running = false;

        //Check if head touches right border
        if (y[0] > SCREEN_HEIGHT) running = false;

        if (!running) timer.stop();

    }

    private void gameOver(Graphics g) {
        //Game over text
        showScore(g);
        showGameOver(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApples();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: {
                    if (direction != 'R') direction = 'L';
                    break;
                }
                case KeyEvent.VK_RIGHT: {
                    if (direction != 'L') direction = 'R';
                    break;
                }
                case KeyEvent.VK_UP: {
                    if (direction != 'D') direction = 'U';
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    if (direction != 'U') direction = 'D';
                    break;
                }
            }
        }
    }
}
