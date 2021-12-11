package org.cis120;

import java.awt.*;

public class DoubleBrick extends Brick {

    public DoubleBrick(int courtWidth, int courtHeight, int posX, int posY, Color color) {
        super(courtWidth, courtHeight, posX, posY, color);
    }

    @Override
    public void interact(GameCourt g, Ball b) {
        Color one = g.getDoubleBrickColorOne();
        Color two = g.getDoubleBrickColorTwo();
        Color c = g.getCourtColor();
        if (b.intersects(this) && this.getColor().equals(one)) {
            this.setColor(two);
            b.bounce(b.hitObj(this));
        } else if (b.intersects(this) && this.getColor().equals(two)) {
            super.interact(g, b);
        }
    }
}

