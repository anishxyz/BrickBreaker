package org.cis120;

import java.awt.*;

/** Brick game object which is a rectangle created from many parameters.
 * Will be placed on GameCourt as a grid of bricks
 */
public class Brick extends GameObj {

    //brick constants
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;

    private Color color;

    public Brick(int courtWidth, int courtHeight, int posX, int posY, Color color) {
        super(INIT_VEL_X, INIT_VEL_Y, posX, posY, WIDTH, HEIGHT, courtWidth, courtHeight);

        this.color = color;
    }

    public void setColor(Color col) {
        this.color = col;
    }

    public Color getColor() {
        return this.color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }

    public static int getBrickWidth() {
        return WIDTH;
    }

    public static int getBrickHeight() {
        return HEIGHT;
    }

    public void interact(GameCourt g, Ball b) {
        Color c = g.getCourtColor();
        if (b.intersects(this) && !this.color.equals(c)) {
            g.incrementScore();
            this.setColor(c);
            b.bounce(b.hitObj(this));
        }
    }

}