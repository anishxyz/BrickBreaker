import java.awt.*;

public class SpeedBrick extends Brick {

    public SpeedBrick(int courtWidth, int courtHeight, int posX, int posY, Color color) {
        super(courtWidth, courtHeight, posX, posY, color);
    }

    @Override
    public void interact(GameCourt g, Ball b) {
        if (b.intersects(this) && !this.getColor().equals(GameCourt.getCourtColor())) {
            b.speedChange(true, 1);
        }
        super.interact(g, b);
    }
}

