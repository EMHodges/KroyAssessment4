package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * FireTruckType is an enum defining the patrols that can be present in the game.
 * Each patrol type has a unique reserve, speed, trail colour, range, and attack points.
 * This allows there to be numerous different types of FireTrucks and numerous patrols
 * of each type without having to randomly generate values which may make the game
 * unplayable in some instances and too easy in other instances.
 */
public enum PatrolType {

    /**
     * The preset values for the different patrol types includes the type's:
     * texture, speed, name, name, attack range, attack points
     */
    Green(new Texture(Gdx.files.internal("sprites/Patrol/greenPatrol.png")), 3, "Green Patrol", 0.16f, 100, new Vector2(17, 2), new Vector2(47, 2), new Vector2(47, 20), new Vector2(17, 20)),
    Blue(new Texture(Gdx.files.internal("sprites/Patrol/bluePatrol.png")), 6, "Blue Patrol", 0.08f, 150, new Vector2(2, 27), new Vector2(2, 19), new Vector2(18,19), new Vector2(18, 27)),
    Peach(new Texture(Gdx.files.internal("sprites/Patrol/peachPatrol.png")), 3, "Peach Patrol",  0.16f, 100, new Vector2(2, 4), new Vector2(9, 16), new Vector2(31, 21), new Vector2(26, 9)),
    Violet(new Texture(Gdx.files.internal("sprites/Patrol/violetPatrol.png")), 6, "Violet Patrol",  0.08f, 150, new Vector2(48, 18), new Vector2(38, 28), new Vector2(28, 18), new Vector2(38, 8)),
    Yellow(new Texture(Gdx.files.internal("sprites/Patrol/yellowPatrol.png")), 6, "Yellow Patrol", 0.08f, 150, new Vector2(46, 13), new Vector2(46, 21), new Vector2(27, 21), new Vector2(27, 13)),
    Boss(new Texture(Gdx.files.internal("sprites/Patrol/PatrolBoss.png")), 1, "Boss Patrol",  0.04f, 150, new Vector2(44, 11), new Vector2(43, 11), new Vector2(6, 11), new Vector2(6, 10));

    /**
     * Texture for this type of patrol
     */
    private final Texture texture;

    /**
     * The maximum amount of health points this type of patrol can have,
     * also the value of the patrol's health points when it is spawned
     */
    private final float maxHP;

    /**
     * The speed at which this type of patrol moves around the map
     */
    private final float speed;

    /**
     * The speed at which this type of patrol moves around the map
     */
    private final String name;

    /**
     * The Attack points the patrol deals when in the minigame
     */
    private final float AP;

    private Vector2 target;

    /**
     * Points the patrol will cycle through
     */
    private final Vector2 point1;
    private final Vector2 point2;
    private final Vector2 point3;
    private final Vector2 point4;

    /**
     * Constructs the PatrolType
     *
     * @param texture Texture of this type of patrol
     * @param speed The speed of this type of patrol
     * @param name The name for this type of patrol
     * @param AP the attack points for this type of patrol
     */
    PatrolType(Texture texture, int speed, String name, float AP, float maxHP, Vector2 point1, Vector2 point2, Vector2 point3, Vector2 point4) {
        this.texture = texture;
        this.maxHP = maxHP;
        this.speed = speed;
        this.name = name;
        this.AP = AP;
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.point4 = point4;
        this.target = point2;
    }

    public float getMaxHP() {
        return this.maxHP;
    }
    public float getSpeed() {
        return this.speed;
    }
    public Texture getTexture(){
        return this.texture;
    }
    public Vector2 getPoint1(){
        return point1;
    }
    public Vector2 getPoint2(){
        return point2;
    }
    public Vector2 getPoint3(){
        return point3;
    }
    public Vector2 getPoint4(){
        return point4;
    }
    public Vector2 getTarget() {
        return target;
    }
    public void setTarget(Vector2 target) {
        this.target = target;
    }
}
