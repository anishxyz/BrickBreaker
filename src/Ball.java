import java.awt.*;

/**
 * Ball object which is essentially a circle that bounces acorss the screen
 * and used to destroy bricks.
 */
public class Ball extends GameObj {

    //ball constants
    public static final int RADIUS = 8;
    public static final int INIT_POS_X = 406;
    public static final int INIT_POS_Y = 450;
    public static final int INIT_VEL_X = 6;
    public static final int INIT_VEL_Y = 8;

    private Color color;

    public Ball(int courtWidth, int courtHeight, Color color) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X,
                INIT_POS_Y, RADIUS, RADIUS, courtWidth, courtHeight);

        this.color = color;
    }


    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}