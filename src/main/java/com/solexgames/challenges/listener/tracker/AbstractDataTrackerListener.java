package com.solexgames.challenges.listener.tracker;

import com.solexgames.challenges.tracker.DataTracker;
import lombok.NoArgsConstructor;
import org.bukkit.event.Listener;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@NoArgsConstructor
public abstract class AbstractDataTrackerListener implements Listener {

    /**
     * Returns the paired Data Tracker
     *
     * @return Class which uses the listener
     */
    public abstract Class<? extends DataTracker> getPairedDataTracker();

}
