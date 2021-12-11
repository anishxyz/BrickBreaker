package org.cis120;

import java.awt.*;

/**
 * Paddle is a rectangle object that slides horizontally across the
 * bottom of the screen and operated by user input.
 */
public class Paddle extends GameObj {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 15;
    public static final int INIT_POS_X = 425;
    public static final int INIT_POS_Y = 900;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;

    private Color color;

    public Paddle(int courtWidth, int courtHeight, Color color) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X,
                INIT_POS_Y, WIDTH, HEIGHT, courtWidth, courtHeight);

        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}