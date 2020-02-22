package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import com.mozarellabytes.kroy.Utilities.SoundFX;


/**
 * FireStation is a class created when it is called from GameScreen.
 * This class contains the location and sprite of the FireStation.
 * The FireStation spawns, repairs and refills the firetrucks and
 * ensures that trucks do not collide
 */

public class FireStation {

    /**
     * X and Y co-ordinates of the FireStation's position on the game screen
     * in tiles
     */
    private final int x,y;

    private final int w;

    /** The height of the sprite measured in tiles */
    private final int h;

    private final Rectangle area;

    /** A tile inside the station where a truck can be repaired and refilled */
    private ArrayList<Vector2> bayTiles;

    /** The sprite image for the station */
    private final Texture texture, deadTexture;

    /** List of active fire trucks
     * @link FireTruck */
    private final ArrayList<FireTruck> trucks;

    private final ArrayList<Patrol> patrols;

    private float HP;

    /**
     * Constructs the Firestation with at a given position with locations
     * for the repair and refill tiles and the spawn tiles.
     *
     * @param x  x coordinate of Station in tiles (lower left point)
     * @param y  y coordinate of Station in tiles (lower left point)
     */
    public FireStation(int x, int y) {
        this.x = x;
        this.y = y;
        this.w=6;
        this.h=3;
        bayTiles = new ArrayList<>();
        for (int i=0; i<4; i++) bayTiles.add(new Vector2(x + i + 1, y));
        this.texture = new Texture(Gdx.files.internal("sprites/station/extended_station.png"));
        this.deadTexture = new Texture(Gdx.files.internal("sprites/fortress/fortress_revs_dead.png")); // change me pls
        this.trucks = new ArrayList<FireTruck>();
        this.patrols = new ArrayList<Patrol>();
        this.HP=100.0f;
        this.area = new Rectangle(this.x - (float) this.w/2, this.y - (float) this.h/2,
                this.w, this.h);
    }



    /**
     * Creates a fire truck of type specified from FireTruckType. It signals to
     * the game state that a truck has been created and add the truck to the
     * arraylist this.truck so the game screen can iterate through all active trucks
     * @param truck truck to add to the arrayList of active trucks
     *
     */
    public void spawn(FireTruck truck) {
        if (SoundFX.music_enabled) {
            SoundFX.sfx_truck_spawn.play();
        }
        this.trucks.add(truck);
    }

    public void spawn(Patrol patrol) {
        if (SoundFX.music_enabled) {
            SoundFX.sfx_truck_spawn.play();
        }
        this.patrols.add(patrol);
    }

