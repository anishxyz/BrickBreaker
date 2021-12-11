import java.awt.*;

public class DoubleBrick extends Brick {

    public DoubleBrick(int courtWidth, int courtHeight, int posX, int posY, Color color) {
        super(courtWidth, courtHeight, posX, posY, color);
    }

    @Override
    public void interact(GameCourt g, Ball b) {
        Color one = GameCourt.getDoubleBrickColorOne();
        Color two = GameCourt.getDoubleBrickColorTwo();
        Color c = GameCourt.getCourtColor();
        if (b.intersects(this) && this.getColor().equals(one)) {
            this.setColor(two);
            b.bounce(b.hitObj(this));
        } else if (b.intersects(this) && this.getColor().equals(two)) {
            super.interact(g, b);
        }
    }
}

