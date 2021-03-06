package com.mozarellabytes.kroy.PowerUp;

/**********************************************************************************
                            Added for assessment 4
 **********************************************************************************/

import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

/** Added for assessment 4 to implement the power
 * up functionality
 *
 * Water resets a truck's reserve to the truck's maximum
 * reserve
 */
public class Water extends PowerUp {

    /**
     * Constructor for Water
     *
     * @param location where the PowerUp spawns on the map
     */
    public Water(Vector2 location) {
        super("water", location);
    }

    /**
     * This restores the truck's reserve and then sets the
     * appropriate flags so that the PowerUp can be
     * removed from gameScreen
     *
     * @param truck truck that gets the effect of the fire truck
     */
    @Override
    public void invokePower(FireTruck truck) {
        isPowerCurrentlyInvoked = true;
        refillReserve(truck);
        removePowerUp();
    }

    /**
     * Refills the truck to full reserve capacity
     *
     * @param truck to get refilled
     */
    private void refillReserve(FireTruck truck) {
        truck.setReserve(truck.type.getMaxReserve());
    }

    @Override
    public String getName() {
        return "Water";
    }

    @Override
    public String getDesc() {
        return "Restores the fire truck's water supply back to full";
    }

}
