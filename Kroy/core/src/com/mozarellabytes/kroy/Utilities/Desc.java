package com.mozarellabytes.kroy.Utilities;

/**********************************************************************************
                             Added for assessment 4
 **********************************************************************************/

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

/** Added for assessment 4 to implement save functionality
 *
 * Descriptions of the complex classes that need to be saved
 * to the save file. We use a description of the object and
 * not the object itself as we do not need all the values that
 * come with the whole object, therefore we define the critical
 * values we need here.
 */
public class Desc {

    /**
     * FireTruck Description
     */
    public static class FireTruck {
        public String type, rotation;
        public int x, y;
        public float health, reserve;
    }

    /**
     * FireStation Description
     */
    public static class FireStation {
        public float x, y, health;
    }

    /**
     * Fortress Description
     */
    public static class Fortress {
        public String type;
        public float x, y, health;
    }

    /**
     * Patrol Description
     */
    public static class Patrol {
        public String type, name;
        public float x, y, health;
        public Queue<Vector2> path;
    }

}

