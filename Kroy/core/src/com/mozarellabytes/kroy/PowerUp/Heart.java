package com.mozarellabytes.kroy.PowerUp;

/**********************************************************************************
                                    Added for assessment 4
 **********************************************************************************/

import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

/** Added for assessment 4 to implement the power
 * up functionality
 *
 * Heart resets the Fire Truck's HP (health points) to
 * it Fire Truck's maximum HP
 */
public class Heart extends PowerUp {

    /**
     * Constructor for Heart
     * @param location where the PowerUp spawns on the map
     */
    public Heart(Vector2 location) { super("heart", location); }

    /**
     * This restores the truck's HP and then sets the
     * appropriate flags so that the PowerUp can be
     * removed from gameScreen
     * @param truck truck that gets the effect of the fire truck
     */
    @Override
    public void invokePower(FireTruck truck) {
        isPowerCurrentlyInvoked = true;
        restoreTrucksHP(truck);
        removePowerUp();
    }

    /**
     * Replenish health
     * @param truck to heal
     */
    private void restoreTrucksHP(FireTruck truck) {
        int fullHP = (int)truck.type.getMaxHP();
        truck.setHP(fullHP);
    }

    @Override
    public String getName() { return "Heart"; }

    @Override
    public String getDesc() { return "Heals the fire truck to full HP"; }

}
