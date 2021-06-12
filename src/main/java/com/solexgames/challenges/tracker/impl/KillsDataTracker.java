package com.solexgames.challenges.tracker.impl;

import com.solexgames.challenges.tracker.DataTracker;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@Getter
@NoArgsConstructor
public class KillsDataTracker implements DataTracker {

    private int currentKills;

    @Override
    public String getName() {
        return "Kills";
    }

    @Override
    public boolean hasCompleted(Object data) {
        return data instanceof Integer && this.currentKills >= (Integer) data;
    }
}
