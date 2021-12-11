import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;

public class GameCourt extends JPanel {

    //Court Size Constants
    final static int COURT_WIDTH = 800 + 12;
    final static int COURT_HEIGHT = 1000;

    //Coloring Constants
    final static Color COURT_COLOR = Color.WHITE;
    final static Color PADDLE_COLOR = Color.RED;
    final static Color BALL_COLOR = Color.BLACK;
    final static Color BRICK_COLOR = Color.BLUE;
    final static Color SPEED_BRICK_COLOR = Color.YELLOW;
    final static Color SLOW_BRICK_COLOR = new Color(181,220,219);
    final static Color DOUBLE_BRICK_COLOR_ONE = new Color(255,102,0);
    final static Color DOUBLE_BRICK_COLOR_TWO = new Color(255,203,164);

    //Brick Construction Constants
    final static int BRICK_ROWS = 10;
    final static int BRICK_PER_ROW = 10;
    final static int BRICK_SPACE_X = 2;
    final static int BRICK_SPACE_Y = 2;
    final static int MAX_SPECIAL_BRICKS = (BRICK_ROWS * BRICK_PER_ROW) / 3;

    //Paddle Speed
    final static int PADDLE_SPEED = 13;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 20;

    //Game Pieces
    private Ball ball;
    private Paddle paddle;
    private Brick[][] bricks = new Brick[BRICK_ROWS][BRICK_PER_ROW];

    //Game Score
    private int score;
    final static int MAX_SCORE = BRICK_ROWS * BRICK_PER_ROW;

    //Leaderboard
    private String user;
    private static Map<String, Integer> leaderboard = new TreeMap<>();

    //Game State
    private boolean playing = false; // whether the game is running
    private JLabel status; // Current status text, i.e. "Running..."

    //user getter
    public String getUser() {
        return this.user;
    }

    //user setter
    public void setUser(String u) {
        this.user = u;
    }

    //score increment
    public void incrementScore() {
        score++;
    }

    //court color getter
    public static Color getCourtColor() {
        return COURT_COLOR;
    }

    //double brick color one getter
    public static Color getDoubleBrickColorOne() {
        return DOUBLE_BRICK_COLOR_ONE;
    }

    //double brick color two getter
    public static Color getDoubleBrickColorTwo() {
        return DOUBLE_BRICK_COLOR_TWO;
    }

    /**
     * Returns a string listing the top three players on the leaderboard
     *
     * @return string with top 3 users and score, each on a new line
     */
    public static String getLeaderboard() {
        if (leaderboard.size() == 0) {
            return "No players have played.";
        } else {
            // converts map to list to be sorted
            List<Map.Entry<String, Integer>> lead = new ArrayList<>(leaderboard.entrySet());
            lead.sort(Map.Entry.comparingByValue());
            Collections.reverse(lead);

            String s = "User : Score\n";

            //loop that creates a new line with each ordered user/score pair
            for (Map.Entry<String, Integer> entry : lead) {
                String temp = entry.getKey() + ": " + entry.getValue() + "\n";
                s = s + temp;
            }

            return s;
        }
    }

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(COURT_COLOR);

