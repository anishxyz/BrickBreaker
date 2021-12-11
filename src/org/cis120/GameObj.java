package org.cis120;

import java.awt.Graphics;

/**
 * An object in the game.
 * <p>
 * Game objects exist in the game court. They have a position, velocity, size
 * and bounds. Their velocity controls how they move; their position should
 * always be within their bounds.
 */
public abstract class GameObj {
    /*
     * Current position of the object (in terms of graphics coordinates)
     *
     * Coordinates are given by the upper-left hand corner of the object. This
     * position should always be within bounds:
     * 0 <= px <= maxX 0 <= py <= maxY
     */
    private int px;
    private int py;

    /* Size of object, in pixels. */
    private int width;
    private int height;

    /* Velocity: number of pixels to move every time move() is called. */
    private int vx;
    private int vy;

    /*
     * Upper bounds of the area in which the object can be positioned. Maximum
     * permissible x, y positions for the upper-left hand corner of the object.
     */
    private int maxX;
    private int maxY;

    /**
     * Constructor
     */
    public GameObj(
            int vx, int vy, int px, int py, int width, int height, int courtWidth,
            int courtHeight
    ) {
        this.vx = vx;
        this.vy = vy;
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;

        // take the width and height into account when setting the bounds for
        // the upper left corner of the object.
        this.maxX = courtWidth - width;
        this.maxY = courtHeight - height;
    }

    /***
     * GETTERS
     **********************************************************************************/
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }

    public int getVx() {
        return this.vx;
    }

    public int getVy() {
        return this.vy;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    /**************************************************************************
     * SETTERS
     **************************************************************************/
    public void setPx(int px) {
        this.px = px;
        clip();
    }

    public void setPy(int py) {
        this.py = py;
        clip();
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    /**************************************************************************
     * UPDATES AND OTHER METHODS
     **************************************************************************/

    /**
     * Prevents the object from going outside of the bounds of the area
     * designated for the object (i.e. Object cannot go outside of the active
     * area the user defines for it).
     */
    private void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }

    /**
     * Moves the object by its velocity. Ensures that the object does not go
     * outside its bounds by clipping.
     */
    public void move() {
        this.px += this.vx;
        this.py += this.vy;

        clip();
    }

    /**
     * Determine whether this game object is currently intersecting another
     * object.
     * <p>
     * Intersection is determined by comparing bounding boxes. If the bounding
     * boxes overlap, then an intersection is considered to occur.
     *
     * @param that The other object
     * @return Whether this object intersects the other object.
     */
    public boolean intersects(GameObj that) {
        return this.hitObj(that) != null;
    }

    /**
     * Update the velocity of the object in response to hitting an obstacle in
     * the given direction. If the direction is null, this method has no effect
     * on the object.
     *
     * @param d The direction in which this object hit an obstacle
     */
    public void bounce(Direction d) {
        if (d == null) {
            return;
        }

        switch (d) {
            case UP:
            case DOWN:
                this.vy = -this.vy;
                break;
            case LEFT:
            case RIGHT:
                this.vx = -this.vx;
                break;
            default:
                break;
        }
    }

    /**
     * Determine whether the game object will hit a wall in the next time step.
     * If so, return the direction of the wall in relation to this game object.
     *
     * @return Direction of impending wall, null if all clear.
     */
    public Direction hitWall() {
        if (this.px + this.vx < 0) {
            return Direction.LEFT;
        } else if (this.px + this.vx > this.maxX) {
            return Direction.RIGHT;
        }

        if (this.py + this.vy < 0) {
            return Direction.UP;
        } else if (this.py + this.vy > this.maxY) {
            return Direction.DOWN;
        } else {
            return null;
        }
    }

    /**
     * Determine whether the game object will hit another object in the next
     * time step. If so, return the side of target object contacted.
     *
     * @param that The other object
     * @return Direction of impending object, null if all clear.
     */
    public Direction hitObj(GameObj that) {
        //checks if x position of @this is within the bounds of @that
        if (this.px + this.width >= that.getPx() && this.px <= that.getPx() + that.getWidth()) {
            //checks if contact is made on the top edge of @that
            // (i.e. bottom part of @this contacts @that)
            if (this.py + this.height >= that.getPy() && this.py <= that.getPy()) {
                return Direction.UP;
            }
            //checks if contact is made on the bottom edge of @that
            // (i.e. top part of @this contacts @that)
            if (this.py + this.height >= that.getPy() + that.getHeight()
                    && this.py <= that.getPy() + that.getHeight()) {
                return Direction.DOWN;
            }
        }
        //checks if y position of @this is within the bounds of @that
        if (this.py + this.height >= that.getPy() && this.py <= that.getPy() + that.getHeight()) {
            //checks if contact is made on the left edge of @that
            // (i.e. right part of @this contacts @that)
            if (this.px + this.width >= that.px && this.px <= that.px) {
                return Direction.LEFT;
            }
            //checks if contact is made on the right edge of @that
            // (i.e. left part of @this contacts @that)
            if (this.px <= that.px + that.getWidth()
                    && this.px + this.width >= that.px + that.getWidth()) {
                return Direction.RIGHT;
            }
        }
        return null;
    }

    /**
     * Method to change the speed of a game object by a certain amount
     * Changes both vx and vy by that amount
     *
     * @param t    boolean, true for adding speed, false for removing speed
     * @param incr int for how much to add/subtract to speed in each direction
     */
    public void speedChange(boolean t, int incr) {
        int c;
        if (t) {
            c = incr;
        } else {
            c = -incr;
        }
        if (vx > 0) {
            this.setVx(vx + c);
        }
        if (vx < 0) {
            this.setVx(vx - c);
        }
        if (vy > 0) {
            this.setVy(vy + c);
        }
        if (vy < 0) {
            this.setVy(vy - c);
        }
    }


    /**
     * Default draw method that provides how the object should be drawn in the
     * GUI. This method does not draw anything. Subclass should override this
     * method based on how their object should appear.
     *
     * @param g The <code>Graphics</code> context used for drawing the object.
     *          Remember graphics contexts that we used in OCaml, it gives the
     *          context in which the object should be drawn (a canvas, a frame,
     *          etc.)
     */
    public abstract void draw(Graphics g);
}