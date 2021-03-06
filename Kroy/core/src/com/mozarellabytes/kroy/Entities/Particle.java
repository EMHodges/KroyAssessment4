package com.mozarellabytes.kroy.Entities;

/**********************************************************************************
                                Added for assessment 4
 **********************************************************************************/

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

/** Edited for assessment 4 - combined what was previously
 * water particle and blaster particle into one particle
 * class
 *
 * One of many colourful visual rectangles which spawn
 * when a FireTruck attacks a Fortress, or when the Boss
 * attacks the FireStation. It goes towards the target
 * and deals damage once it reaches.
 */
public class Particle {

    /** Random colour of Rectangle */
    private Color colour;

    /** Random size of the Rectangle */
    private final float size;

    /** The position where the water particle starts from (the position
     * of the truck)
     */
    private final Vector2 startPosition;

    /** The current position of the water particle */
    private Vector2 currentPosition;

    private Color[] colours;

    private final Object target;

    /** The end position of the water particle (the fortress the truck
     * is attacking)
     */
    private Vector2 targetPosition;

    /**
     * Constructor for Particle
     *
     * @param startPosition source position of particle
     * @param endPosition   target position of particle
     * @param target        target entity
     */
    public Particle(Vector2 startPosition, Vector2 endPosition, Object target) {
        this.target = target;
        if (target instanceof Fortress) setWaterColour();
        if (target instanceof FireStation) setBlasterColour();

        this.size = (float)(Math.random()/5 + 0.1);
        this.startPosition = new Vector2(startPosition.x, startPosition.y);
        this.currentPosition = startPosition;
        this.targetPosition = endPosition;
        createTargetPosition(endPosition);
    }

    /**
     * Sets random water colour (fire truck)
     */
    private void setWaterColour() {
        this.colours = new Color[] {Color.CYAN, Color.NAVY, Color.BLUE, Color.PURPLE, Color.SKY, Color.TEAL};
        this.colour = colours[(int)(Math.random() * 4)];
    }

    /**
     * Sets random blaster colour (boss patrol)
     */
    private void setBlasterColour() {
        this.colours = new Color[] {Color.RED, Color.CHARTREUSE, Color.FIREBRICK, Color.MAGENTA, Color.ORANGE, Color.BROWN};
        this.colour = colours[(int)(Math.random() * 4)];
    }

    /**
     * Scatter particles around the target instead of
     * just at once singular location
     *
     * @param target    central position to aim around
     */
    public void createTargetPosition(Vector2 target) {
        float xCoord = (float)(Math.random()-0.5+target.x);
        float yCoord = (float)(Math.random()-0.5+target.y);
        this.targetPosition = new Vector2(xCoord, yCoord);
    }

    /**
     * Updates the position of the WaterParticle
     * using the Interpolation function
     */
    public void updatePosition() {
        this.currentPosition = this.startPosition.interpolate(this.targetPosition, 0.2f, Interpolation.circle);
    }

    /**
     * Checks if the Particle has reached the the Fortress
     *
     * @return  <code>true</code> if Particle hit Target entity
     *          <code>false</code> otherwise
     */
    public boolean isHit() {
        return (((int) this.targetPosition.x == (int) this.currentPosition.x) &&
                ((int) this.targetPosition.y == (int) this.currentPosition.y));
    }

    public Object getTarget() { return this.target; }

    public float getSize() { return this.size; }

    public Color getColour() { return this.colour; }

    public Vector2 getPosition() { return this.currentPosition; }

    public void setPositionX(float x) {this.currentPosition.x = x;}

    public void setPositionY(float y) {this.currentPosition.y = y;}

}



