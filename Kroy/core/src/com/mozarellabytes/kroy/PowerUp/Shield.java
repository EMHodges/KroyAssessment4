package com.mozarellabytes.kroy.PowerUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

/**
 * Shield makes the Fire Truck immune to damage
 * for 5 seconds
 */
public class Shield extends PowerUp {

    final float timeInShield;
    float elapsedTime;
    FireTruck truck;

    /**
     * Constructor for Shield
     *
     * @param location  where the power up spawns
     */
    public Shield(Vector2 location) {
        super("shield", location);
        timeInShield = 5;
        elapsedTime = 0;
    }


    @Override
    public void update() {
        super.update();
        if (truck != null) {
            canBeRendered = false;
            this.setPosition(new Vector2(0,0));
            elapsedTime += Gdx.graphics.getRawDeltaTime();
            checkIfFinishedShield();
        }
    }

    @Override
    public void invokePower(FireTruck truck) {
        this.truck = truck;
        truck.setShield(true);
    }

    /**
     * Turns the shield off once time has expired
     */
    private void checkIfFinishedShield() {
        if (elapsedTime >= timeInShield){
            truck.setShield(false);
            canBeDestroyed = true;
        }
    }
}