        // The timer is an object which triggers an action periodically with the
        // given INTERVAL. We register an ActionListener with this timer, whose
        // actionPerformed() method is called each time the timer triggers. We
        // define a helper method called tick() that actually does everything
        // that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    paddle.setVx(-PADDLE_SPEED);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    paddle.setVx(PADDLE_SPEED);
                } else if (e.getKeyCode() == KeyEvent.VK_N) {
                    reset();
                }
            }

            public void keyReleased(KeyEvent e) {
                paddle.setVx(0);
                paddle.setVy(0);
            }
        });

        this.status = status;
    }

    /**
     * Randomly generate a legal position of brick on game board.
     *
     * @return int[2] which contains the x and y position of brick
     */
    public int[] randomBrick() {
        // find i value for brick in matrix between 0 and BRICK_ROWS
        int i = (int)(Math.random() * BRICK_ROWS);
        //find j value for brick in matrix between 0 and BRICKS_PER_ROW
        int j = (int)(Math.random() * BRICK_PER_ROW);
        return new int[]{i, j};
    }

    public ArrayList<int[]> specialBrickPositionList() {
        ArrayList<int[]> s = new ArrayList<>();

        for (int i = 0; i <= MAX_SPECIAL_BRICKS; i++) {
            while (s.size() != i + 1) {
                int[] r = randomBrick();
                if (!s.contains(r)) {
                    s.add(r);
                }
            }
            //loop test
            //System.out.println("Size: " + s.size() + "Entry" + Arrays.toString(s.get(i)));
        }

        return s;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        score = 0;
        paddle = new Paddle(COURT_WIDTH, COURT_HEIGHT, PADDLE_COLOR);
        ArrayList<int[]> sbp = specialBrickPositionList();
        /*
        for (int i = 0; i < sbp.size(); i++) {
            // Printing and display the elements in ArrayList for testing below
            System.out.print(Arrays.toString(sbp.get(i)) + "\n");
        }*/

        for (int y = 0; y < BRICK_ROWS; y++) {
            for (int x = 0; x < BRICK_PER_ROW; x++) {
                int brickX = x * Brick.getBrickWidth() + x * BRICK_SPACE_X;
                int brickY = y * Brick.getBrickHeight() + y * BRICK_SPACE_Y;
                Brick brick = null;
                for (int[] b : sbp) {
                    if (b[0] == y && b[1] == x) {
                        int r = (int)(Math.random() * (3));
                        if (r == 0) {
                            brick = new DoubleBrick(COURT_WIDTH,
                                    COURT_HEIGHT, brickX, brickY, DOUBLE_BRICK_COLOR_ONE);
                        }
                        if (r == 1) {
                            brick = new SpeedBrick(
                                    COURT_WIDTH, COURT_HEIGHT, brickX, brickY, SPEED_BRICK_COLOR);
                        }
                        if (r == 2) {
                            brick = new SlowBrick(
                                    COURT_WIDTH, COURT_HEIGHT, brickX, brickY, SLOW_BRICK_COLOR);
                        }
                    }
                }
                if (brick == null) {
                    brick = new Brick(COURT_WIDTH, COURT_HEIGHT, brickX, brickY, BRICK_COLOR);
                }
                bricks[y][x] = brick;
            }
        }

        ball = new Ball(COURT_WIDTH, COURT_HEIGHT, BALL_COLOR);
        ball.setVx(randomVelocity());

        playing = true;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    //reset game with new player
    public void reset(String u) {
        reset();
        user = u;
    }

    public int randomVelocity() {
        int x = (int)(Math.random() * (2));
        if (x == 0) {
            return (int)(3 + Math.random() * (6));
        }
        return -(int)(3 + Math.random() * (6));
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {

            // update paddle position
            paddle.move();

            //update ball/implement ball physics
            ball.bounce(ball.hitWall());
            ball.bounce(ball.hitObj(paddle));
            ball.move();


            //handle brick contact and score
            for (int i = 0; i < BRICK_ROWS; i++) {
                for (int j = 0; j < BRICK_PER_ROW; j++) {
                    bricks[i][j].interact(this, ball);
                }
            }

            //update score status
            status.setText("Running... -- Player: " + user + " -- Score: " + score);

            // check for the game end conditions
            if (ballDead() && score < MAX_SCORE) {
                playing = false;
                status.setText("-- You lose! -- Score: " + score + " -- Press N to play again. --");
                updateLeaderboard();
                //saveState(); //testing save state writing to file
            } else if (score >= MAX_SCORE) {
                playing = false;
                status.setText("-- Player: " + user + " -- You WIN! -- Score: "
                        + score + " -- Press N to play again. --");
                //update leaderboard scores
                updateLeaderboard();
            }
        }
        // update the display
        repaint();
    }


    /**
     * Updates the leaderboard by adding score to map
     * Checks if new score is higher than the current
     * users previous score and only updates if user
     * scores more than all previous attempts
     */
    public void updateLeaderboard() {
        if (user != null) {
            if (leaderboard.containsKey(user)) {
                if (leaderboard.get(user) < score) {
                    leaderboard.put(user, score);
                }
            } else {
                leaderboard.put(user, score);
            }
        }
    }

    /**
     * Checks if all balls on the game field are above the paddle.
     * @return boolean true if all balls below paddle, else false
     */
    public Boolean ballDead() {
        return (ball.getPy() > paddle.getPy());
    }


    /**
     * Method that saves the game state by writing to a file called gameState.txt
     * Save the brick layout, current user and score, and leaderboard
     */
    public void saveState() {
        try {
            FileWriter myWriter = new FileWriter("src//gameState.txt", false);
            myWriter.write("---Brick Layout---\n");
            for (int i = 0; i < BRICK_ROWS; i++) {
                for (int j = 0; j < BRICK_PER_ROW; j++) {
                    if (bricks[i][j].getColor().equals(BRICK_COLOR)) {
                        myWriter.write("BASE\n");
                    }
                    if (bricks[i][j].getColor().equals(COURT_COLOR)) {
                        myWriter.write("DEAD\n");
                    }
                    if (bricks[i][j].getColor().equals(SPEED_BRICK_COLOR)) {
                        myWriter.write("SPEED\n");
                    }
                    if (bricks[i][j].getColor().equals(SLOW_BRICK_COLOR)) {
                        myWriter.write("SLOW\n");
                    }
                    if (bricks[i][j].getColor().equals(DOUBLE_BRICK_COLOR_ONE)) {
                        myWriter.write("DOUBLE_ONE\n");
                    }
                    if (bricks[i][j].getColor().equals(DOUBLE_BRICK_COLOR_TWO)) {
                        myWriter.write("DOUBLE_TWO\n");
                    }
                }
            }
            myWriter.write("---User and Score---\n");
            myWriter.write(user + "\n");
            myWriter.write(score + "\n");
            myWriter.write("---Leaderboard---\n");
            for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
                myWriter.write(entry.getKey() + "\n" + entry.getValue() + "\n");
            }
            myWriter.write("Complete Save");
            myWriter.close();
            System.out.println("Complete Save.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Takes in a correctly formatted file and loads a file
     *
     * @param fileName fileName with game state stored
     */
    public void loadState(String fileName) {
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                if (myReader.nextLine().equals("---Brick Layout---")) {
                    for (int i = 0; i < BRICK_ROWS; i++) {
                        for (int j = 0; j < BRICK_PER_ROW; j++) {
                            String brickType = myReader.nextLine();
                            int brickX = j * Brick.getBrickWidth() + j * BRICK_SPACE_X;
                            int brickY = i * Brick.getBrickHeight() + i * BRICK_SPACE_Y;
                            Brick brick = null;
                            Color col = generateColor(brickType);
                            if (col.equals(BRICK_COLOR)) {
                                brick = new Brick(
                                        COURT_WIDTH, COURT_HEIGHT, brickX, brickY, col);
                            }
                            if (col.equals(COURT_COLOR)) {
                                brick = new Brick(
                                        COURT_WIDTH, COURT_HEIGHT, brickX, brickY, col);
                            }
                            if (col.equals(SPEED_BRICK_COLOR)) {
                                brick = new SpeedBrick(
                                        COURT_WIDTH, COURT_HEIGHT, brickX, brickY, col);
                            }
                            if (col.equals(SLOW_BRICK_COLOR)) {
                                brick = new SlowBrick(
                                        COURT_WIDTH, COURT_HEIGHT, brickX, brickY, col);
                            }
                            if (col.equals(DOUBLE_BRICK_COLOR_ONE)) {
                                brick = new DoubleBrick(
                                        COURT_WIDTH, COURT_HEIGHT, brickX, brickY, col);
                            }
                            if (col.equals(DOUBLE_BRICK_COLOR_TWO)) {
                                brick = new DoubleBrick(
                                        COURT_WIDTH, COURT_HEIGHT, brickX, brickY, col);
                            }
                            bricks[i][j] = brick;
                        }
                    }
                }
                if (myReader.nextLine().equals("---User and Score---")) {
                    user = myReader.nextLine();
                    score = getInteger(myReader.nextLine());
                }
            }
            ball = new Ball(COURT_WIDTH, COURT_HEIGHT, BALL_COLOR);
            ball.setVx(randomVelocity());
            playing = true;
            status.setText("Running...");
            requestFocusInWindow();
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Takes in a string and attempts to extract an integer
     *
     * @param str string to be parsed for integer
     * @return integer in string if possible
     */
    public int getInteger(String str) {
        int number = 0;
        try {
            number = Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            System.out.println("Not a number");
            ex.printStackTrace();
        }
        return number;
    }

    /**
     * Returns color of brick based on inputted string
     * Used as helper function for loading state by allowing easy way
     * to convert strings in file to read color
     *
     * @param b String denoting the brick state
     * @return Color given by string
     */
    public Color generateColor(String b) {
        Color col = null;
        if (b.equals("BASE")) {
            col = BRICK_COLOR;
        }
        if (b.equals("DEAD")) {
            col = COURT_COLOR;
        }
        if (b.equals("SPEED")) {
            col = SPEED_BRICK_COLOR;
        }
        if (b.equals("SLOW")) {
            col = SLOW_BRICK_COLOR;
        }
        if (b.equals("DOUBLE_ONE")) {
            col = DOUBLE_BRICK_COLOR_ONE;
        }
        if (b.equals("DOUBLE_TWO")) {
            col = DOUBLE_BRICK_COLOR_TWO;
        }
        return col;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paddle.draw(g);
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_PER_ROW; j++) {
                bricks[i][j].draw(g);
            }
        }
        ball.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}