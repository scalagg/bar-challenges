package com.solexgames.challenges.tracker.impl;

import com.solexgames.challenges.tracker.DataTracker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@Getter
@Setter
@NoArgsConstructor
public class DeathDataTracker implements DataTracker {

    private int currentDeaths;

    @Override
    public String getName() {
        return "Deaths";
    }

    @Override
    public boolean hasCompleted(Object data) {
        return data instanceof Integer && this.currentDeaths >= (Integer) data;
    }
}
