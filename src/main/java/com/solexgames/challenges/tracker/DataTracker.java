package com.solexgames.challenges.tracker;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

public interface DataTracker {

    String getName();

    boolean canAchieve(Object data);

    String getStatusMessage(Object data);

}
