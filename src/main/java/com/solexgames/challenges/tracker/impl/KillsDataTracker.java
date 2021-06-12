package com.solexgames.challenges.tracker.impl;

import com.solexgames.challenges.tracker.DataTracker;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@Getter
@RequiredArgsConstructor
public class KillsDataTracker implements DataTracker<Integer> {

    private int currentKills;

    @Override
    public String getName() {
        return "Kills";
    }

    @Override
    public boolean hasCompleted(Integer data) {
        return this.currentKills >= data;
    }
}
