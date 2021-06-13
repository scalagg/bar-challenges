package com.solexgames.challenges.tracker.impl;

import com.solexgames.challenges.tracker.DataTracker;
import com.solexgames.core.util.Color;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.ChatColor;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@Getter
@Setter
@NoArgsConstructor
public class KillsDataTracker implements DataTracker {

    private int currentKills;

    @Override
    public String getName() {
        return "Kills";
    }

    @Override
    public boolean canAchieve(Object data) {
        return data instanceof Integer && this.currentKills == (Integer) data;
    }

    @Override
    public boolean hasCompleted(Object data) {
        return data instanceof Integer && this.currentKills >= (Integer) data;
    }

    @Override
    public String getStatusMessage(Object data) {
        return data instanceof Integer ? ChatColor.GRAY + "(" + Color.MAIN_COLOR + this.currentKills + "/" + data + ChatColor.GRAY + ")" : "";
    }
}