    public void drawStats(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.rect(this.getPosition().x + 2.76f, this.getPosition().y + 2.9f, 0.55f, 1.2f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeMapRenderer.rect(this.getPosition().x + 2.86f, this.getPosition().y + 3f, 0.34f, 1f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        shapeMapRenderer.rect(this.getPosition().x + 2.86f, this.getPosition().y + 3f, 0.34f, this.getHP() / 100 * 1f, Color.RED, Color.RED, Color.RED, Color.RED);
    }

    /**
     * Calls the repair and refill methods. When a truck is within the station
     * (the trucks position is the same as one of the station's bay tiles) the
     * truck will repair and refill at the same time.
     */
    public void restoreTrucks() {
        for (FireTruck truck : this.trucks) {
            if (this.HP > 0) {
                for (Vector2 bayTile : bayTiles) {
                    if (truck.getPosition().equals(bayTile)) {
                        repair(truck);
                        refill(truck);
                    }
                }
             }
        }
    }


    /**
     * Increments the truck's HP until the truck's HP equals the truck's maximum
     * HP
     * @param truck truck that is being refilled
     */
    private void refill(FireTruck truck) {
        if (truck.getReserve() < truck.type.getMaxReserve()) {
            truck.refill(Math.min(0.6f, truck.getType().getMaxReserve() - truck.getReserve()));
        }
    }

    /**
     * Increments the truck's reserve until the truck's reserve equals the
     * truck's maximum reserve
     * @param truck truck that is being repaired
     */
    private void repair(FireTruck truck) {
        if (truck.getHP() < truck.type.getMaxHP()) {
            truck.repair(Math.min(0.4f, truck.getType().getMaxHP() - truck.getHP()));
        }
    }

    /**
     * Called when a truck's HP reaches 0, it removes the truck from the
     * array list of active trucks and the game screen.
     *
     * @param truck truck that is being removed from the arrayList of active trucks
     */
    public void destroyTruck(FireTruck truck) {
        this.trucks.remove(truck);
    }

    public void destroyPatrol(Patrol patrol) {
        this.patrols.remove(patrol);
    }


    public void damage(float HP){
        this.HP -= HP;
    }

    public float getHP() {
        return this.HP;
    }
    /**
     * Checks that no more than one truck occupies a tile at a time by checking trucks
     * are not moving towards each other and that a moving truck is not going to go onto
     * the same tile as a stationary truck. If two trucks are going to collide reset
     * trucks is called.
     */
    public void checkForCollisions() {
        for (FireTruck truck : trucks) {
            for (FireTruck truck2 : trucks) {
                if (!(truck.equals(truck2))) {
                    if (!truck.trailPath.isEmpty()) {
                        Vector2 truck2tile = new Vector2(Math.round(truck2.getPosition().x), Math.round(truck2.getPosition().y));
                        Vector2 truckstile = new Vector2((float)Math.floor(truck2.getPosition().x),(float) Math.floor(truck2.getPosition().y));
                        if (!truck2.trailPath.isEmpty() && truck.trailPath.first().equals(truck2.trailPath.first())) {
                            truck.setCollision();
                            truck2.setCollision();
                            resetTruck(truck, truck2);
                        } else if (truck.trailPath.first().equals(truck2tile)) {
                            resetTruck(truck, truck2);
                            truck.trailPath.clear();
                            truck2.trailPath.clear();
                        } else if (truck.trailPath.first().equals(truckstile)) {
                            resetTruck(truck, truck2);
                        }
                    }
                }
            }
            /*for (Patrol patrol : this.patrols) {
                Vector2 patroltile = new Vector2(Math.round(patrol.getPosition().x), Math.round(patrol.getPosition().y));
                Vector2 truckstile = new Vector2((float)Math.floor(truck.getPosition().x),(float) Math.floor(truck.getPosition().y));
                if (truckstile.equals(patroltile)) {
                    patrol.setAttacking(true);
                }
            }*/
        }
    }

    /** Resets two trucks - is called when both trucks are moving towards each other
     * It removes their paths so they halt on the tile of the collision. It then adds
     * the nearest tile to their path, the trucks move to this tile so that after the
     * collision the trucks are positioned at the centre of adjacent tiles.
     *
     * @param truck one truck involved in the collision
     * @param truck2 the second truck involved in the collision
     */
    private void resetTruck(FireTruck truck, FireTruck truck2) {
        if (SoundFX.music_enabled) {
            SoundFX.sfx_horn.play();
        }

        Vector2 hold = truck.trailPath.first();

        truck.resetPath();
        truck.addTileToPath(truck.getPosition());
        truck.addTileToPath(new Vector2 ((float)Math.floor(truck.getX()),(float)Math.floor(truck.getY())));

        truck2.resetPath();
        truck2.addTileToPath(truck2.getPosition());
        truck2.addTileToPath(hold);
    }


    /** Draws the firetruck to the gameScreen
     * @param mapBatch batch being used to render to the gameScreen */
    public void draw(Batch mapBatch) {
        mapBatch.draw(this.texture, this.x, this.y, this.w, this.h);
    }
    public DestroyedEntity getDestroyedStation(){
        return new DestroyedEntity(this.deadTexture, this.x, this.y, 5, 3);
    }

    public ArrayList<FireTruck> getTrucks() {
        return this.trucks;
    }

    public ArrayList<Patrol> getPatrol() {
        return this.patrols;
    }

    public FireTruck getTruck(int i) {
        return this.trucks.get(i);
    }

    public Vector2 getPosition() {
        return new Vector2(this.x,this.y);
    }

    /*public Patrols getPatrol(int i) {
        return this.patrols.get(i);
    }*/
}