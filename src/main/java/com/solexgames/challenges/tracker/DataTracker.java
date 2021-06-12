package com.solexgames.challenges.tracker;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

public interface DataTracker<K> {

    String getName();

    boolean hasCompleted(K data);

}
